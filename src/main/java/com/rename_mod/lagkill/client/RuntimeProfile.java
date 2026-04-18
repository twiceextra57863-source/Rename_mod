package com.rename_mod.lagkill.client;

record RuntimeProfile(int renderDistance, int simulationDistance, int maxFps, double entityDistanceScale) {
    static RuntimeProfile competitive() {
        return new RuntimeProfile(8, 6, 120, 0.9);
    }

    static RuntimeProfile balanced() {
        return new RuntimeProfile(12, 8, 165, 1.0);
    }

    static RuntimeProfile quality() {
        return new RuntimeProfile(16, 10, 240, 1.25);
    }

    static RuntimeProfile recordingQuality(int recordingFpsCap) {
        return new RuntimeProfile(14, 9, recordingFpsCap, 1.15);
    }

    static RuntimeProfile recordingSafe(int recordingFpsCap) {
        return new RuntimeProfile(11, 7, recordingFpsCap, 1.0);
    }

    static RuntimeProfile emergency() {
        return new RuntimeProfile(6, 4, 90, 0.75);
    }
}
