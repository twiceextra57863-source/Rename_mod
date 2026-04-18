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

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("lagkill.properties");
    private static ConfigState state = new ConfigState(true, Profile.BALANCED, true, true, true, 240, true, 60);

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
            parseInt(properties.getProperty("recordingFpsCap", "60"), 60)
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

    public record ConfigState(
        boolean adaptiveEnabled,
        Profile profile,
        boolean dynamicRenderDistance,
        boolean dynamicSimulationDistance,
        boolean adaptiveFpsCap,
        int targetFpsCap,
        boolean recordingModeEnabled,
        int recordingFpsCap
    ) {
    }
}
