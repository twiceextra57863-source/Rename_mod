package com.rename_mod.lagkill.client;

import net.minecraft.client.MinecraftClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class MultiplayerAdvisor {
    boolean isMultiplayerSession(MinecraftClient client) {
        return currentServerEntry(client) != null;
    }

    String currentServerHost(MinecraftClient client) {
        Object entry = currentServerEntry(client);
        if (entry == null) {
            return null;
        }

        try {
            Method addressMethod = entry.getClass().getMethod("address");
            Object address = addressMethod.invoke(entry);
            return address == null ? null : address.toString();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            return null;
        }
    }

    private Object currentServerEntry(MinecraftClient client) {
        if (client == null) {
            return null;
        }

        try {
            Method method = client.getClass().getMethod("getCurrentServerEntry");
            return method.invoke(client);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            return null;
        }
    }
}
