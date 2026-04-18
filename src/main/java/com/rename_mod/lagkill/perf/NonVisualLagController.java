package com.rename_mod.lagkill.perf;

import com.rename_mod.lagkill.nativebridge.NativeBridge;

public final class NonVisualLagController {
    private int smoothedFpsCap = 240;

    public int computeFpsCap(double p95Ms, int targetCap, boolean multiplayer) {
        int desired = targetCap;

        if (multiplayer) {
            if (p95Ms > 30.0) {
                desired = Math.min(desired, 144);
            } else if (p95Ms > 22.0) {
                desired = Math.min(desired, 165);
            }
        } else {
            if (p95Ms > 35.0) {
                desired = Math.min(desired, 120);
            } else if (p95Ms > 26.0) {
                desired = Math.min(desired, 165);
            }
        }

        int nativeSuggested = NativeBridge.suggestFpsCap(p95Ms, desired, multiplayer);
        smoothedFpsCap = (int) Math.round((smoothedFpsCap * 0.85) + (nativeSuggested * 0.15));
        return Math.max(90, smoothedFpsCap);
    }

    public boolean nativeAccelerationEnabled() {
        return NativeBridge.isLoaded();
    }
}
