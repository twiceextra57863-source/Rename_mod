package com.rename_mod.lagkill;

import com.rename_mod.lagkill.config.LagKillConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LagKillMod implements ModInitializer {
    public static final String MOD_ID = "lagkill";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LagKillConfig.bootstrap();
        LOGGER.info("LagKill initialized. Profile={}, RecordingMode={}",
            LagKillConfig.get().profile(),
            LagKillConfig.get().recordingModeEnabled());
    }
}
