package com.raishxn.legendsarena.listener;

import com.pixelmonmod.pixelmon.api.battles.BattleResults; // Import corrigido
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.raishxn.legendsarena.database.PlayerStats;
import com.raishxn.legendsarena.database.PlayerStatsService;
import com.raishxn.legendsarena.game.EloService;
import com.raishxn.legendsarena.game.MatchmakingManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BattleEndListener {

    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event) {
        BattleController bc = event.getBattleController();

        List<PlayerParticipant> players = new ArrayList<>();
        for (BattleParticipant participant : bc.participants) {
            if (participant instanceof PlayerParticipant) {
                players.add((PlayerParticipant) participant);
            }
        }

        if (players.size() != 2) {
            return;
        }

        if (event.isAbnormal()) {
            return;
        }

        ServerPlayerEntity winnerEntity = null;
        ServerPlayerEntity loserEntity = null;

        // CORREÇÃO APLICADA: Usando a classe BattleResults importada corretamente
        for (Map.Entry<BattleParticipant, BattleResults> entry : event.getResults().entrySet()) {
            if (!(entry.getKey() instanceof PlayerParticipant)) continue;

            PlayerParticipant participant = (PlayerParticipant) entry.getKey();
            if (entry.getValue() == BattleResults.VICTORY) {
                winnerEntity = participant.player;
            } else {
                loserEntity = participant.player;
            }
        }

        if (winnerEntity == null || loserEntity == null) {
            return;
        }

        String battleTier = MatchmakingManager.getPlayerBattleTier(winnerEntity.getUUID());
        if (battleTier == null) {
            return;
        }

        PlayerStats winnerStats = PlayerStatsService.getPlayerStats(winnerEntity, battleTier);
        PlayerStats loserStats = PlayerStatsService.getPlayerStats(loserEntity, battleTier);

        if (winnerStats != null && loserStats != null) {
            EloService.processMatchResult(winnerStats, loserStats);
            winnerEntity.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Você venceu a partida ranqueada!"), winnerEntity.getUUID());
            loserEntity.sendMessage(new StringTextComponent(TextFormatting.RED + "Você perdeu a partida ranqueada."), loserEntity.getUUID());
        }

        MatchmakingManager.endRankedBattleForPlayers(winnerEntity.getUUID(), loserEntity.getUUID());
    }
}