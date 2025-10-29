package com.raishxn.legendsarena.listener;

import com.pixelmonmod.pixelmon.api.battles.BattleResults;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.raishxn.legendsarena.ModFile;
import com.raishxn.legendsarena.database.PlayerStats;
import com.raishxn.legendsarena.database.PlayerStatsService;
import com.raishxn.legendsarena.game.EloService;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class BattleEndListener {

    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event) {
        List<ServerPlayerEntity> players = new ArrayList<>();
        for (BattleParticipant participant : event.getResults().keySet()) {
            if (participant instanceof PlayerParticipant) {
                // CORREÇÃO: Aceder diretamente ao campo público 'player'
                players.add(((PlayerParticipant) participant).player);
            }
        }

        if (players.size() != 2) {
            return;
        }

        ServerPlayerEntity player1 = players.get(0);
        ServerPlayerEntity player2 = players.get(1);

        // TODO: Lógica para verificar se a batalha é ranqueada virá do MatchmakingManager

        BattleParticipant participant1 = event.getBattleController().getParticipantForEntity(player1);
        BattleParticipant participant2 = event.getBattleController().getParticipantForEntity(player2);

        if (participant1 == null || participant2 == null) {
            return;
        }

        BattleResults result1 = event.getResults().get(participant1);
        BattleResults result2 = event.getResults().get(participant2);

        ServerPlayerEntity winnerEntity;
        ServerPlayerEntity loserEntity;

        if (result1 == BattleResults.VICTORY) {
            winnerEntity = player1;
            loserEntity = player2;
        } else if (result2 == BattleResults.VICTORY) {
            winnerEntity = player2;
            loserEntity = player1;
        } else {
            return; // Empate ou outro resultado
        }

        if (winnerEntity == null || loserEntity == null) {
            return;
        }

        String battleQueueId = "ou"; // Placeholder

        PlayerStats winnerStats = PlayerStatsService.getPlayerStats(winnerEntity.getUUID(), battleQueueId);
        PlayerStats loserStats = PlayerStatsService.getPlayerStats(loserEntity.getUUID(), battleQueueId);

        if (winnerStats == null || loserStats == null) {
            ModFile.LOGGER.warn("Batalha terminada, mas um dos jogadores não tinha estatísticas para a fila " + battleQueueId);
            return;
        }

        EloService.calculateAndApplyElo(winnerStats, loserStats);

        winnerEntity.sendMessage(new StringTextComponent("§aVocê venceu! Novo ELO: " + winnerStats.getElo()), winnerEntity.getUUID());
        loserEntity.sendMessage(new StringTextComponent("§cVocê perdeu! Novo ELO: " + loserStats.getElo()), loserEntity.getUUID());
    }
}