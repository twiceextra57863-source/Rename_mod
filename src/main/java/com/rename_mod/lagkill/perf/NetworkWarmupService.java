package com.rename_mod.lagkill.perf;

import com.rename_mod.lagkill.LagKillMod;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class NetworkWarmupService {
    public void prewarm(boolean enabled, String serverHost) {
        if (!enabled) {
            return;
        }

        CompletableFuture.runAsync(() -> {
            List<String> hosts = List.of(
                "api.minecraftservices.com",
                "sessionserver.mojang.com",
                "textures.minecraft.net"
            );

            for (String host : hosts) {
                resolve(host);
            }

            if (serverHost != null && !serverHost.isBlank()) {
                resolve(serverHost);
            }
        });
    }

    private void resolve(String host) {
        try {
            InetAddress.getByName(host);
        } catch (Exception exception) {
            LagKillMod.LOGGER.debug("LagKill DNS warmup skipped for {}: {}", host, exception.getMessage());
        }
    }
}
