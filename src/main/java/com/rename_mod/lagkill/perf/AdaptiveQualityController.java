package com.rename_mod.lagkill.perf;

import com.rename_mod.lagkill.config.LagKillConfig;

public final class AdaptiveQualityController {
    private int overloadTicks;

    public void tick(FrameTimeMonitor monitor) {
        if (!LagKillConfig.get().adaptiveEnabled()) {
            overloadTicks = 0;
            return;
        }

        double ms = monitor.smoothedMs();
        if (ms > 34.0) {
            overloadTicks += 3;
        } else if (ms > 28.0) {
            overloadTicks += 1;
        } else if (overloadTicks > 0) {
            overloadTicks--;
        }

        overloadTicks = Math.min(overloadTicks, 300);
    }

    public int overloadTicks() {
        return overloadTicks;
    }

    public boolean emergencyMode() {
        return overloadTicks >= 110;
    }

    public boolean recoveryMode() {
        return overloadTicks >= 40;
    }
}
