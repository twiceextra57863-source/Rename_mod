package com.rename_mod.lagkill.nativebridge;

import com.rename_mod.lagkill.LagKillMod;

public final class NativeBridge {
    private static boolean loaded;

    static {
        try {
            System.loadLibrary("lagkill_native");
            loaded = true;
        } catch (UnsatisfiedLinkError error) {
            loaded = false;
            LagKillMod.LOGGER.debug("LagKill native library not loaded: {}", error.getMessage());
        }
    }

    private NativeBridge() {
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static int suggestFpsCap(double p95Ms, int desiredCap, boolean multiplayer) {
        if (!loaded) {
            return desiredCap;
        }

        try {
            return nativeSuggestFpsCap(p95Ms, desiredCap, multiplayer);
        } catch (Throwable throwable) {
            LagKillMod.LOGGER.debug("LagKill native call failed: {}", throwable.getMessage());
            return desiredCap;
        }
    }

    private static native int nativeSuggestFpsCap(double p95Ms, int desiredCap, boolean multiplayer);
}
