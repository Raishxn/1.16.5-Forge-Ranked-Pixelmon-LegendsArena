package com.raishxn.legendsarena;

import com.raishxn.legendsarena.config.*; // Importa todas as novas classes POJO
import com.raishxn.legendsarena.db.DatabaseManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.loading.FMLPaths; // Importação necessária
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path; // Importação necessária

@Mod("legendsarena")
public class legendsarena {

    public static final String MOD_ID = "legendsarena";
    public static final Logger LOGGER = LogManager.getLogger();

    private static ConfigManager configManager;
    private static DatabaseManager databaseManager;

    // Caminho para o diretório de configuração específico do seu mod
    public static Path CONFIG_DIRECTORY;

    public legendsarena() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStopping);

        // Define o caminho do diretório de config (ex: ./config/legendsarena/)
        CONFIG_DIRECTORY = FMLPaths.CONFIGDIR.get().resolve(MOD_ID);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("Iniciando carregamento das configs de LegendsArena...");
        try {
            // Passa o diretório de config para o manager
            configManager = new ConfigManager(CONFIG_DIRECTORY); // MODIFICADO
            configManager.loadConfigs();
            LOGGER.info("Configs de LegendsArena carregadas com sucesso.");
        } catch (Exception e) {
            LOGGER.error("!!! FALHA CRÍTICA AO CARREGAR CONFIGS DO LEGENDSARENA !!!", e);
            return; // Para a inicialização se a config falhar
        }

        // INICIALIZAÇÃO DO BANCO DE DADOS (Funciona como antes)
        try {
            if (configManager.getCoreConfig() == null) {
                LOGGER.error("!!! config.yml não foi carregado. A conexão com o banco de dados foi abortada. !!!");
                return;
            }

            LOGGER.info("Iniciando conexão com o banco de dados...");
            databaseManager = new DatabaseManager(configManager.getCoreConfig());
            databaseManager.connect();
            databaseManager.createTables(); // Cria as tabelas na inicialização

        } catch (Exception e) {
            LOGGER.error("!!! FALHA CRÍTICA AO CONECTAR AO BANCO DE DADOS !!!", e);
        }
    }

    // Método para fechar a conexão quando o servidor parar
    @SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent event) {
        if (databaseManager != null) {
            LOGGER.info("Fechando pool de conexões com o banco de dados...");
            databaseManager.disconnect();
        }
    }

    // --- Getters Estáticos ---
    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    // --- NOVOS GETTERS ESTÁTICOS ---
    // (Para acessar as configs de qualquer lugar do seu código)

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

    // ... Adicione getters para as outras configs (rewards, commands, etc.)
}