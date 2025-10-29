package com.raishxn.legendsarena.game;

import com.raishxn.legendsarena.ModFile;
import com.raishxn.legendsarena.config.categories.RankConfig;
import com.raishxn.legendsarena.database.PlayerStats;

public class EloService {

    // Lógica principal de cálculo de ELO (ainda por implementar completamente)
    public static void calculateAndApplyElo(PlayerStats winner, PlayerStats loser) {
        // TODO: Implementar a lógica de cálculo de ELO usando o config.yml
        // 1. Obter o K-factor para cada jogador
        // 2. Calcular a probabilidade de vitória
        // 3. Calcular a mudança de ELO
        // 4. Aplicar bónus de winstreak
        // 5. Aplicar limites (max gain/loss, min elo)
        // 6. Atualizar os objectos PlayerStats
        // 7. Salvar na base de dados com PlayerStatsService.updatePlayerStats()
    }

    // Determina o K-Factor a ser usado para um jogador
    public static int getKFactor(PlayerStats player) {
        // Lógica de K-Factor provisório
        if (player.getMatchesPlayed() < ModFile.getInstance().getConfig().getElo().getProvisionalMatches()) {
            return ModFile.getInstance().getConfig().getElo().getProvisionalKFactor();
        }

        // Lógica de K-Factor por rank
        for (RankConfig rank : ModFile.getInstance().getConfig().getRanks()) {
            if (player.getElo() >= rank.getMinElo() && player.getElo() <= rank.getMaxElo()) {
                return rank.getkFactor();
            }
        }

        // Retorna um valor padrão se nenhum rank for encontrado (não deve acontecer)
        return 32;
    }
}