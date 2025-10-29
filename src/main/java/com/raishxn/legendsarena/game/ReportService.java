package com.raishxn.legendsarena.game;

import com.raishxn.legendsarena.ModFile;
import com.raishxn.legendsarena.config.ConfigManager;
import com.raishxn.legendsarena.database.PlayerStats;
import com.raishxn.legendsarena.database.PlayerStatsService;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportService {

    public static boolean generateTop10Report(String tier) {
        int season = ConfigManager.get("season-settings.current-season");
        List<PlayerStats> top10Players = PlayerStatsService.getTop10Players(tier);

        if (top10Players.isEmpty()) {
            ModFile.LOGGER.info("Nenhum jogador para gerar relatório na tier " + tier);
            return false;
        }

        Map<String, Object> reportData = new LinkedHashMap<>();
        reportData.put("season", season);
        reportData.put("tier", tier);
        reportData.put("generated_at", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new Date()));

        List<Map<String, Object>> playersList = new ArrayList<>();
        int rank = 1;
        for (PlayerStats stats : top10Players) {
            Map<String, Object> playerData = new LinkedHashMap<>();
            playerData.put("rank", rank++);
            playerData.put("nickname", stats.getPlayerName());
            playerData.put("elo", stats.getElo());
            playerData.put("wins", stats.getWins());
            playerData.put("losses", stats.getLosses());
            playerData.put("best_winstreak", stats.getBestWinstreak());
            playersList.add(playerData);
        }
        reportData.put("top_10_players", playersList);

        try {
            File historyDir = new File("config/legendsarena/history");
            if (!historyDir.exists()) {
                historyDir.mkdirs();
            }
            File reportFile = new File(historyDir, "season_" + season + "_" + tier + "_top10.yml");

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);

            Yaml yaml = new Yaml(options);
            try (FileWriter writer = new FileWriter(reportFile)) {
                yaml.dump(reportData, writer);
            }
            return true;
        } catch (IOException e) {
            ModFile.LOGGER.error("Falha ao gerar o relatório de final de temporada.", e);
            return false;
        }
    }
}