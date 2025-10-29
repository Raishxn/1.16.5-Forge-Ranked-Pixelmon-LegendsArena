package com.raishxn.legendsarena.game;

import com.raishxn.legendsarena.ModFile;
import com.raishxn.legendsarena.config.ConfigManager;
import com.raishxn.legendsarena.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeasonService {

    public static void endSeasonAndResetElo(String tier) {
        int currentSeason = ConfigManager.get("season-settings.current-season");
        double resetFactor = (double) ConfigManager.get("season-settings.elo-reset-factor");
        int baselineElo = ConfigManager.get("general.baseline-elo");

        String selectSql = "SELECT player_uuid, elo FROM player_stats WHERE tier = ? AND season = ?";
        String updateSql = "UPDATE player_stats SET elo = ? WHERE player_uuid = ? AND tier = ? AND season = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            selectStmt.setString(1, tier);
            selectStmt.setInt(2, currentSeason);

            ResultSet rs = selectStmt.executeQuery();
            conn.setAutoCommit(false); // Inicia transação para performance

            while (rs.next()) {
                String uuid = rs.getString("player_uuid");
                int oldElo = rs.getInt("elo");

                // Fórmula: R_new = round(R_old * (1 - f) + baseline * f)
                int newElo = (int) Math.round(oldElo * (1.0 - resetFactor) + baselineElo * resetFactor);

                updateStmt.setInt(1, newElo);
                updateStmt.setString(2, uuid);
                updateStmt.setString(3, tier);
                updateStmt.setInt(4, currentSeason);
                updateStmt.addBatch(); // Adiciona a atualização em um lote
            }

            updateStmt.executeBatch(); // Executa todas as atualizações de uma vez
            conn.commit(); // Confirma a transação
            conn.setAutoCommit(true);

            ModFile.LOGGER.info("Reset de ELO para a tier " + tier + " concluído com sucesso.");

        } catch (SQLException e) {
            ModFile.LOGGER.error("Falha ao executar o reset de ELO da temporada.", e);
        }
    }
}