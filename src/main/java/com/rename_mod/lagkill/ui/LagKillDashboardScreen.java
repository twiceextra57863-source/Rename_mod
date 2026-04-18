package com.rename_mod.lagkill.ui;

import com.rename_mod.lagkill.config.LagKillVisualConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;

public class LagKillDashboardScreen extends Screen {
    private final List<String> categories = List.of(
        "General",
        "Sodium",
        "Iris",
        "Camera",
        "Water",
        "Motion",
        "Multiplayer"
    );

    private String selectedCategory = "General";

    public LagKillDashboardScreen() {
        super(Text.literal("LagKill Dashboard"));
    }

    @Override
    protected void init() {
        buildCategoryButtons();
        buildCategoryPanel();
    }

    private void buildCategoryButtons() {
        int x = 20;
        for (String category : categories) {
            final String categoryName = category;
            addDrawableChild(ButtonWidget.builder(Text.literal(category), button -> {
                selectedCategory = categoryName;
                clearAndInit();
            }).dimensions(x, 20, 90, 20).build());
            x += 94;
            if (x > this.width - 100) {
                break;
            }
        }
    }

    private void buildCategoryPanel() {
        int left = 20;
        int top = 60;

        switch (selectedCategory) {
            case "General", "Sodium", "Iris", "Multiplayer" -> buildGeneralPanel(left, top);
            case "Camera", "Motion" -> buildMotionPanel(left, top);
            case "Water" -> buildWaterPanel(left, top);
            default -> buildGeneralPanel(left, top);
        }

        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> close())
            .dimensions(left, top + 200, 180, 20).build());
    }

    private void buildGeneralPanel(int left, int top) {
        addDrawableChild(ButtonWidget.builder(labelForHurtShake(), button -> {
            LagKillVisualConfig.toggleHurtShake();
            button.setMessage(labelForHurtShake());
        }).dimensions(left, top, 180, 20).build());

        addDrawableChild(ButtonWidget.builder(labelForFireOverlay(), button -> {
            LagKillVisualConfig.toggleFireOverlay();
            button.setMessage(labelForFireOverlay());
        }).dimensions(left, top + 24, 180, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Apply Smooth Preset"), button -> LagKillVisualConfig.applySmoothPreset())
            .dimensions(left, top + 48, 180, 20).build());
    }

    private void buildMotionPanel(int left, int top) {
        addDrawableChild(ButtonWidget.builder(labelForMotionBlur(), button -> {
            LagKillVisualConfig.toggleMotionBlur();
            button.setMessage(labelForMotionBlur());
        }).dimensions(left, top, 180, 20).build());

        addDrawableChild(ButtonWidget.builder(labelForMotionStrength(), button -> {
            LagKillVisualConfig.cycleMotionBlurStrength();
            button.setMessage(labelForMotionStrength());
        }).dimensions(left, top + 24, 180, 20).build());
    }

    private void buildWaterPanel(int left, int top) {
        addDrawableChild(ButtonWidget.builder(labelForWater(), button -> {
            LagKillVisualConfig.toggleWaterClarity();
            button.setMessage(labelForWater());
        }).dimensions(left, top, 180, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Saturation +"), button -> LagKillVisualConfig.increaseSaturation())
            .dimensions(left, top + 24, 88, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Saturation -"), button -> LagKillVisualConfig.decreaseSaturation())
            .dimensions(left + 92, top + 24, 88, 20).build());
    }

    private Text labelForHurtShake() {
        return Text.literal("Disable Hurt Shake: " + onOff(LagKillVisualConfig.get().disableHurtShake()));
    }

    private Text labelForFireOverlay() {
        return Text.literal("Disable Fire Overlay: " + onOff(LagKillVisualConfig.get().disableFireOverlay()));
    }

    private Text labelForMotionBlur() {
        return Text.literal("Motion Blur: " + onOff(LagKillVisualConfig.get().motionBlurEnabled()));
    }

    private Text labelForMotionStrength() {
        return Text.literal(String.format("Motion Strength: %.2f", LagKillVisualConfig.get().motionBlurStrength()));
    }

    private Text labelForWater() {
        return Text.literal("Water Clarity: " + onOff(LagKillVisualConfig.get().waterClarityBoost()));
    }

    private String onOff(boolean value) {
        return value ? "ON" : "OFF";
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawText(this.textRenderer, Text.literal("LagKill Dashboard - " + selectedCategory), 20, 46, 0x7CFC00, true);
        context.drawText(this.textRenderer, Text.literal(String.format("Saturation: %.2f", LagKillVisualConfig.get().saturationBoost())), 220, 60, 0xFFFFFF, false);
    }
}
