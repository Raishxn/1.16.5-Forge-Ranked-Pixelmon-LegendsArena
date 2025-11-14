package com.raishxn.legendsarena.queue;

// --- Imports corretos da API 1.16.5 ---
import com.pixelmonmod.pixelmon.battles.api.BattleBuilder;
import com.pixelmonmod.pixelmon.api.battles.BattleType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRuleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.api.rules.clauses.BattleClauseRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant; // <-- NOVO IMPORT

// Imports do nosso mod
import com.raishxn.legendsarena.battle.BattleManager;
import com.raishxn.legendsarena.legendsarena;
import com.raishxn.legendsarena.config.TiersConfig;

// Imports do Minecraft/Forge
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

// Imports do Java
import java.util.*;
import java.util.stream.Collectors;

public class QueueManager {

    // Mapeia o ID da Tier (ex: "ou") para uma Fila de jogadores
    private static final Map<String, Queue<ServerPlayerEntity>> queues = new HashMap<>();

    // Mapeia o UUID do jogador para qual fila (ID da Tier) ele está
    private static final Map<UUID, String> playerQueues = new HashMap<>();

    /**
     * Adiciona um jogador a uma fila de tier.
     */
    public static boolean addPlayer(ServerPlayerEntity player, TiersConfig.TierData tier) {
        if (isInQueue(player)) {
            return false;
        }
        String tierId = tier.getId();
        UUID playerUUID = player.getUUID();
        Queue<ServerPlayerEntity> queue = queues.computeIfAbsent(tierId, k -> new LinkedList<>());
        queue.add(player);
        playerQueues.put(playerUUID, tierId);
        return true;
    }

    /**
     * Remove um jogador de qualquer fila em que esteja.
     */
    public static boolean removePlayer(ServerPlayerEntity player) {
        UUID playerUUID = player.getUUID();
        if (!playerQueues.containsKey(playerUUID)) {
            return false;
        }
        String tierId = playerQueues.remove(playerUUID);
        if (tierId != null && queues.containsKey(tierId)) {
            queues.get(tierId).remove(player);
        }
        return true;
    }

    /**
     * Verifica se um jogador já está em alguma fila.
     */
    public static boolean isInQueue(ServerPlayerEntity player) {
        return playerQueues.containsKey(player.getUUID());
    }

    /**
     * Processa todas as filas e tenta criar partidas.
     * Este método é chamado pelo ServerEvents a cada X segundos.
     */
    public static void processQueues() {
        // Itera por uma cópia das chaves para evitar ConcurrentModificationException
        for (String tierId : new ArrayList<>(queues.keySet())) {
            Queue<ServerPlayerEntity> queue = queues.get(tierId);
            if (queue == null) continue;

            while (queue.size() >= 2) {
                ServerPlayerEntity player1 = queue.poll();
                ServerPlayerEntity player2 = queue.poll();

                if (player1 != null) playerQueues.remove(player1.getUUID());
                if (player2 != null) playerQueues.remove(player2.getUUID());

                if (player1 != null && player2 != null && player1.isAlive() && player2.isAlive()) {
                    legendsarena.LOGGER.info("Partida encontrada para " + tierId + ": " + player1.getName().getString() + " vs " + player2.getName().getString());

                    String matchFoundMsg = legendsarena.getMessagesConfig().getMatch().getMatchFound()
                            .replace("%opponent%", player2.getName().getString());
                    player1.sendMessage(new StringTextComponent(matchFoundMsg), player1.getUUID());

                    matchFoundMsg = legendsarena.getMessagesConfig().getMatch().getMatchFound()
                            .replace("%opponent%", player1.getName().getString());
                    player2.sendMessage(new StringTextComponent(matchFoundMsg), player2.getUUID());

                    startRankedBattle(player1, player2, tierId);
                } else {
                    if (player1 != null && player1.isAlive()) {
                        queue.add(player1);
                        playerQueues.put(player1.getUUID(), tierId);
                    }
                    if (player2 != null && player2.isAlive()) {
                        queue.add(player2);
                        playerQueues.put(player2.getUUID(), tierId);
                    }
                }
            }
        }
    }

