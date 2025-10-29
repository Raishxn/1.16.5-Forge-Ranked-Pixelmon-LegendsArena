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
            ModFile.LOGGER.info("Arquivo de configuração não encontrado, a criar um novo com valores padrão...");
            createDefaultConfig();
        }
        loadConfig();
    }

    private static void createDefaultConfig() {
        // Usamos LinkedHashMap para manter a ordem das chaves igual ao seu exemplo
        Map<String, Object> root = new LinkedHashMap<>();

        root.put("version", 1);

        // General
        Map<String, Object> general = new LinkedHashMap<>();
        general.put("plugin-enabled", true);
        general.put("locale", "pt_BR");
        general.put("baseline-elo", 1200);
        root.put("general", general);

        // Database
        Map<String, Object> database = new LinkedHashMap<>();
        database.put("type", "sqlite");
        Map<String, Object> mysql = new LinkedHashMap<>();
        mysql.put("host", "127.0.0.1");
        mysql.put("port", 3306);
        mysql.put("database", "legendsarena");
        mysql.put("user", "legends_user");
        mysql.put("password", "sua_senha_aqui");
        Map<String, Object> pool = new LinkedHashMap<>();
        pool.put("max-connections", 10);
        pool.put("min-connections", 1);
        pool.put("connection-timeout-seconds", 30);
        mysql.put("pool", pool);
        database.put("mysql", mysql);
        Map<String, Object> sqlite = new LinkedHashMap<>();
        sqlite.put("path", "./config/legendsarena/legendsarena.db");
        database.put("sqlite", sqlite);
        root.put("database", database);

        // Ranked Tiers
        Map<String, Object> rankedTiers = new LinkedHashMap<>();
        String[] tiers = {"Bronze", "Prata", "Ouro", "Lendario"};
        int[] levelCaps = {30, 50, 70, 100};
        for (int i = 0; i < tiers.length; i++) {
            Map<String, Object> tier = new LinkedHashMap<>();
            tier.put("active", i < 3); // Bronze, Prata, Ouro ativos por padrão
            tier.put("level-cap", levelCaps[i]);
            tier.put("bans-enabled", true);
            rankedTiers.put(tiers[i], tier);
        }
        root.put("ranked-tiers", rankedTiers);

        // Elo Ranks
        Map<String, Object> eloRanks = new LinkedHashMap<>();
        eloRanks.put("Bronze", new LinkedHashMap<String, Integer>() {{ put("min", 0); put("max", 1199); }});
        eloRanks.put("Prata", new LinkedHashMap<String, Integer>() {{ put("min", 1200); put("max", 1599); }});
        eloRanks.put("Ouro", new LinkedHashMap<String, Integer>() {{ put("min", 1600); put("max", 1999); }});
        eloRanks.put("Lendario", new LinkedHashMap<String, Integer>() {{ put("min", 2000); put("max", 9999); }});
        root.put("elo-ranks", eloRanks);

        // Season Settings
        Map<String, Object> seasonSettings = new LinkedHashMap<>();
        seasonSettings.put("current-season", 1);
        seasonSettings.put("start-date", "2025-01-01");
        seasonSettings.put("end-date", "2025-03-31");
        seasonSettings.put("elo-reset-factor", 0.30);
        seasonSettings.put("auto-apply-reset", true);
        root.put("season-settings", seasonSettings);

        // Matchmaking Settings
        Map<String, Object> matchmaking = new LinkedHashMap<>();
        matchmaking.put("allow-multiple-queues", false);
        matchmaking.put("max-rank-difference", 1);
        Map<String, Object> winstreak = new LinkedHashMap<>();
        winstreak.put("enabled", true);
        winstreak.put("min-streak-for-bonus", 3);
        winstreak.put("bonus-elo-per-win", 2);
        winstreak.put("bonus-cap", 10);
        matchmaking.put("winstreak-bonus", winstreak);
        matchmaking.put("max-elo-gain", 50);
        matchmaking.put("max-elo-loss", 50);
        matchmaking.put("min-elo", 100);
        Map<String, Object> provisional = new LinkedHashMap<>();
        provisional.put("enabled", true);
        provisional.put("matches", 10);
        provisional.put("k_provisional", 64);
        matchmaking.put("provisional", provisional);
        Map<String, Integer> kByTier = new LinkedHashMap<>();
        kByTier.put("Bronze", 40);
        kByTier.put("Prata", 32);
        kByTier.put("Ouro", 24);
        kByTier.put("Lendario", 16);
        matchmaking.put("k_by_tier", kByTier);
        Map<String, Object> kDynamic = new LinkedHashMap<>();
        kDynamic.put("enabled", true);
        kDynamic.put("threshold_matches", 30);
        kDynamic.put("multiplier_before_threshold", 1.25);
        matchmaking.put("k_dynamic", kDynamic);
        Map<String, Object> antiSmurf = new LinkedHashMap<>();
        antiSmurf.put("enabled", true);
        antiSmurf.put("min_matches_for_full_gain", 10);
        antiSmurf.put("tier_diff_block", 2);
        antiSmurf.put("tier_diff_gain_multiplier", 0.5);
        matchmaking.put("anti_smurf", antiSmurf);
        root.put("matchmaking-settings", matchmaking);

        // Logging
        Map<String, Object> logging = new LinkedHashMap<>();
        logging.put("enable-match-logging", true);
        logging.put("match-history-retention-days", 365);
        root.put("logging", logging);

        // Admin
        Map<String, Object> admin = new LinkedHashMap<>();
        admin.put("permission-prefix", "legendsarena");
        Map<String, Object> maintenance = new LinkedHashMap<>();
        maintenance.put("enabled", true);
        maintenance.put("run-time", "03:00");
        admin.put("daily-maintenance", maintenance);
        root.put("admin", admin);

        // UI
        Map<String, Object> ui = new LinkedHashMap<>();
        ui.put("show-rank-on-tab", true);
        ui.put("show-elo-in-status", true);
        root.put("ui", ui);

        // Dev
        Map<String, Object> dev = new LinkedHashMap<>();
        dev.put("debug-mode", false);
        dev.put("verbose-logging", false);
        root.put("dev", dev);

        // Salva o arquivo
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);
        options.setIndicatorIndent(2);
        options.setWidth(120);

        Yaml yaml = new Yaml(options);

        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("# LegendsArena - config.yml\n");
            writer.write("# Ficheiro de configuração principal do LegendsArena.\n\n");
            yaml.dump(root, writer);
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
            configData = new LinkedHashMap<>();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String path) {
        if (configData == null) return null;
        String[] keys = path.split("\\.");
        Object current = configData;
        for (String key : keys) {
            if (!(current instanceof Map)) {
                return null;
            }
            current = ((Map<String, Object>) current).get(key);
            if (current == null) {
                return null;
            }
        }
        return (T) current;
    }
}