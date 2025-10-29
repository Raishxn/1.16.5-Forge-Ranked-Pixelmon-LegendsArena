package com.raishxn.legendsarena.database;

import com.raishxn.legendsarena.ModFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerStatsService {

    public static PlayerStats getPlayerStats(UUID playerUuid, String queueId) {
        int currentSeason = 1; // Placeholder
        String sql = "SELECT * FROM player_stats WHERE player_uuid = ? AND queue_id = ? AND season = ?";

        try (Connection conn = ModFile.getInstance().getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerUuid.toString());
            pstmt.setString(2, queueId);
            pstmt.setInt(3, currentSeason);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // CORREÇÃO: Usa o novo construtor
                return new PlayerStats(
                        playerUuid,
                        rs.getString("player_name"),
                        queueId,
                        rs.getInt("elo"),
                        rs.getInt("wins"),
                        rs.getInt("losses"),
                        rs.getInt("winstreak"),
                        rs.getInt("matches_played"),
                        rs.getInt("season")
                );
            }
        } catch (SQLException e) {
            ModFile.LOGGER.error("Erro ao obter estatísticas do jogador.", e);
        }
        return null;
    }

    public static void createPlayerStats(UUID playerUuid, String playerName, String queueId) {
        // CORREÇÃO: Acesso correto à config
        int baselineElo = ModFile.getInstance().getConfig().getGeneral().getBaselineElo();
        int currentSeason = 1; // Placeholder
        String sql = "INSERT INTO player_stats(player_uuid, player_name, queue_id, elo, wins, losses, winstreak, matches_played, season) VALUES(?, ?, ?, ?, 0, 0, 0, 0, ?)";

        try (Connection conn = ModFile.getInstance().getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerUuid.toString());
            pstmt.setString(2, playerName);
            pstmt.setString(3, queueId);
            pstmt.setInt(4, baselineElo);
            pstmt.setInt(5, currentSeason);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            ModFile.LOGGER.error("Erro ao criar estatísticas para o jogador.", e);
        }
    }

    public static void updatePlayerStats(PlayerStats stats) {
        String sql = "UPDATE player_stats SET elo = ?, wins = ?, losses = ?, winstreak = ?, matches_played = ? WHERE player_uuid = ? AND queue_id = ? AND season = ?";

        try (Connection conn = ModFile.getInstance().getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stats.getElo());
            pstmt.setInt(2, stats.getWins());
            pstmt.setInt(3, stats.getLosses());
            pstmt.setInt(4, stats.getWinstreak());
            pstmt.setInt(5, stats.getMatchesPlayed());
            pstmt.setString(6, stats.getPlayerUuid().toString());
            pstmt.setString(7, stats.getQueueId()); // CORREÇÃO: Usa o novo getter
            pstmt.setInt(8, stats.getSeason());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            ModFile.LOGGER.error("Erro ao atualizar estatísticas do jogador.", e);
        }
    }
}