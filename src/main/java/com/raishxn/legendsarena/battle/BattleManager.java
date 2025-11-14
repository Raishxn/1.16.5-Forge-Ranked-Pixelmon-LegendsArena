package com.raishxn.legendsarena.battle;

import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Rastreia quais jogadores estão atualmente em uma batalha ranqueada.
 */
public class BattleManager {

    // Um conjunto (Set) de UUIDs de jogadores que estão em uma batalha ranqueada.
    private static final Set<UUID> rankedBattlePlayers = Collections.synchronizedSet(new HashSet<>());

    /**
     * Registra que dois jogadores estão iniciando uma batalha ranqueada.
     */
    public static void startRankedBattle(ServerPlayerEntity player1, ServerPlayerEntity player2) {
        if (player1 != null) rankedBattlePlayers.add(player1.getUUID());
        if (player2 != null) rankedBattlePlayers.add(player2.getUUID());
    }

    /**
     * Remove os jogadores do rastreamento (a batalha terminou).
     */
    public static void endRankedBattle(ServerPlayerEntity player1, ServerPlayerEntity player2) {
        if (player1 != null) rankedBattlePlayers.remove(player1.getUUID());
        if (player2 != null) rankedBattlePlayers.remove(player2.getUUID());
    }

    /**
     * Verifica se um jogador específico está em uma batalha ranqueada.
     */
    public static boolean isPlayerInRankedBattle(ServerPlayerEntity player) {
        if (player == null) return false;
        return rankedBattlePlayers.contains(player.getUUID());
    }
}