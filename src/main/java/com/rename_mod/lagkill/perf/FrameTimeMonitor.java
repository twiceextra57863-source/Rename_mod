package com.rename_mod.lagkill.perf;

import java.util.Arrays;

public final class FrameTimeMonitor {
    private final double[] window = new double[120];
    private int cursor;
    private long lastNs = System.nanoTime();
    private double smoothedMs = 16.0;

    public void onFrame() {
        long now = System.nanoTime();
        double frameMs = Math.max(1.0, (now - lastNs) / 1_000_000.0);
        lastNs = now;

        smoothedMs = (smoothedMs * 0.90) + (frameMs * 0.10);
        window[cursor] = frameMs;
        cursor = (cursor + 1) % window.length;
    }

    public double smoothedMs() {
        return smoothedMs;
    }

    public double p95Ms() {
        double[] copy = Arrays.copyOf(window, window.length);
        Arrays.sort(copy);
        return copy[(int) (copy.length * 0.95)];
    }
}
