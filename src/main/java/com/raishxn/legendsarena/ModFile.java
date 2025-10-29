package com.raishxn.legendsarena;

import com.raishxn.legendsarena.command.RankedAdminCommand;
import com.raishxn.legendsarena.database.DatabaseManager;
import com.raishxn.legendsarena.game.MatchmakingManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.raishxn.legendsarena.config.ConfigManager;
import net.minecraftforge.event.RegisterCommandsEvent; // Importe este
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.raishxn.legendsarena.command.RankedCommand;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod(ModFile.MOD_ID)
public class ModFile {

    public static final String MOD_ID = "legendsarena";
    public static final Logger LOGGER = LogManager.getLogger();

    public ModFile() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("LegendsArena: Carregando sistema de configuração.");
        ConfigManager.initialize();

        LOGGER.info("LegendsArena: Conectando ao banco de dados...");
        DatabaseManager.connect();

        MatchmakingManager.initialize();
    }

    // Evento para registrar comandos quando o servidor iniciar
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("LegendsArena: Servidor iniciando, registrando comandos.");
        // Aqui registraremos nossos comandos
    }
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        LOGGER.info("LegendsArena: Registrando comandos.");
        RankedCommand.register(event.getDispatcher());
        RankedAdminCommand.register(event.getDispatcher()); // Adicione esta linha    }
}
}