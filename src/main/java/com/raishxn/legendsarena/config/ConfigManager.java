package com.raishxn.legendsarena.config;

import com.raishxn.legendsarena.legendsarena;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*; // Importação necessária
import java.nio.file.Files; // Importação necessária
import java.util.HashMap; // Importação necessária
import java.util.Map; // Importação necessária

public class ConfigManager {

    // VARIÁVEIS QUE FALTARAM:
    private final File mainDir = new File("plugins/Ranked");
    private final Map<String, File> configFiles = new HashMap<>();

    // --- Resto das variáveis que já tínhamos ---
    private Yaml yamlElos; // Vamos usar instâncias Yaml separadas para cada POJO
    private Yaml yamlCore;

    // Nossos POJOs de config carregados
    private ElosConfig elosConfig;
    private CoreConfig coreConfig; // ADICIONADO
    // ...

    public ConfigManager() {
        // ... (o resto do seu construtor)
    }

    public void loadConfigs() {
        // ... (o resto do seu método loadConfigs)
        // ...

        // AGORA ISSO DEVE FUNCIONAR:
        try (InputStream in = new FileInputStream(configFiles.get("elos.yml"))) {
            // ...
        } catch (Exception e) {
            // ...
        }

        // E ISSO TAMBÉM:
        try (InputStream in = new FileInputStream(configFiles.get("config.yml"))) {
            // ...
        } catch (Exception e) {
            // ...
        }
    }

    // Método copyDefaultFile (COMO ESTAVA ANTES)
    private void copyDefaultFile(String resourcePath, File destination) {
        try (InputStream in = legendsarena.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                legendsarena.LOGGER.warn("Recurso não encontrado: " + resourcePath);
                // Se o recurso não for encontrado, apenas cria um arquivo vazio
                destination.createNewFile();
                return;
            }
            // Garante que a pasta pai exista
            destination.getParentFile().mkdirs();
            Files.copy(in, destination.toPath());
        } catch (IOException e) {
            legendsarena.LOGGER.error("Falha ao copiar config padrão: " + resourcePath, e);
        }
    }

    // ... (Getters)
    public ElosConfig getElosConfig() {
        return this.elosConfig;
    }

    public CoreConfig getCoreConfig() {
        return this.coreConfig;
    }
}