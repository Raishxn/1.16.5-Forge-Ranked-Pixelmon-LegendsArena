package com.raishxn.legendsarena.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.permission.Permissible;
import com.envyful.api.forge.chat.UtilChatColour;
import net.minecraft.entity.player.ServerPlayerEntity;

@Command(
        value = "ranked",
        description = "Comando principal para o sistema de ranqueadas",
        aliases = { "r" }
)
@Permissible("legendsarena.command.ranked")
public class RankedCommand {

    // Exemplo de como seria /ranked join <tier>
    @CommandProcessor("join")
    public void onJoinCommand(ServerPlayerEntity sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(UtilChatColour.colour("&cPor favor, especifique uma tier. Ex: /ranked join ou"), sender.getUUID());
            return;
        }

        String tier = args[0];
        sender.sendMessage(UtilChatColour.colour("&aA entrar na fila para a tier: " + tier), sender.getUUID());
        // TODO: Lógica de matchmaking aqui
    }

    // Este método será executado se o jogador digitar apenas /ranked
    @CommandProcessor
    public void onBaseCommand(ServerPlayerEntity sender, String[] args) {
        sender.sendMessage(UtilChatColour.colour("&6Comandos disponíveis: /ranked join <tier>"), sender.getUUID());
    }
}