    /**
     * Inicia uma batalha ranqueada usando a API do Pixelmon (BattleBuilder).
     */
    private static void startRankedBattle(ServerPlayerEntity player1, ServerPlayerEntity player2, String tierId) {
        try {
            // 1. Buscar as regras da tier no TiersConfig
            TiersConfig.TierData tierConfig = legendsarena.getTiersConfig().getTiers().stream()
                    .filter(t -> t.getId().equals(tierId))
                    .findFirst()
                    .orElse(null);

            // 2. Criar o objeto de Regras de Batalha
            BattleRules rules = new BattleRules();

            int teamSize = 6; // Padrão
            BattleType battleType = BattleType.SINGLE; // Padrão

            // --- CORREÇÃO 1: LÓGICA DE CLÁUSULAS ---
            // Pega o Set de cláusulas padrão (mutável)
            Set<BattleClause> clauses = new HashSet<>(rules.getOrDefault(BattleRuleRegistry.CLAUSES));

            if (tierConfig != null && tierConfig.getBattleSpecs() != null) {
                TiersConfig.BattleSpecs specs = tierConfig.getBattleSpecs();

                try {
                    battleType = BattleType.valueOf(specs.getBattlespot().toUpperCase(Locale.ROOT));
                } catch (Exception e) {
                    legendsarena.LOGGER.warn("Battlespot '" + specs.getBattlespot() + "' inválido no tiers.yml. Usando SINGLE.");
                    battleType = BattleType.SINGLE;
                }

                rules.set(BattleRuleRegistry.BATTLE_TYPE, battleType);
                rules.set(BattleRuleRegistry.LEVEL_CAP, specs.getLevelCap());

                if (specs.getRules() != null) {
                    for (String ruleId : specs.getRules()) {
                        if (ruleId.matches("^\\d+v\\d+$")) { // Ex: "6v6"
                            try {
                                teamSize = Integer.parseInt(ruleId.substring(0, 1));
                            } catch (Exception e) { /* ignora */ }
                        } else if (BattleClauseRegistry.hasClause(ruleId)) {
                            // Adiciona a cláusula ao nosso Set
                            clauses.add(BattleClauseRegistry.getClause(ruleId));
                        }
                    }
                }

            } else {
                legendsarena.LOGGER.warn("Tier " + tierId + " não encontrada no config. Usando regras padrão (Lvl 100, 6v6).");
                rules.set(BattleRuleRegistry.BATTLE_TYPE, BattleType.SINGLE);
                rules.set(BattleRuleRegistry.LEVEL_CAP, 100);
            }

            rules.set(BattleRuleRegistry.NUM_POKEMON, teamSize);

            // Define o Set de cláusulas atualizado de volta nas regras
            rules.set(BattleRuleRegistry.CLAUSES, clauses);
            // --- FIM DA CORREÇÃO 1 ---

            // 3. Pegar as Partys dos jogadores
            PlayerPartyStorage party1 = StorageProxy.getStorageManager().getParty(player1);
            PlayerPartyStorage party2 = StorageProxy.getStorageManager().getParty(player2);

            // 4. Montar os times (List<Pokemon> é usado para o .team() no 1.12, mas não aqui)
            // Não precisamos mais disso para o BattleBuilder

            // --- CORREÇÃO 2: LÓGICA DO BATTLEBUILDER ---
            // Cria os BattleParticipants que o BattleBuilder espera
            PlayerParticipant participant1 = new PlayerParticipant(player1, party1);
            PlayerParticipant participant2 = new PlayerParticipant(player2, party2);

            // 5. Usar o BattleBuilder para iniciar a batalha
            BattleBuilder.builder()
                    .rules(rules)
                    .teamOne(participant1) // <-- Correto
                    .teamTwo(participant2) // <-- Correto
                    .start(); // Inicia a batalha!

            // 6. Rastreia a batalha como ranqueada
            BattleManager.startRankedBattle(player1, player2);

        } catch (Exception e) {
            legendsarena.LOGGER.error("Falha ao iniciar batalha ranqueada:", e);
            player1.sendMessage(new StringTextComponent("§cOcorreu um erro ao iniciar sua partida."), player1.getUUID());
            player2.sendMessage(new StringTextComponent("§cOcorreu um erro ao iniciar sua partida."), player2.getUUID());
        }
    }
}