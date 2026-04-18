package com.rename_mod.lagkill.mixin.client;

import com.rename_mod.lagkill.config.LagKillVisualConfig;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void lagkill$disableHurtTilt(CallbackInfo ci) {
        if (LagKillVisualConfig.get().disableHurtShake()) {
            ci.cancel();
        }
    }
}
