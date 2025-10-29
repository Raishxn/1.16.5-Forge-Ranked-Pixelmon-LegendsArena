package com.raishxn.legendsarena;

import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.config.yaml.YamlConfigFactory;
import com.raishxn.legendsarena.command.RankedAdminCommand;
import com.raishxn.legendsarena.command.RankedCommand;
import com.raishxn.legendsarena.config.ConfigManager;
import com.raishxn.legendsarena.config.LegendsArenaConfig;
import com.raishxn.legendsarena.database.DatabaseManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod("legendsarena")
public class ModFile {

    private static final Logger LOGGER = LogManager.getLogger();
    private static ModFile instance;

    private LegendsArenaConfig config;
    private DatabaseManager databaseManager;
    private ConfigManager configManager;
    private final ForgeCommandFactory commandFactory = new ForgeCommandFactory();

    public ModFile() {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
        this.loadConfig();
        this.configManager = new ConfigManager(this);
        this.databaseManager = new DatabaseManager(this.configManager);
    }

    private void loadConfig() {
        try {
            // Corrigido para getInstance
            this.config = YamlConfigFactory.getInstance(LegendsArenaConfig.class);
        } catch (IOException e) {
            LOGGER.error("Erro ao carregar o arquivo de configuração.", e);
        }
    }

    @SubscribeEvent
    public void onServerStarting(RegisterCommandsEvent event) {
        // A API da Envyful espera o PlatformCommand, então precisamos analisar o objeto.
        this.commandFactory.registerCommand(event.getDispatcher(), this.commandFactory.parseCommand(new RankedCommand()));
        this.commandFactory.registerCommand(event.getDispatcher(), this.commandFactory.parseCommand(new RankedAdminCommand()));
    }

    public static ModFile getInstance() {
        return instance;
    }

    public LegendsArenaConfig getConfig() {
        return this.config;
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }
}