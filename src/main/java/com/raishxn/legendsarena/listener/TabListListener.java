package com.raishxn.legendsarena.listener;

import com.raishxn.legendsarena.config.ConfigManager;
import com.raishxn.legendsarena.database.PlayerStats;
import com.raishxn.legendsarena.database.PlayerStatsService;
import com.raishxn.legendsarena.game.RankService;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TabListListener {

    /**
     * Este evento é acionado pelo Forge sempre que ele precisa formatar
     * o nome de um jogador para a lista de TAB. É o local perfeito para adicionar nosso rank.
     */
    @SubscribeEvent
    public void onTabListNameFormat(PlayerEvent.TabListNameFormat event) {
        // Verifica se a funcionalidade está ativa no config.yml
        if (!(Boolean) ConfigManager.get("ui.show-rank-on-tab")) {
            return;
        }

        // Garante que estamos a trabalhar com um jogador do lado do servidor
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
            return;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

        // TODO: O sistema precisa saber qual a "tier principal" do jogador para buscar o ELO.
        // Por agora, vamos usar uma tier fixa como exemplo.
        String primaryTier = "bronze";

        PlayerStats stats = PlayerStatsService.getPlayerStats(player, primaryTier);
        if (stats == null) {
            return; // Não faz nada se não encontrar as estatísticas
        }

        String rank = RankService.getRankFromElo(stats.getElo());

        // Formata o nome para incluir o rank. Ex: "[Ouro] raishxn"
        StringTextComponent rankComponent = new StringTextComponent("[" + rank + "] ");
        rankComponent.withStyle(TextFormatting.GOLD); // Pode ser customizável no futuro

        StringTextComponent nameComponent = new StringTextComponent(player.getName().getString());

        // Define o novo nome de exibição formatado para o evento
        event.setDisplayName(rankComponent.append(nameComponent));
    }

    /**
     * No futuro, podemos criar uma função aqui para forçar a atualização da lista
     * para todos os jogadores online, chamando player.refreshTabListName(),
     * que por sua vez irá acionar o evento onTabListNameFormat novamente.
     */
}