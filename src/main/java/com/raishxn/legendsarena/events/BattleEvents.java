package com.raishxn.legendsarena.events;

import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.raishxn.legendsarena.battle.BattleManager;
import com.raishxn.legendsarena.legendsarena;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class BattleEvents {

    @SubscribeEvent
    public static void onBattleEnd(BattleEndEvent event) {
        // Pega todos os jogadores envolvidos na batalha
        List<PlayerParticipant> players = event.getParticipants().stream()
                .filter(p -> p instanceof PlayerParticipant)
                .map(p -> (PlayerParticipant) p)
                .collect(Collectors.toList());

        // Se não for uma batalha PVP (1v1), ignora
        if (players.size() != 2) {
            return;
        }

        PlayerParticipant p1 = players.get(0);
        PlayerParticipant p2 = players.get(1);

        // Verifica se esta batalha ESTÁ sendo rastreada pelo nosso BattleManager
        if (BattleManager.isPlayerInRankedBattle(p1.player) || BattleManager.isPlayerInRankedBattle(p2.player)) {

            // A batalha terminou, remove os jogadores do rastreamento
            BattleManager.endRankedBattle(p1.player, p2.player);

            // Determina o vencedor e o perdedor
            PlayerParticipant winner = null;
            PlayerParticipant loser = null;

            if (event.getResult(p1).result.isWin()) {
                winner = p1;
                loser = p2;
            } else if (event.getResult(p2).result.isWin()) {
                winner = p2;
                loser = p1;
            } else {
                // Foi um empate (Draw)
                p1.player.sendMessage(new StringTextComponent("§eA partida ranqueada terminou em empate!"), p1.player.getUUID());
                p2.player.sendMessage(new StringTextComponent("§eA partida ranqueada terminou em empate!"), p2.player.getUUID());
                return;
            }

            legendsarena.LOGGER.info("Batalha ranqueada finalizada. Vencedor: " + winner.getName() + ", Perdedor: " + loser.getName());

            //
            // --- AQUI ENTRA A LÓGICA DE ELO ---
            //
            // TODO: Chamar o DatabaseManager para buscar o ELO de ambos
            // int eloWinner = DatabaseManager.getElo(winner.player.getUUID());
            // int eloLoser = DatabaseManager.getElo(loser.player.getUUID());
            int eloWinner = 1200; // Placeholder
            int eloLoser = 1200; // Placeholder

            // TODO: Calcular o novo ELO (ex: Fórmula K-Factor)
            // int k = 32;
            // double expectedWinner = 1.0 / (1.0 + Math.pow(10, (eloLoser - eloWinner) / 400.0));
            // double expectedLoser = 1.0 / (1.0 + Math.pow(10, (eloWinner - eloLoser) / 400.0));
            // int newEloWinner = (int) (eloWinner + k * (1 - expectedWinner));
            // int newEloLoser = (int) (eloLoser + k * (0 - expectedLoser));
            int newEloWinner = 1220; // Placeholder
            int newEloLoser = 1180; // Placeholder
            int eloChange = 20; // Placeholder

            // TODO: Salvar o novo ELO no DatabaseManager
            // DatabaseManager.setElo(winner.player.getUUID(), newEloWinner);
            // DatabaseManager.setElo(loser.player.getUUID(), newEloLoser);
            // DatabaseManager.addWin(winner.player.getUUID());
            // DatabaseManager.addLoss(loser.player.getUUID());

            // Envia as mensagens de resultado do messages.yml
            String msgWinner = legendsarena.getMessagesConfig().getMatch().getEloChange()
                    .replace("%new_elo%", String.valueOf(newEloWinner))
                    .replace("%change%", "§a+" + eloChange);
            winner.player.sendMessage(new StringTextComponent(msgWinner), winner.player.getUUID());

            String msgLoser = legendsarena.getMessagesConfig().getMatch().getEloChange()
                    .replace("%new_elo%", String.valueOf(newEloLoser))
                    .replace("%change%", "§c-" + eloChange);
            loser.player.sendMessage(new StringTextComponent(msgLoser), loser.player.getUUID());
        }
    }
}