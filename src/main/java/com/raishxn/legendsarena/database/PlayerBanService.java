package com.raishxn.legendsarena.database;

import com.raishxn.legendsarena.ModFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerBanService {

    public static void banPlayer(UUID playerUuid, String reason, UUID bannerUuid, long duration) {
        long bannedAt = System.currentTimeMillis();
        long expiresAt = bannedAt + duration;
        String sql = "INSERT INTO player_bans(player_uuid, reason, banned_by_uuid, banned_at, expires_at) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = ModFile.getInstance().getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerUuid.toString());
            pstmt.setString(2, reason);
            pstmt.setString(3, bannerUuid.toString());
            pstmt.setLong(4, bannedAt);
            pstmt.setLong(5, expiresAt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            ModFile.LOGGER.error("Erro ao banir jogador da base de dados.", e);
        }
    }

    public static void unbanPlayer(UUID playerUuid) {
        String sql = "DELETE FROM player_bans WHERE player_uuid = ?";

        try (Connection conn = ModFile.getInstance().getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerUuid.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            ModFile.LOGGER.error("Erro ao desbanir jogador da base de dados.", e);
        }
    }

    public static boolean isPlayerBanned(UUID playerUuid) {
        String sql = "SELECT expires_at FROM player_bans WHERE player_uuid = ? AND expires_at > ?";

        try (Connection conn = ModFile.getInstance().getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerUuid.toString());
            pstmt.setLong(2, System.currentTimeMillis());
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            ModFile.LOGGER.error("Erro ao verificar se o jogador está banido.", e);
        }
        return false;
    }
}