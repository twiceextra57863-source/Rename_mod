package com.rename_mod.lagkill.mixin.client;

import com.rename_mod.lagkill.config.LagKillVisualConfig;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void lagkill$disableFireOverlay(CallbackInfo ci) {
        if (LagKillVisualConfig.get().disableFireOverlay()) {
            ci.cancel();
        }
    }
}
