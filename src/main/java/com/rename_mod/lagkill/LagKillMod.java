package com.rename_mod.lagkill;

import com.rename_mod.lagkill.config.LagKillConfig;
import com.rename_mod.lagkill.config.LagKillVisualConfig;
import com.rename_mod.lagkill.nativebridge.NativeBridge;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LagKillMod implements ModInitializer {
    public static final String MOD_ID = "lagkill";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LagKillConfig.bootstrap();
        LagKillVisualConfig.bootstrap();
        LOGGER.info("LagKill initialized. Profile={}, RecordingMode={}, RendererPreference={}, MultiplayerBoost={} target={}fps, VisualIntegrity={}, StartupFastPath={}, SafeMode={}",
            LagKillConfig.get().profile(),
            LagKillConfig.get().recordingModeEnabled(),
            LagKillConfig.get().rendererPreference(),
            LagKillConfig.get().multiplayerBoostEnabled(),
            LagKillConfig.get().multiplayerTargetFps(),
            LagKillConfig.get().visualIntegrityMode(),
            LagKillConfig.get().startupFastPathEnabled(),
            LagKillConfig.get().aggressiveSafeModeEnabled());
        LOGGER.info("LagKill native bridge loaded={}", NativeBridge.isLoaded());
    }
}
