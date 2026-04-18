package com.rename_mod.lagkill.config;

import com.rename_mod.lagkill.LagKillMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class LagKillVisualConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("lagkill_visual.properties");

    private static VisualState state = new VisualState(
        true,
        true,
        false,
        0.15,
        true,
        1.08,
        true,
        true
    );

    private LagKillVisualConfig() {
    }

    public static void bootstrap() {
        load();
        save();
    }

    public static VisualState get() {
        return state;
    }

    public static void set(VisualState next) {
        state = next;
        save();
    }

    public static void toggleHurtShake() {
        set(new VisualState(!state.disableHurtShake(), state.disableFireOverlay(), state.motionBlurEnabled(), state.motionBlurStrength(), state.waterClarityBoost(), state.saturationBoost(), state.cleanFlameImpact(), state.dashboardEnabled()));
    }

    public static void toggleFireOverlay() {
        set(new VisualState(state.disableHurtShake(), !state.disableFireOverlay(), state.motionBlurEnabled(), state.motionBlurStrength(), state.waterClarityBoost(), state.saturationBoost(), state.cleanFlameImpact(), state.dashboardEnabled()));
    }

    public static void toggleMotionBlur() {
        set(new VisualState(state.disableHurtShake(), state.disableFireOverlay(), !state.motionBlurEnabled(), state.motionBlurStrength(), state.waterClarityBoost(), state.saturationBoost(), state.cleanFlameImpact(), state.dashboardEnabled()));
    }

    public static void cycleMotionBlurStrength() {
        double next = state.motionBlurStrength() + 0.05;
        if (next > 0.35) {
            next = 0.10;
        }
        set(new VisualState(state.disableHurtShake(), state.disableFireOverlay(), state.motionBlurEnabled(), next, state.waterClarityBoost(), state.saturationBoost(), state.cleanFlameImpact(), state.dashboardEnabled()));
    }

    public static void toggleWaterClarity() {
        set(new VisualState(state.disableHurtShake(), state.disableFireOverlay(), state.motionBlurEnabled(), state.motionBlurStrength(), !state.waterClarityBoost(), state.saturationBoost(), state.cleanFlameImpact(), state.dashboardEnabled()));
    }

    public static void increaseSaturation() {
        double next = Math.min(1.30, state.saturationBoost() + 0.02);
        set(new VisualState(state.disableHurtShake(), state.disableFireOverlay(), state.motionBlurEnabled(), state.motionBlurStrength(), state.waterClarityBoost(), next, state.cleanFlameImpact(), state.dashboardEnabled()));
    }

    public static void decreaseSaturation() {
        double next = Math.max(0.90, state.saturationBoost() - 0.02);
        set(new VisualState(state.disableHurtShake(), state.disableFireOverlay(), state.motionBlurEnabled(), state.motionBlurStrength(), state.waterClarityBoost(), next, state.cleanFlameImpact(), state.dashboardEnabled()));
    }

    public static void applySmoothPreset() {
        set(new VisualState(true, true, true, 0.15, true, 1.08, true, state.dashboardEnabled()));
    }

    public static void load() {
        Properties properties = new Properties();
        if (Files.exists(CONFIG_PATH)) {
            try (InputStream input = Files.newInputStream(CONFIG_PATH)) {
                properties.load(input);
            } catch (IOException exception) {
                LagKillMod.LOGGER.warn("LagKill visual config load failed: {}", exception.getMessage());
            }
        }

        state = new VisualState(
            Boolean.parseBoolean(properties.getProperty("disableHurtShake", "true")),
            Boolean.parseBoolean(properties.getProperty("disableFireOverlay", "true")),
            Boolean.parseBoolean(properties.getProperty("motionBlurEnabled", "false")),
            parseDouble(properties.getProperty("motionBlurStrength", "0.15"), 0.15),
            Boolean.parseBoolean(properties.getProperty("waterClarityBoost", "true")),
            parseDouble(properties.getProperty("saturationBoost", "1.08"), 1.08),
            Boolean.parseBoolean(properties.getProperty("cleanFlameImpact", "true")),
            Boolean.parseBoolean(properties.getProperty("dashboardEnabled", "true"))
        );
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Properties properties = new Properties();
            properties.setProperty("disableHurtShake", Boolean.toString(state.disableHurtShake()));
            properties.setProperty("disableFireOverlay", Boolean.toString(state.disableFireOverlay()));
            properties.setProperty("motionBlurEnabled", Boolean.toString(state.motionBlurEnabled()));
            properties.setProperty("motionBlurStrength", Double.toString(state.motionBlurStrength()));
            properties.setProperty("waterClarityBoost", Boolean.toString(state.waterClarityBoost()));
            properties.setProperty("saturationBoost", Double.toString(state.saturationBoost()));
            properties.setProperty("cleanFlameImpact", Boolean.toString(state.cleanFlameImpact()));
            properties.setProperty("dashboardEnabled", Boolean.toString(state.dashboardEnabled()));

            try (OutputStream output = Files.newOutputStream(CONFIG_PATH)) {
                properties.store(output, "LagKill visual config");
            }
        } catch (IOException exception) {
            LagKillMod.LOGGER.warn("LagKill visual config save failed: {}", exception.getMessage());
        }
    }

    private static double parseDouble(String value, double fallback) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    public record VisualState(
        boolean disableHurtShake,
        boolean disableFireOverlay,
        boolean motionBlurEnabled,
        double motionBlurStrength,
        boolean waterClarityBoost,
        double saturationBoost,
        boolean cleanFlameImpact,
        boolean dashboardEnabled
    ) {
    }
}
