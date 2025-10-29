package com.raishxn.legendsarena.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.raishxn.legendsarena.database.PlayerStats;
import com.raishxn.legendsarena.database.PlayerStatsService;
import com.raishxn.legendsarena.game.MatchmakingManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class RankedCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("ranked")
                .then(Commands.literal("join")
                        .then(Commands.argument("tier", StringArgumentType.word())
                                .executes(context -> {
                                    ServerPlayerEntity player = context.getSource().getPlayerOrException();
                                    String tier = StringArgumentType.getString(context, "tier");
                                    MatchmakingManager.addPlayerToQueue(player, tier);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("leave")
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayerOrException();
                            MatchmakingManager.removePlayerFromAllQueues(player);
                            return 1;
                        })
                )
                .then(Commands.literal("status")
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayerOrException();
                            // TODO: Precisamos de uma forma de saber a tier principal do jogador.
                            // Por enquanto, usaremos uma tier fixa para o exemplo.
                            String tier = "bronze"; // Placeholder
                            PlayerStats stats = PlayerStatsService.getPlayerStats(player, tier);

                            if (stats != null) {
                                player.sendMessage(new StringTextComponent(TextFormatting.GOLD + "--- Suas Estatísticas (" + tier + ") ---"), player.getUUID());
                                player.sendMessage(new StringTextComponent("ELO: " + stats.getElo()), player.getUUID());
                                player.sendMessage(new StringTextComponent("Vitórias: " + stats.getWins()), player.getUUID());
                                player.sendMessage(new StringTextComponent("Derrotas: " + stats.getLosses()), player.getUUID());
                                player.sendMessage(new StringTextComponent("Winstreak Atual: " + stats.getWinstreak()), player.getUUID());
                                player.sendMessage(new StringTextComponent("Melhor Winstreak: " + stats.getBestWinstreak()), player.getUUID());
                            } else {
                                player.sendMessage(new StringTextComponent(TextFormatting.RED + "Não foi possível buscar suas estatísticas."), player.getUUID());
                            }
                            return 1;
                        })
                )
                .then(Commands.literal("top")
                        .then(Commands.argument("tier", StringArgumentType.word())
                                .executes(context -> {
                                    String tier = StringArgumentType.getString(context, "tier");
                                    List<PlayerStats> top10 = PlayerStatsService.getTop10Players(tier);
                                    CommandSource source = context.getSource();

                                    source.sendSuccess(new StringTextComponent(TextFormatting.GOLD + "--- Top 10 Ranqueados (" + tier + ") ---"), false);

                                    if (top10.isEmpty()) {
                                        source.sendSuccess(new StringTextComponent("Nenhum jogador classificado nesta tier ainda."), false);
                                    } else {
                                        int rank = 1;
                                        for (PlayerStats stats : top10) {
                                            source.sendSuccess(new StringTextComponent(
                                                    String.format("%d. %s - ELO: %d (V:%d/D:%d)",
                                                            rank++, stats.getPlayerName(), stats.getElo(), stats.getWins(), stats.getLosses())), false);
                                        }
                                    }
                                    return 1;
                                })
                        )
                )
        );
    }
}