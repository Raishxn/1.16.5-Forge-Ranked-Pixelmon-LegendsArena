package com.raishxn.legendsarena.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.permission.Permissible;
import com.envyful.api.forge.chat.UtilChatColour;
import net.minecraft.entity.player.ServerPlayerEntity;

@Command(
        value = "rankedadmin",
        description = "Comandos de administração para o sistema de ranqueadas",
        aliases = { "ra" }
)
@Permissible("legendsarena.command.admin")
public class RankedAdminCommand {

    @CommandProcessor("reload")
    public void onReloadCommand(ServerPlayerEntity sender, String[] args) {
        // TODO: Lógica para recarregar a configuração
        sender.sendMessage(UtilChatColour.colour("&aConfiguração recarregada!"), sender.getUUID());
    }

    @CommandProcessor
    public void onBaseCommand(ServerPlayerEntity sender, String[] args) {
        sender.sendMessage(UtilChatColour.colour("&6Comandos de admin: /rankedadmin reload"), sender.getUUID());
    }
}