package com.raishxn.legendsarena.database;

import com.raishxn.legendsarena.ModFile;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerBanService {

    public static void banPlayer(ServerPlayerEntity player, String tier, long durationMillis, String reason) {
        String sql = "INSERT OR REPLACE INTO player_bans (player_uuid, tier, end_timestamp, reason) VALUES (?, ?, ?, ?)";
        long endTime = System.currentTimeMillis() + durationMillis;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, player.getUUID().toString());
            stmt.setString(2, tier);
            stmt.setLong(3, endTime);
            stmt.setString(4, reason);
            stmt.executeUpdate();

        } catch (SQLException e) {
            ModFile.LOGGER.error("Erro ao banir o jogador " + player.getName().getString(), e);
        }
    }

    public static void unbanPlayer(ServerPlayerEntity player, String tier) {
        String sql = "DELETE FROM player_bans WHERE player_uuid = ? AND tier = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, player.getUUID().toString());
            stmt.setString(2, tier);
            stmt.executeUpdate();

        } catch (SQLException e) {
            ModFile.LOGGER.error("Erro ao desbanir o jogador " + player.getName().getString(), e);
        }
    }

    public static boolean isPlayerBanned(ServerPlayerEntity player, String tier) {
        String sql = "SELECT end_timestamp FROM player_bans WHERE player_uuid = ? AND tier = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, player.getUUID().toString());
            stmt.setString(2, tier);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                long endTime = rs.getLong("end_timestamp");
                // Se o tempo de banimento for maior que o tempo atual, ele ainda está banido.
                return endTime > System.currentTimeMillis();
            }
            return false; // Não há registro de banimento.

        } catch (SQLException e) {
            ModFile.LOGGER.error("Erro ao verificar status de banimento do jogador " + player.getName().getString(), e);
            return true; // Previne que o jogador jogue em caso de erro no DB.
        }
    }
}