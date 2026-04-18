package com.rename_mod.lagkill.client;

import com.rename_mod.lagkill.LagKillMod;
import com.rename_mod.lagkill.config.LagKillConfig;
import com.rename_mod.lagkill.perf.AdaptiveQualityController;
import com.rename_mod.lagkill.perf.FrameTimeMonitor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class LagKillClientMod implements ClientModInitializer {
    private final FrameTimeMonitor frameTimeMonitor = new FrameTimeMonitor();
    private final AdaptiveQualityController adaptiveQualityController = new AdaptiveQualityController();
    private final GameOptionTuner gameOptionTuner = new GameOptionTuner();

    private int ticks;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            frameTimeMonitor.onFrame();
            adaptiveQualityController.tick(frameTimeMonitor);
            ticks++;

            if (ticks % 40 == 0) {
                RuntimeProfile profile = chooseProfile();
                gameOptionTuner.applyProfile(client, profile);
            }

            if (ticks % 200 == 0) {
                LagKillMod.LOGGER.info(
                    "LagKill runtime | avg={}ms p95={}ms overload={}",
                    String.format("%.2f", frameTimeMonitor.smoothedMs()),
                    String.format("%.2f", frameTimeMonitor.p95Ms()),
                    adaptiveQualityController.overloadTicks()
                );
            }
        });
    }

    private RuntimeProfile chooseProfile() {
        if (adaptiveQualityController.emergencyMode()) {
            return RuntimeProfile.emergency();
        }

        LagKillConfig.ConfigState config = LagKillConfig.get();

        if (config.recordingModeEnabled()) {
            if (frameTimeMonitor.p95Ms() > 24.0 || adaptiveQualityController.recoveryMode()) {
                return RuntimeProfile.recordingSafe(config.recordingFpsCap());
            }
            return RuntimeProfile.recordingQuality(config.recordingFpsCap());
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
