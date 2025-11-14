package com.raishxn.legendsarena.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.raishxn.legendsarena.legendsarena;
import com.raishxn.legendsarena.config.MessagesConfig;
import com.raishxn.legendsarena.config.SeasonsConfig;
import com.raishxn.legendsarena.config.TiersConfig;
import com.raishxn.legendsarena.queue.QueueManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

import java.util.Optional;

public class RankedCommands {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        // O comando principal /ranked
        dispatcher.register(Commands.literal("ranked")
                // /ranked join <tier>
                .then(Commands.literal("join")
                        .then(Commands.argument("tier", StringArgumentType.string())
                                .executes(RankedCommands::joinQueue)
                        )
                )
                // /ranked leave
                .then(Commands.literal("leave")
                        .executes(RankedCommands::leaveQueue)
                )
                // /ranked stats [player]
                .then(Commands.literal("stats")
                        // /ranked stats (para si mesmo)
                        .executes(RankedCommands::showSelfStats)
                        // /ranked stats <player> (para outros)
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(RankedCommands::showOtherStats)
                        )
                )
                // /ranked top [tier]
                .then(Commands.literal("top")
                        // /ranked top (top global)
                        .executes(RankedCommands::showTopGlobal)
                        // /ranked top <tier>
                        .then(Commands.argument("tier", StringArgumentType.string())
                                .executes(RankedCommands::showTopTier)
                        )
                )
                // /ranked ladder <tier>
                .then(Commands.literal("ladder")
                        .then(Commands.argument("tier", StringArgumentType.string())
                                .executes(RankedCommands::showLadder)
                        )
                )
                // /ranked info
                .then(Commands.literal("info")
                        .executes(RankedCommands::showInfo)
                )
        );
    }

    // --- LÓGICA DOS COMANDOS ---

    private static int joinQueue(CommandContext<CommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayerOrException();
            String tierId = StringArgumentType.getString(context, "tier").toLowerCase();

            MessagesConfig messages = legendsarena.getMessagesConfig();
            TiersConfig tiers = legendsarena.getTiersConfig();

            Optional<TiersConfig.TierData> tierData = tiers.getTiers().stream()
                    .filter(t -> t.getId().equals(tierId))
                    .findFirst();

            if (!tierData.isPresent()) {
                player.sendMessage(new StringTextComponent(messages.getErrors().getTierNotFound()), player.getUUID());
                return 0;
            }

            if (!tierData.get().isEnabled()) {
                player.sendMessage(new StringTextComponent("§cEsta tier está desativada."), player.getUUID());
                return 0;
            }

            // TODO: Adicionar verificação de permissão
            // ...

            boolean joined = QueueManager.addPlayer(player, tierData.get());

            if (joined) {
                String joinMsg = messages.getGeneral().getQueueJoin().replace("%tier_name%", tierData.get().getName());
                player.sendMessage(new StringTextComponent(joinMsg), player.getUUID());
            } else {
                player.sendMessage(new StringTextComponent(messages.getErrors().getAlreadyInQueue()), player.getUUID());
            }

            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(new StringTextComponent("§cApenas jogadores podem usar este comando."));
            return 0;
        }
    }

    private static int leaveQueue(CommandContext<CommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayerOrException();
            MessagesConfig messages = legendsarena.getMessagesConfig();

            if (QueueManager.isInQueue(player)) {
                QueueManager.removePlayer(player);
                player.sendMessage(new StringTextComponent(messages.getGeneral().getQueueLeave()), player.getUUID());
            } else {
                player.sendMessage(new StringTextComponent(messages.getErrors().getNotInQueue()), player.getUUID());
            }

            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(new StringTextComponent("§cApenas jogadores podem usar este comando."));
            return 0;
        }
    }

    private static int showSelfStats(CommandContext<CommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayerOrException();
            return showStats(player.getName().getString(), context);
        } catch (Exception e) {
            context.getSource().sendFailure(new StringTextComponent("§cApenas jogadores podem usar este comando."));
            return 0;
        }
    }

    private static int showOtherStats(CommandContext<CommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgument.getPlayer(context, "player");
            return showStats(player.getName().getString(), context);
        } catch (Exception e) {
            context.getSource().sendFailure(new StringTextComponent("§cJogador não encontrado."));
            return 0;
        }
    }

    // --- SEÇÃO CORRIGIDA ---
    // Todos os sendFeedback foram trocados por sendSuccess(..., false)

    private static int showStats(String playerName, CommandContext<CommandSource> context) {
        // TODO: Lógica para buscar stats do 'playerName' no DatabaseManager
        // DatabaseManager.getPlayerStats(playerName)...

        context.getSource().sendSuccess(new StringTextComponent("§aExibindo stats de: §e" + playerName), false);
        context.getSource().sendSuccess(new StringTextComponent("§7- ELO: 1200"), false);
        context.getSource().sendSuccess(new StringTextComponent("§7- Vitórias: 0"), false);
        context.getSource().sendSuccess(new StringTextComponent("§7- Derrotas: 0"), false);
        return 1;
    }

    private static int showTopGlobal(CommandContext<CommandSource> context) {
        context.getSource().sendSuccess(new StringTextComponent("§6--- Top 10 Global ---"), false);
        // TODO: Lógica para buscar o Top 10 Global do DatabaseManager
        context.getSource().sendSuccess(new StringTextComponent("§e1. Player (1500 ELO)"), false);
        return 1;
    }

    private static int showTopTier(CommandContext<CommandSource> context) {
        String tierId = StringArgumentType.getString(context, "tier");
        context.getSource().sendSuccess(new StringTextComponent("§6--- Top 10 Tier: " + tierId + " ---"), false);
        // TODO: Lógica para buscar o Top 10 da Tier 'tierId' do DatabaseManager
        context.getSource().sendSuccess(new StringTextComponent("§e1. Player (1400 ELO)"), false);
        return 1;
    }

    private static int showLadder(CommandContext<CommandSource> context) {
        String tierId = StringArgumentType.getString(context, "tier");
        context.getSource().sendSuccess(new StringTextComponent("§a--- Ladder da Tier: " + tierId + " ---"), false);
        // TODO: Lógica para buscar a "ladder" (pode ser o top 50, ou a posição do player)
        return 1;
    }

    private static int showInfo(CommandContext<CommandSource> context) {
        SeasonsConfig seasons = legendsarena.getSeasonsConfig();
        context.getSource().sendSuccess(new StringTextComponent("§b--- LegendsArena Info ---"), false);
        context.getSource().sendSuccess(new StringTextComponent("§7Versão: 1.0.0"), false);
        context.getSource().sendSuccess(new StringTextComponent("§7Temporada: §f" + seasons.getCurrentSeason().getName()), false);
        context.getSource().sendSuccess(new StringTextComponent("§7Início: §f" + seasons.getCurrentSeason().getStartDate()), false);
        context.getSource().sendSuccess(new StringTextComponent("§7Fim: §f" + seasons.getCurrentSeason().getEndDate()), false);
        return 1;
    }
}