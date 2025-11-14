package com.raishxn.legendsarena;

import com.raishxn.legendsarena.commands.RankedAdminCommands;
import com.raishxn.legendsarena.commands.RankedCommands; // Importar comandos
import com.raishxn.legendsarena.config.*;
import com.raishxn.legendsarena.db.DatabaseManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent; // <-- NOVO IMPORT
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

@Mod("legendsarena")
public class legendsarena {

    public static final String MOD_ID = "legendsarena";
    public static final Logger LOGGER = LogManager.getLogger();

    private static ConfigManager configManager;
    private static DatabaseManager databaseManager;
    public static Path CONFIG_DIRECTORY;

    public legendsarena() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStopping);
        // NOVO: Adiciona o listener para o evento de registrar comandos
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);

        CONFIG_DIRECTORY = FMLPaths.CONFIGDIR.get().resolve(MOD_ID);
    }

    // --- NOVO EVENTO PARA REGISTRAR COMANDOS ---
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        LOGGER.info("Registrando comandos do LegendsArena...");
        // CORREÇÃO: É aqui que os comandos são registrados
        RankedCommands.register(event.getDispatcher());
        RankedAdminCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("Iniciando carregamento das configs de LegendsArena...");
        try {
            configManager = new ConfigManager(CONFIG_DIRECTORY);
            configManager.loadConfigs();
            LOGGER.info("Configs de LegendsArena carregadas com sucesso.");
        } catch (Exception e) {
            LOGGER.error("!!! FALHA CRÍTICA AO CARREGAR CONFIGS DO LEGENDSARENA !!!", e);
            return;
        }

        // --- LINHA DE REGISTRO REMOVIDA DAQUI ---

        // INICIALIZAÇÃO DO BANCO DE DADOS
        try {
            if (configManager.getCoreConfig() == null) {
                LOGGER.error("!!! config.yml não foi carregado. A conexão com o banco de dados foi abortada. !!!");
                return;
            }
            if (configManager.getCoreConfig().getDatabase() == null) {
                LOGGER.error("!!! A seção 'database' dentro do config.yml não foi carregada. Verifique o POJO CoreConfig.java. Conexão abortada. !!!");
                return;
            }

            LOGGER.info("Iniciando conexão com o banco de dados...");
            databaseManager = new DatabaseManager(configManager.getCoreConfig());
            databaseManager.connect();
            databaseManager.createTables();

        } catch (Exception e) {
            LOGGER.error("!!! FALHA CRÍTICA AO CONECTAR AO BANCO DE DADOS !!!", e);
        }
    }

    @SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent event) {
        if (databaseManager != null) {
            LOGGER.info("Fechando pool de conexões com o banco de dados...");
            databaseManager.disconnect();
        }
    }

    // --- Getters Estáticos ---
    // (O resto da sua classe com todos os getters estáticos)
    public static ConfigManager getConfigManager() {
        return configManager;
    }
    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    public static CoreConfig getCoreConfig() {
        return configManager != null ? configManager.getCoreConfig() : null;
    }
    public static ElosConfig getElosConfig() {
        return configManager != null ? configManager.getElosConfig() : null;
    }
    public static TiersConfig getTiersConfig() {
        return configManager != null ? configManager.getTiersConfig() : null;
    }
    public static MessagesConfig getMessagesConfig() {
        return configManager != null ? configManager.getMessagesConfig() : null;
    }
    public static SeasonsConfig getSeasonsConfig() {
        return configManager != null ? configManager.getSeasonsConfig() : null;
    }
    public static CommandsConfig getCommandsConfig() {
        return configManager != null ? configManager.getCommandsConfig() : null;
    }
    public static PermissionsConfig getPermissionsConfig() {
        return configManager != null ? configManager.getPermissionsConfig() : null;
    }
    public static RewardsConfig getRewardsConfig() {
        return configManager != null ? configManager.getRewardsConfig() : null;
    }
    public static StatsConfig getStatsConfig() {
        return configManager != null ? configManager.getStatsConfig() : null;
    }
    public static AntiSmurfConfig getAntiSmurfConfig() {
        return configManager != null ? configManager.getAntiSmurfConfig() : null;
    }
    public static BattleRulesConfig getBattleRulesConfig() {
        return configManager != null ? configManager.getBattleRulesConfig() : null;
    }
}