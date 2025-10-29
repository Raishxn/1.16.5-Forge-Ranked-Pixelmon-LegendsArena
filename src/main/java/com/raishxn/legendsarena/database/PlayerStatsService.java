package com.raishxn.legendsarena.database;

import com.raishxn.legendsarena.ModFile;
import com.raishxn.legendsarena.config.ConfigManager;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerStatsService {

    public static PlayerStats getPlayerStats(ServerPlayerEntity player, String tier) {
        UUID playerUuid = player.getUUID();
        int currentSeason = ConfigManager.get("season-settings.current-season");

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM player_stats WHERE player_uuid = ? AND tier = ? AND season = ?")) {

            stmt.setString(1, playerUuid.toString());
            stmt.setString(2, tier);
            stmt.setInt(3, currentSeason);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPlayerStats(rs);
            } else {
                return createNewPlayerStats(player, tier, currentSeason);
            }
        } catch (SQLException e) {
            ModFile.LOGGER.error("Erro ao buscar estatísticas do jogador.", e);
            return null;
        }
    }

    private static PlayerStats createNewPlayerStats(ServerPlayerEntity player, String tier, int season) {
        String sql = "INSERT INTO player_stats(player_uuid, player_name, tier, season, elo) VALUES(?,?,?,?,?)";
        int baselineElo = ConfigManager.get("general.baseline-elo");

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, player.getUUID().toString());
            stmt.setString(2, player.getName().getString());
            stmt.setString(3, tier);
            stmt.setInt(4, season);
            stmt.setInt(5, baselineElo);
            stmt.executeUpdate();

            PlayerStats newStats = new PlayerStats();
            newStats.setPlayerUuid(player.getUUID());
            newStats.setPlayerName(player.getName().getString());
            newStats.setTier(tier);
            newStats.setSeason(season);
            newStats.setElo(baselineElo);
            return newStats;

        } catch (SQLException e) {
            ModFile.LOGGER.error("Erro ao criar novo registro de estatísticas.", e);
            return null;
        }
    }

    public static void updatePlayerStats(PlayerStats stats) {
        String sql = "UPDATE player_stats SET player_name = ?, elo = ?, wins = ?, losses = ?, " +
                "winstreak = ?, matches_played = ?, best_winstreak = ? " +
                "WHERE player_uuid = ? AND tier = ? AND season = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, stats.getPlayerName());
            stmt.setInt(2, stats.getElo());
            stmt.setInt(3, stats.getWins());
            stmt.setInt(4, stats.getLosses());
            stmt.setInt(5, stats.getWinstreak());
            stmt.setInt(6, stats.getMatchesPlayed());
            stmt.setInt(7, stats.getBestWinstreak());
            stmt.setString(8, stats.getPlayerUuid().toString());
            stmt.setString(9, stats.getTier());
            stmt.setInt(10, stats.getSeason());

            stmt.executeUpdate();
        } catch (SQLException e) {
            ModFile.LOGGER.error("Erro ao atualizar estatísticas do jogador.", e);
        }
    }

    public static List<PlayerStats> getTop10Players(String tier) {
        List<PlayerStats> topPlayers = new ArrayList<>();
        int currentSeason = ConfigManager.get("season-settings.current-season");
        String sql = "SELECT * FROM player_stats WHERE tier = ? AND season = ? ORDER BY elo DESC LIMIT 10";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tier);
            stmt.setInt(2, currentSeason);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                topPlayers.add(mapResultSetToPlayerStats(rs));
            }
        } catch (SQLException e) {
            ModFile.LOGGER.error("Erro ao buscar o top 10 do ranking.", e);
        }
        return topPlayers;
    }

    private static PlayerStats mapResultSetToPlayerStats(ResultSet rs) throws SQLException {
        PlayerStats stats = new PlayerStats();
        stats.setPlayerUuid(UUID.fromString(rs.getString("player_uuid")));
        stats.setPlayerName(rs.getString("player_name"));
        stats.setTier(rs.getString("tier"));
        stats.setSeason(rs.getInt("season"));
        stats.setElo(rs.getInt("elo"));
        stats.setWins(rs.getInt("wins"));
        stats.setLosses(rs.getInt("losses"));
        stats.setWinstreak(rs.getInt("winstreak"));
        stats.setMatchesPlayed(rs.getInt("matches_played"));
        stats.setBestWinstreak(rs.getInt("best_winstreak"));
        return stats;
    }
}