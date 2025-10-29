package com.raishxn.legendsarena.config;

import com.raishxn.legendsarena.ModFile;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigService {

    public static boolean setTierActive(String tierName, boolean isActive) {
        File configFile = new File("config/legendsarena/config.yml");
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);

        try (InputStream inputStream = new FileInputStream(configFile)) {
            // Usamos LinkedHashMap para tentar preservar a ordem das chaves
            Map<String, Object> data = yaml.load(inputStream);

            if (data.containsKey("ranked-tiers")) {
                Map<String, Object> rankedTiers = (Map<String, Object>) data.get("ranked-tiers");
                String tierFormatted = tierName.substring(0, 1).toUpperCase() + tierName.substring(1).toLowerCase();

                if (rankedTiers.containsKey(tierFormatted)) {
                    Map<String, Object> tierData = (Map<String, Object>) rankedTiers.get(tierFormatted);
                    tierData.put("active", isActive);
                } else {
                    ModFile.LOGGER.warn("Tentativa de alterar a tier '" + tierFormatted + "' que não existe no config.yml.");
                    return false;
                }
            }

            // Escreve o arquivo inteiro de volta com a modificação
            try (FileWriter writer = new FileWriter(configFile)) {
                yaml.dump(data, writer);
            }

            // Recarrega a configuração na memória para refletir a mudança
            ConfigManager.loadConfig();
            return true;

        } catch (IOException | ClassCastException e) {
            ModFile.LOGGER.error("Falha ao modificar o arquivo config.yml.", e);
            return false;
        }
    }
}