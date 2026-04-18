package com.rename_mod.lagkill.client;

import net.fabricmc.loader.api.FabricLoader;

final class RendererAdvisor {
    enum BackendMode {
        DEFAULT,
        VULKAN_MOD
    }

    BackendMode detect() {
        if (FabricLoader.getInstance().isModLoaded("vulkanmod")) {
            return BackendMode.VULKAN_MOD;
        }
        return BackendMode.DEFAULT;
    }
}
