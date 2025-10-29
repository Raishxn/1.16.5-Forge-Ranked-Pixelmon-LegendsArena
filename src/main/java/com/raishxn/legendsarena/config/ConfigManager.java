package com.raishxn.legendsarena.config;

import com.raishxn.legendsarena.ModFile;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private static File configFile;
    private static Map<String, Object> configData;

    public static void initialize() {
        File configDir = new File("config/legendsarena");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        configFile = new File(configDir, "config.yml");

        if (!configFile.exists()) {
            ModFile.LOGGER.info("Arquivo de configuração não encontrado, criando um novo...");
            createDefaultConfig();
        }
        loadConfig();
    }

    private static void createDefaultConfig() {
        Map<String, Object> defaultConfig = new HashMap<>();
        // Preencha com os valores padrão, inspirados no seu legendsarena_config_mysql.yml
        defaultConfig.put("general.plugin-enabled", true);
        defaultConfig.put("general.baseline-elo", 1200);
        defaultConfig.put("database.type", "sqlite");
        // Adicione aqui TODOS os outros valores padrão...

        try (FileWriter writer = new FileWriter(configFile)) {
            Yaml yaml = new Yaml();
            yaml.dump(defaultConfig, writer);
        } catch (IOException e) {
            ModFile.LOGGER.error("Não foi possível criar o arquivo de configuração padrão.", e);
        }
    }

    public static void loadConfig() {
        try (InputStream inputStream = new FileInputStream(configFile)) {
            Yaml yaml = new Yaml();
            configData = yaml.load(inputStream);
            ModFile.LOGGER.info("Configuração do LegendsArena carregada com sucesso!");
        } catch (IOException e) {
            ModFile.LOGGER.error("Não foi possível carregar o arquivo de configuração.", e);
            configData = new HashMap<>(); // Garante que não dê erro se o arquivo falhar
        }
    }

    // Método para buscar um valor da config
    public static <T> T get(String path) {
        String[] keys = path.split("\\.");
        Map<String, Object> currentMap = configData;
        for (int i = 0; i < keys.length - 1; i++) {
            currentMap = (Map<String, Object>) currentMap.get(keys[i]);
            if (currentMap == null) {
                return null;
            }
        }
        return (T) currentMap.get(keys[keys.length - 1]);
    }
}