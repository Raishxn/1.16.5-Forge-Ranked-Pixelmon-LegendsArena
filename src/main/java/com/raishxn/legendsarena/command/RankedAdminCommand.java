package com.raishxn.legendsarena.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.raishxn.legendsarena.config.ConfigManager;
import com.raishxn.legendsarena.config.ConfigService;
import com.raishxn.legendsarena.database.PlayerBanService;
import com.raishxn.legendsarena.game.ReportService;
import com.raishxn.legendsarena.game.SeasonService;
import com.raishxn.legendsarena.util.TimeUtil;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class RankedAdminCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("rankedadmin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("ban")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("tier", StringArgumentType.word())
                                        .then(Commands.argument("time", StringArgumentType.word())
                                                .then(Commands.argument("reason", StringArgumentType.greedyString())
                                                        .executes(context -> {
                                                            ServerPlayerEntity playerToBan = EntityArgument.getPlayer(context, "player");
                                                            String tier = StringArgumentType.getString(context, "tier");
                                                            String timeStr = StringArgumentType.getString(context, "time");
                                                            String reason = StringArgumentType.getString(context, "reason");

                                                            long durationMillis = TimeUtil.parseDuration(timeStr);
                                                            if (durationMillis == -1L) {
                                                                context.getSource().sendFailure(new StringTextComponent(TextFormatting.RED + "Formato de tempo inválido. Use 's', 'm', 'h' ou 'd'. Ex: 7d, 12h, 30m."));
                                                                return 0;
                                                            }

                                                            PlayerBanService.banPlayer(playerToBan, tier, durationMillis, reason);
                                                            context.getSource().sendSuccess(new StringTextComponent(TextFormatting.GREEN + "Jogador " + playerToBan.getName().getString() + " foi banido da tier " + tier + "."), true);
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
                )
                .then(Commands.literal("unban")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("tier", StringArgumentType.word())
                                        .executes(context -> {
                                            ServerPlayerEntity playerToUnban = EntityArgument.getPlayer(context, "player");
                                            String tier = StringArgumentType.getString(context, "tier");
                                            PlayerBanService.unbanPlayer(playerToUnban, tier);
                                            context.getSource().sendSuccess(new StringTextComponent(TextFormatting.GREEN + "Banimento de " + playerToUnban.getName().getString() + " na tier " + tier + " foi removido."), true);
                                            return 1;
                                        })
                                )
                        )
                )
                .then(Commands.literal("season")
                        .then(Commands.literal("start")
                                .then(Commands.argument("tier", StringArgumentType.word())
                                        .executes(context -> {
                                            String tier = StringArgumentType.getString(context, "tier");
                                            if (ConfigService.setTierActive(tier, true)) {
                                                context.getSource().sendSuccess(new StringTextComponent(TextFormatting.GREEN + "A tier " + tier + " foi ativada. As filas agora estão abertas."), true);
                                            } else {
                                                context.getSource().sendFailure(new StringTextComponent(TextFormatting.RED + "Não foi possível ativar a tier. Verifique se o nome está correto e se o arquivo config.yml não está corrompido."));
                                            }
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("end")
                                .then(Commands.argument("tier", StringArgumentType.word())
                                        .executes(context -> {
                                            String tier = StringArgumentType.getString(context, "tier");
                                            CommandSource source = context.getSource();
                                            source.sendSuccess(new StringTextComponent(TextFormatting.YELLOW + "Finalizando temporada para a tier: " + tier + "..."), true);

                                            ConfigService.setTierActive(tier, false);
                                            source.sendSuccess(new StringTextComponent(TextFormatting.YELLOW + "A tier " + tier + " foi desativada. As filas agora estão fechadas."), true);

                                            boolean reportSuccess = ReportService.generateTop10Report(tier);
                                            if (reportSuccess) {
                                                source.sendSuccess(new StringTextComponent(TextFormatting.GREEN + "Relatório do Top 10 gerado com sucesso em /config/legendsarena/history/"), true);
                                            } else {
                                                source.sendSuccess(new StringTextComponent(TextFormatting.YELLOW + "Não foi possível gerar o relatório (provavelmente não há jogadores classificados na tier)."), true);
                                            }

                                            if ((Boolean) ConfigManager.get("season-settings.auto-apply-reset")) {
                                                SeasonService.endSeasonAndResetElo(tier);
                                                source.sendSuccess(new StringTextComponent(TextFormatting.GREEN + "Reset de ELO aplicado conforme o fator de reset."), true);
                                            }
                                            return 1;
                                        })
                                )
                        )
                )
                .then(Commands.literal("reload")
                        .executes(context -> {
                            ConfigManager.loadConfig();
                            context.getSource().sendSuccess(new StringTextComponent(TextFormatting.GREEN + "Configuração do LegendsArena recarregada com sucesso!"), true);
                            return 1;
                        })
                )
        );
    }
}