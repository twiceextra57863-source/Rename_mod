package com.rename_mod.lagkill.perf;

import com.rename_mod.lagkill.LagKillMod;
import net.minecraft.client.MinecraftClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ResourcePackWarmupService {
    public void warmup(MinecraftClient client, boolean enabled) {
        if (!enabled || client == null) {
            return;
        }

        try {
            Method managerGetter = client.getClass().getMethod("getResourcePackManager");
            Object manager = managerGetter.invoke(client);
            if (manager == null) {
                return;
            }

            Method scanPacks = manager.getClass().getMethod("scanPacks");
            scanPacks.invoke(manager);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            LagKillMod.LOGGER.debug("LagKill resource-pack warmup skipped: {}", exception.getMessage());
        }
    }
}
