package com.raishxn.legendsarena;

import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod("legendsarena")
public class ModFile {

    public static final String MOD_ID = "legendsarena";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static ModFile instance;

    private final ForgeCommandFactory commandFactory = new ForgeCommandFactory();





    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

    }

    public static ModFile getInstance() {
        return instance;
    }
    }