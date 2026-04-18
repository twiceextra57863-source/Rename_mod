package com.rename_mod.lagkill.perf;

public final class StartupFastPathController {
    private final int startupTicks;

    public StartupFastPathController(int startupTicks) {
        this.startupTicks = Math.max(120, startupTicks);
    }

    public boolean isActive(int ticksSinceClientStart) {
        return ticksSinceClientStart <= startupTicks;
    }
}
