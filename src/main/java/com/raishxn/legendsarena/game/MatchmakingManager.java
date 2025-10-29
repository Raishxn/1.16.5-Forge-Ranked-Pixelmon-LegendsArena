package com.raishxn.legendsarena.game;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.raishxn.legendsarena.ModFile;
import com.raishxn.legendsarena.config.ConfigManager;
import com.raishxn.legendsarena.database.PlayerBanService;
import com.raishxn.legendsarena.database.PlayerStats;
import com.raishxn.legendsarena.database.PlayerStatsService;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MatchmakingManager {

    private static final Map<String, Map<UUID, ServerPlayerEntity>> queues = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final Map<UUID, String> activeRankedBattles = new ConcurrentHashMap<>();

    public static void initialize() {
        scheduler.scheduleAtFixedRate(MatchmakingManager::findMatches, 5, 5, TimeUnit.SECONDS);
        ModFile.LOGGER.info("MatchmakingManager iniciado.");
    }

    public static void addPlayerToQueue(ServerPlayerEntity player, String tier) {
        String tierFormatted = tier.substring(0, 1).toUpperCase() + tier.substring(1).toLowerCase();

        Boolean isTierActive = ConfigManager.get("ranked-tiers." + tierFormatted + ".active");
        if (isTierActive == null) {
            player.sendMessage(new StringTextComponent(TextFormatting.RED + "A tier '" + tier + "' não existe."), player.getUUID());
            return;
        }
        if (!isTierActive) {
            player.sendMessage(new StringTextComponent(TextFormatting.RED + "A temporada para a tier '" + tier + "' não está ativa."), player.getUUID());
            return;
        }

        if (PlayerBanService.isPlayerBanned(player, tier)) {
            player.sendMessage(new StringTextComponent(TextFormatting.RED + "Você está banido de competir nesta tier."), player.getUUID());
            return;
        }

        removePlayerFromAllQueues(player);
        queues.computeIfAbsent(tier.toLowerCase(), k -> new ConcurrentHashMap<>()).put(player.getUUID(), player);
        player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Você entrou na fila para a tier " + tier + "."), player.getUUID());
    }

    public static void removePlayerFromAllQueues(ServerPlayerEntity player) {
        queues.values().forEach(queue -> {
            if (queue.remove(player.getUUID()) != null) {
                player.sendMessage(new StringTextComponent(TextFormatting.YELLOW + "Você foi removido da fila."), player.getUUID());
            }
        });
    }

    /**
     * Lógica de matchmaking inteligente que respeita a diferença de ranks.
     */
    private static void findMatches() {
        int maxRankDiff = ConfigManager.get("matchmaking-settings.max-rank-difference");

        for (String tier : queues.keySet()) {
            Map<UUID, ServerPlayerEntity> queue = queues.get(tier);
            if (queue.size() < 2) continue;

            List<ServerPlayerEntity> playersInQueue = new ArrayList<>(queue.values());

            // Percorre a fila comparando cada jogador com todos os outros
            for (int i = 0; i < playersInQueue.size(); i++) {
                for (int j = i + 1; j < playersInQueue.size(); j++) {
                    ServerPlayerEntity player1 = playersInQueue.get(i);
                    ServerPlayerEntity player2 = playersInQueue.get(j);

                    PlayerStats stats1 = PlayerStatsService.getPlayerStats(player1, tier);
                    PlayerStats stats2 = PlayerStatsService.getPlayerStats(player2, tier);

                    if (stats1 == null || stats2 == null) continue;

                    String rank1 = RankService.getRankFromElo(stats1.getElo());
                    String rank2 = RankService.getRankFromElo(stats2.getElo());

                    // A verificação principal acontece aqui!
                    if (RankService.getRankDifference(rank1, rank2) <= maxRankDiff) {
                        // Par compatível encontrado!
                        queue.remove(player1.getUUID());
                        queue.remove(player2.getUUID());

                        activeRankedBattles.put(player1.getUUID(), tier);
                        activeRankedBattles.put(player2.getUUID(), tier);

                        player1.sendMessage(new StringTextComponent(TextFormatting.AQUA + "Partida encontrada contra " + player2.getName().getString() + "!"), player1.getUUID());
                        player2.sendMessage(new StringTextComponent(TextFormatting.AQUA + "Partida encontrada contra " + player1.getName().getString() + "!"), player2.getUUID());

                        startRankedBattle(player1, player2);

                        // Recomeça a busca na mesma tier, pois a lista de jogadores foi alterada
                        findMatches();
                        return;
                    }
                }
            }
        }
    }

    private static void startRankedBattle(ServerPlayerEntity player1, ServerPlayerEntity player2) {
        PlayerPartyStorage storage1 = StorageProxy.getParty(player1);
        PlayerPartyStorage storage2 = StorageProxy.getParty(player2);

        if (storage1 == null || storage2 == null) {
            ModFile.LOGGER.error("Não foi possível obter a party de um dos jogadores para a batalha ranqueada.");
            String tier = getPlayerBattleTier(player1.getUUID());
            if(tier != null) {
                queues.get(tier).put(player1.getUUID(), player1);
                queues.get(tier).put(player2.getUUID(), player2);
            }
            return;
        }

        Pokemon[] team1 = storage1.getAll();
        Pokemon[] team2 = storage2.getAll();

        PlayerParticipant participant1 = new PlayerParticipant(player1, team1);
        PlayerParticipant participant2 = new PlayerParticipant(player2, team2);

        BattleRegistry.startBattle(participant1, participant2);
    }

    public static String getPlayerBattleTier(UUID playerUuid) {
        return activeRankedBattles.get(playerUuid);
    }

    public static void endRankedBattleForPlayers(UUID... playerUuids) {
        for (UUID uuid : playerUuids) {
            activeRankedBattles.remove(uuid);
        }
    }
}