package com.raishxn.legendsarena;

import com.raishxn.legendsarena.config.ConfigManager;
import com.raishxn.legendsarena.db.DatabaseManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// O Mod ID deve ser o mesmo do seu mods.toml
@Mod("legendsarena") // Confirme se "legendsarena" é seu Mod ID
public class legendsarena {

    public static final String MOD_ID = "legendsarena"; // Confirme seu Mod ID
    public static final Logger LOGGER = LogManager.getLogger();

    private static ConfigManager configManager;
    private static DatabaseManager databaseManager; // ADICIONADO

    public legendsarena() {
        MinecraftForge.EVENT_BUS.register(this);
        // ADICIONAR O LISTENER para o evento de parada
        MinecraftForge.EVENT_BUS.addListener(this::onServerStopping);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("Iniciando carregamento das configs de LegendsArena...");
        try {
            configManager = new ConfigManager();
            configManager.loadConfigs();
            LOGGER.info("Configs de LegendsArena carregadas com sucesso.");
        } catch (Exception e) {
            LOGGER.error("!!! FALHA CRÍTICA AO CARREGAR CONFIGS DO LEGENDSARENA !!!", e);
            return; // Para a inicialização se a config falhar
        }

        // INICIALIZAÇÃO DO BANCO DE DADOS (NOVO)
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

    // Getters Estáticos
    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}