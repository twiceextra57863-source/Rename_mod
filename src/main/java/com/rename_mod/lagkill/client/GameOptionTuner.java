package com.rename_mod.lagkill.client;

import com.rename_mod.lagkill.LagKillMod;
import net.minecraft.client.MinecraftClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class GameOptionTuner {
    void applyProfile(MinecraftClient client, RuntimeProfile profile) {
        if (client == null || client.options == null) {
            return;
        }

        applyOption(client.options, "getViewDistance", Integer.valueOf(profile.renderDistance()));
        applyOption(client.options, "getSimulationDistance", Integer.valueOf(profile.simulationDistance()));
        applyOption(client.options, "getMaxFps", Integer.valueOf(profile.maxFps()));
        applyOption(client.options, "getEntityDistanceScaling", Double.valueOf(profile.entityDistanceScale()));
    }

    private void applyOption(Object options, String getterName, Object value) {
        try {
            Method getter = options.getClass().getMethod(getterName);
            Object option = getter.invoke(options);
            if (option == null) {
                return;
            }

            Method setValue = option.getClass().getMethod("setValue", Object.class);
            setValue.invoke(option, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            LagKillMod.LOGGER.debug("LagKill option tuning skipped for {}: {}", getterName, exception.getMessage());
        }
    }
}
