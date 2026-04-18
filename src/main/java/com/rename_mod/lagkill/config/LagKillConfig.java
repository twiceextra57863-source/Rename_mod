package com.rename_mod.lagkill.config;

import com.rename_mod.lagkill.LagKillMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class LagKillConfig {
    public enum Profile {
        COMPETITIVE,
        BALANCED,
        QUALITY
    }

    public enum RendererPreference {
        AUTO,
        DEFAULT,
        VULKAN_OPTIMIZED
    }

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("lagkill.properties");
    private static ConfigState state = new ConfigState(
        true,
        Profile.BALANCED,
        true,
        true,
        true,
        240,
        true,
        60,
        RendererPreference.AUTO,
        true,
        200,
        true,
        true,
        true,
        true,
        600,
        true,
        true,
        true
    );

    private LagKillConfig() {
    }

    public static void bootstrap() {
        load();
        save();
    }

    public static ConfigState get() {
        return state;
    }

    public static void set(ConfigState next) {
        state = next;
        save();
    }

    public static void load() {
        Properties properties = new Properties();
        if (Files.exists(CONFIG_PATH)) {
            try (InputStream input = Files.newInputStream(CONFIG_PATH)) {
                properties.load(input);
            } catch (IOException exception) {
                LagKillMod.LOGGER.warn("LagKill config load failed: {}", exception.getMessage());
            }
        }

        state = new ConfigState(
            Boolean.parseBoolean(properties.getProperty("adaptiveEnabled", "true")),
            parseProfile(properties.getProperty("profile", Profile.BALANCED.name())),
            Boolean.parseBoolean(properties.getProperty("dynamicRenderDistance", "true")),
            Boolean.parseBoolean(properties.getProperty("dynamicSimulationDistance", "true")),
            Boolean.parseBoolean(properties.getProperty("adaptiveFpsCap", "true")),
            parseInt(properties.getProperty("targetFpsCap", "240"), 240),
            Boolean.parseBoolean(properties.getProperty("recordingModeEnabled", "true")),
            parseInt(properties.getProperty("recordingFpsCap", "60"), 60),
            parseRendererPreference(properties.getProperty("rendererPreference", RendererPreference.AUTO.name())),
            Boolean.parseBoolean(properties.getProperty("multiplayerBoostEnabled", "true")),
            parseInt(properties.getProperty("multiplayerTargetFps", "200"), 200),
            Boolean.parseBoolean(properties.getProperty("visualIntegrityMode", "true")),
            Boolean.parseBoolean(properties.getProperty("networkBurstGuardEnabled", "true")),
            Boolean.parseBoolean(properties.getProperty("backgroundTaskThrottleEnabled", "true")),
            Boolean.parseBoolean(properties.getProperty("startupFastPathEnabled", "true")),
            parseInt(properties.getProperty("startupFastPathTicks", "600"), 600),
            Boolean.parseBoolean(properties.getProperty("resourcePackWarmupEnabled", "true")),
            Boolean.parseBoolean(properties.getProperty("networkWarmupEnabled", "true")),
            Boolean.parseBoolean(properties.getProperty("serverPingWarmupEnabled", "true"))
        );
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Properties properties = new Properties();
            properties.setProperty("adaptiveEnabled", Boolean.toString(state.adaptiveEnabled()));
            properties.setProperty("profile", state.profile().name());
            properties.setProperty("dynamicRenderDistance", Boolean.toString(state.dynamicRenderDistance()));
            properties.setProperty("dynamicSimulationDistance", Boolean.toString(state.dynamicSimulationDistance()));
            properties.setProperty("adaptiveFpsCap", Boolean.toString(state.adaptiveFpsCap()));
            properties.setProperty("targetFpsCap", Integer.toString(state.targetFpsCap()));
            properties.setProperty("recordingModeEnabled", Boolean.toString(state.recordingModeEnabled()));
            properties.setProperty("recordingFpsCap", Integer.toString(state.recordingFpsCap()));
            properties.setProperty("rendererPreference", state.rendererPreference().name());
            properties.setProperty("multiplayerBoostEnabled", Boolean.toString(state.multiplayerBoostEnabled()));
            properties.setProperty("multiplayerTargetFps", Integer.toString(state.multiplayerTargetFps()));
            properties.setProperty("visualIntegrityMode", Boolean.toString(state.visualIntegrityMode()));
            properties.setProperty("networkBurstGuardEnabled", Boolean.toString(state.networkBurstGuardEnabled()));
            properties.setProperty("backgroundTaskThrottleEnabled", Boolean.toString(state.backgroundTaskThrottleEnabled()));
            properties.setProperty("startupFastPathEnabled", Boolean.toString(state.startupFastPathEnabled()));
            properties.setProperty("startupFastPathTicks", Integer.toString(state.startupFastPathTicks()));
            properties.setProperty("resourcePackWarmupEnabled", Boolean.toString(state.resourcePackWarmupEnabled()));
            properties.setProperty("networkWarmupEnabled", Boolean.toString(state.networkWarmupEnabled()));
            properties.setProperty("serverPingWarmupEnabled", Boolean.toString(state.serverPingWarmupEnabled()));

            try (OutputStream output = Files.newOutputStream(CONFIG_PATH)) {
                properties.store(output, "LagKill config");
            }
        } catch (IOException exception) {
            LagKillMod.LOGGER.warn("LagKill config save failed: {}", exception.getMessage());
        }
    }

    private static int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    private static Profile parseProfile(String value) {
        try {
            return Profile.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException exception) {
            return Profile.BALANCED;
        }
    }

    private static RendererPreference parseRendererPreference(String value) {
        try {
            return RendererPreference.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException exception) {
            return RendererPreference.AUTO;
        }
    }

    public record ConfigState(
        boolean adaptiveEnabled,
        Profile profile,
        boolean dynamicRenderDistance,
        boolean dynamicSimulationDistance,
        boolean adaptiveFpsCap,
        int targetFpsCap,
        boolean recordingModeEnabled,
        int recordingFpsCap,
        RendererPreference rendererPreference,
        boolean multiplayerBoostEnabled,
        int multiplayerTargetFps,
        boolean visualIntegrityMode,
        boolean networkBurstGuardEnabled,
        boolean backgroundTaskThrottleEnabled,
        boolean startupFastPathEnabled,
        int startupFastPathTicks,
        boolean resourcePackWarmupEnabled,
        boolean networkWarmupEnabled,
        boolean serverPingWarmupEnabled
    ) {
    }
}
