package com.raishxn.legendsarena;

import com.envyful.api.forge.command.ForgeCommandFactory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("legendsarena")
public class legendsarena {

    public static final String MOD_ID = "legendsarena";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static legendsarena instance;

    private final ForgeCommandFactory commandFactory = new ForgeCommandFactory();





    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

    }

    public static legendsarena getInstance() {
        return instance;
    }
    }