package com.raishxn.legendsarena.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.raishxn.legendsarena.legendsarena;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class RankedAdminCommands {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {

        // Comando principal /ranked admin
        dispatcher.register(Commands.literal("ranked")
                .then(Commands.literal("admin")
                        // CORREÇÃO 1: hasPermissionLevel -> hasPermission
                        .requires(source -> source.hasPermission(2))

                        // /ranked admin reload
                        .then(Commands.literal("reload")
                                .executes(RankedAdminCommands::reloadConfigs)
                        )

                        // /ranked admin ban <player> <tier> [reason]
                        .then(Commands.literal("ban")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("tier", StringArgumentType.string())
                                                .executes(ctx -> banPlayer(ctx, false)) // Sem razão
                                                .then(Commands.argument("reason", StringArgumentType.greedyString())
                                                        .executes(ctx -> banPlayer(ctx, true)) // Com razão
                                                )
                                        )
                                )
                        )

                        // /ranked admin unban <player> <tier>
                        .then(Commands.literal("unban")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("tier", StringArgumentType.string())
                                                .executes(RankedAdminCommands::unbanPlayer)
                                        )
                                )
                        )

                        // /ranked admin season <start|end>
                        .then(Commands.literal("season")
                                .then(Commands.literal("start")
                                        .executes(RankedAdminCommands::startSeason)
                                )
                                .then(Commands.literal("end")
                                        .executes(RankedAdminCommands::endSeason)
                                )
                        )

                        // /ranked admin reset <player> <tier|@all>
                        .then(Commands.literal("reset")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("tier", StringArgumentType.string())
                                                .executes(RankedAdminCommands::resetPlayer)
                                        )
                                )
                        )

                        // /ranked admin forcematch <player1> <player2> <tier>
                        .then(Commands.literal("forcematch")
                                .then(Commands.argument("player1", EntityArgument.player())
                                        .then(Commands.argument("player2",EntityArgument.player())
                                                .then(Commands.argument("tier", StringArgumentType.string())
                                                        .executes(RankedAdminCommands::forceMatch)
                                                )
                                        )
                                )
                        )

                        // /ranked admin elo <set|add|remove> <player> <tier> <amount>
                        .then(Commands.literal("elo")
                                .then(Commands.literal("set")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .then(Commands.argument("tier", StringArgumentType.string())
                                                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                                .executes(ctx -> setElo(ctx, "set"))
                                                        )
                                                )
                                        )
                                )
                                .then(Commands.literal("add")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .then(Commands.argument("tier", StringArgumentType.string())
                                                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                                .executes(ctx -> setElo(ctx, "add"))
                                                        )
                                                )
                                        )
                                )
                                .then(Commands.literal("remove")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .then(Commands.argument("tier", StringArgumentType.string())
                                                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                                .executes(ctx -> setElo(ctx, "remove"))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    // --- LÓGICA DOS COMANDOS DE ADMIN ---
    // CORREÇÃO 2: sendFeedback -> sendSuccess

    private static int reloadConfigs(CommandContext<CommandSource> context) {
        // Usamos 'true' no sendSuccess para notificar os OPs
        context.getSource().sendSuccess(new StringTextComponent("§aRecarregando configurações..."), true);
        try {
            // Recarrega as configs
            legendsarena.getConfigManager().loadConfigs();
            context.getSource().sendSuccess(new StringTextComponent("§aConfigurações de LegendsArena recarregadas com sucesso!"), true);
            return 1;
        } catch (Exception e) {
            legendsarena.LOGGER.error("Falha ao recarregar configs", e);
            context.getSource().sendFailure(new StringTextComponent("§cFalha ao recarregar configs. Verifique o console."));
            return 0;
        }
    }

    private static int banPlayer(CommandContext<CommandSource> context, boolean hasReason) {
        try {
            ServerPlayerEntity player = EntityArgument.getPlayer(context, "player");
            String tier = StringArgumentType.getString(context, "tier");
            String reason = hasReason ? StringArgumentType.getString(context, "reason") : "N/A";

            // TODO: Lógica para registrar o banimento no DatabaseManager
            // DatabaseManager.banPlayer(player.getUUID(), tier, reason);

            context.getSource().sendSuccess(new StringTextComponent("§aJogador " + player.getName().getString() + " banido da tier " + tier + ". Motivo: " + reason), true);
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(new StringTextComponent("§cErro: " + e.getMessage()));
            return 0;
        }
    }

    private static int unbanPlayer(CommandContext<CommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgument.getPlayer(context, "player");
            String tier = StringArgumentType.getString(context, "tier");

            // TODO: Lógica para remover o banimento no DatabaseManager
            // DatabaseManager.unbanPlayer(player.getUUID(), tier);

            context.getSource().sendSuccess(new StringTextComponent("§aJogador " + player.getName().getString() + " desbanido da tier " + tier), true);
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(new StringTextComponent("§cErro: " + e.getMessage()));
            return 0;
        }
    }

    private static int endSeason(CommandContext<CommandSource> context) {
        // TODO: Lógica para terminar a temporada (distribuir recompensas)
        context.getSource().sendSuccess(new StringTextComponent("§aTemporada finalizada com sucesso!"), true);
        return 1;
    }

    private static int startSeason(CommandContext<CommandSource> context) {
        // TODO: Lógica para iniciar uma nova temporada (pode envolver resetar ELO de todos)
        context.getSource().sendSuccess(new StringTextComponent("§aTemporada iniciada com sucesso!"), true);
        return 1;
    }

    private static int resetPlayer(CommandContext<CommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgument.getPlayer(context, "player");
            String tier = StringArgumentType.getString(context, "tier");
            // TODO: Lógica para resetar o ELO/Stats do jogador
            context.getSource().sendSuccess(new StringTextComponent("§aStats de " + player.getName().getString() + " resetados para a tier " + tier), true);
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(new StringTextComponent("§cErro: " + e.getMessage()));
            return 0;
        }
    }

    private static int forceMatch(CommandContext<CommandSource> context) {
        try {
            ServerPlayerEntity player1 = EntityArgument.getPlayer(context, "player1");
            ServerPlayerEntity player2 = EntityArgument.getPlayer(context, "player2");
            String tier = StringArgumentType.getString(context, "tier");

            // TODO: Lógica para forçar o início de uma batalha (chamar o Pixelmon API)
            // BattleManager.startBattle(player1, player2, ...);

            context.getSource().sendSuccess(new StringTextComponent("§aForçando partida entre " + player1.getName().getString() + " e " + player2.getName().getString()), true);
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(new StringTextComponent("§cErro: " + e.getMessage()));
            return 0;
        }
    }

    private static int setElo(CommandContext<CommandSource> context, String mode) {
        try {
            ServerPlayerEntity player = EntityArgument.getPlayer(context, "player");
            String tier = StringArgumentType.getString(context, "tier");
            int amount = IntegerArgumentType.getInteger(context, "amount");

            // TODO: Lógica no DatabaseManager
            // int currentElo = DatabaseManager.getElo(player.getUUID(), tier);
            // int newElo = 0;
            String action = "";

            switch(mode) {
                case "set":
                    // newElo = amount;
                    action = "definido para";
                    break;
                case "add":
                    // newElo = currentElo + amount;
                    action = "adicionado (total";
                    break;
                case "remove":
                    // newElo = currentElo - amount;
                    action = "removido (total";
                    break;
            }

            // DatabaseManager.setElo(player.getUUID(), tier, newElo);

            context.getSource().sendSuccess(new StringTextComponent("§aELO de " + player.getName().getString() + " " + action + ": " + amount), true);
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(new StringTextComponent("§cErro: " + e.getMessage()));
            return 0;
        }
    }
}