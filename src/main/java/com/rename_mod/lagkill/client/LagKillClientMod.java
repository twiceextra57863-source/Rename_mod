package com.rename_mod.lagkill.client;

import com.rename_mod.lagkill.LagKillMod;
import com.rename_mod.lagkill.config.LagKillConfig;
import com.rename_mod.lagkill.config.LagKillVisualConfig;
import com.rename_mod.lagkill.ui.LagKillDashboardScreen;
import com.rename_mod.lagkill.perf.AdaptiveQualityController;
import com.rename_mod.lagkill.perf.FrameTimeMonitor;
import com.rename_mod.lagkill.perf.NetworkWarmupService;
import com.rename_mod.lagkill.perf.NonVisualLagController;
import com.rename_mod.lagkill.perf.ResourcePackWarmupService;
import com.rename_mod.lagkill.perf.StartupFastPathController;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class LagKillClientMod implements ClientModInitializer {
    private final FrameTimeMonitor frameTimeMonitor = new FrameTimeMonitor();
    private final AdaptiveQualityController adaptiveQualityController = new AdaptiveQualityController();
    private final NonVisualLagController nonVisualLagController = new NonVisualLagController();
    private final NetworkWarmupService networkWarmupService = new NetworkWarmupService();
    private final ResourcePackWarmupService resourcePackWarmupService = new ResourcePackWarmupService();
    private final GameOptionTuner gameOptionTuner = new GameOptionTuner();
    private final RendererAdvisor rendererAdvisor = new RendererAdvisor();
    private final MultiplayerAdvisor multiplayerAdvisor = new MultiplayerAdvisor();

    private RendererAdvisor.BackendMode backendMode = RendererAdvisor.BackendMode.DEFAULT;
    private StartupFastPathController startupFastPathController = new StartupFastPathController(600);
    private int cachedStartupTicks = 600;
    private boolean startupWarmupDone;
    private final KeyBinding dashboardKey = KeyBindingHelper.registerKeyBinding(
        new KeyBinding("key.lagkill.dashboard", GLFW.GLFW_KEY_F8, "category.lagkill")
    );
    private int ticks;

    @Override
    public void onInitializeClient() {
        backendMode = rendererAdvisor.detect();
        LagKillMod.LOGGER.info("LagKill backend mode: {}", backendMode);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            frameTimeMonitor.onFrame();
            adaptiveQualityController.tick(frameTimeMonitor);
            ticks++;

            LagKillConfig.ConfigState config = LagKillConfig.get();
            if (config.startupFastPathTicks() != cachedStartupTicks) {
                cachedStartupTicks = config.startupFastPathTicks();
                startupFastPathController = new StartupFastPathController(cachedStartupTicks);
            }

            if (LagKillVisualConfig.get().dashboardEnabled()) {
                while (dashboardKey.wasPressed()) {
                    if (client.currentScreen instanceof LagKillDashboardScreen) {
                        client.setScreen(null);
                    } else {
                        client.setScreen(new LagKillDashboardScreen());
                    }
                }
            }

            if (!startupWarmupDone) {
                startupWarmupDone = true;
                String serverHost = multiplayerAdvisor.currentServerHost(client);
                networkWarmupService.prewarm(config.networkWarmupEnabled() || config.serverPingWarmupEnabled(), serverHost);
                resourcePackWarmupService.warmup(client, config.resourcePackWarmupEnabled());
            }

            boolean startupFastPath = config.startupFastPathEnabled() && startupFastPathController.isActive(ticks);
            boolean multiplayer = multiplayerAdvisor.isMultiplayerSession(client);
            boolean aggressiveSafeMode = config.aggressiveSafeModeEnabled() && frameTimeMonitor.p95Ms() > 45.0;

            if (ticks % 40 == 0) {
                RuntimeProfile profile = aggressiveSafeMode
                    ? RuntimeProfile.competitive()
                    : chooseProfile(startupFastPath, multiplayer, config);
                int adjustedCap = nonVisualLagController.computeFpsCap(
                    frameTimeMonitor.p95Ms(),
                    profile.maxFps(),
                    multiplayer
                );
                gameOptionTuner.applyProfile(client, profile.withMaxFps(adjustedCap), config.visualIntegrityMode());
            }

            if (ticks % 200 == 0) {
                LagKillMod.LOGGER.info(
                    "LagKill runtime | backend={} startupFastPath={} safeMode={} native={} avg={}ms p95={}ms overload={} visualIntegrity={}",
                    backendMode,
                    startupFastPath,
                    aggressiveSafeMode,
                    nonVisualLagController.nativeAccelerationEnabled(),
                    round2(frameTimeMonitor.smoothedMs()),
                    round2(frameTimeMonitor.p95Ms()),
                    adaptiveQualityController.overloadTicks(),
                    config.visualIntegrityMode()
                );
            }
        });
    }

    private static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private RuntimeProfile chooseProfile(boolean startupFastPath, boolean multiplayer, LagKillConfig.ConfigState config) {
        if (startupFastPath) {
            return RuntimeProfile.startupFast();
        }

        if (adaptiveQualityController.emergencyMode()) {
            return RuntimeProfile.emergency();
        }

        if (multiplayer && config.multiplayerBoostEnabled()) {
            if (adaptiveQualityController.recoveryMode() || frameTimeMonitor.p95Ms() > 18.0) {
                return RuntimeProfile.multiplayerSafe(config.multiplayerTargetFps());
            }
            return RuntimeProfile.multiplayerVisual(config.multiplayerTargetFps());
        }

        if (config.recordingModeEnabled()) {
            if (frameTimeMonitor.p95Ms() > 24.0 || adaptiveQualityController.recoveryMode()) {
                return RuntimeProfile.recordingSafe(config.recordingFpsCap());
            }
            return RuntimeProfile.recordingQuality(config.recordingFpsCap());
        }

        boolean vulkanOptimized = switch (config.rendererPreference()) {
            case VULKAN_OPTIMIZED -> true;
            case DEFAULT -> false;
            case AUTO -> backendMode == RendererAdvisor.BackendMode.VULKAN_MOD;
        };

        if (vulkanOptimized) {
            if (adaptiveQualityController.recoveryMode()) {
                return RuntimeProfile.vulkanBalanced();
            }
            return RuntimeProfile.vulkanQuality();
        }

        if (adaptiveQualityController.recoveryMode()) {
            return RuntimeProfile.competitive();
        }

        return switch (config.profile()) {
            case COMPETITIVE -> RuntimeProfile.competitive();
            case QUALITY -> RuntimeProfile.quality();
            case BALANCED -> RuntimeProfile.balanced();
        };
    }
}
