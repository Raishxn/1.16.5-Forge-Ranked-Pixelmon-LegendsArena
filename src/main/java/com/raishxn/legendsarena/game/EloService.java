package com.raishxn.legendsarena.game;

import com.raishxn.legendsarena.config.ConfigManager;
import com.raishxn.legendsarena.database.PlayerStats;
import com.raishxn.legendsarena.database.PlayerStatsService;

public class EloService {

    public static void processMatchResult(PlayerStats winner, PlayerStats loser) {
        int winnerOldElo = winner.getElo();
        int loserOldElo = loser.getElo();

        // 1. Calcular probabilidade de vitória
        double expectedWinner = 1.0 / (1.0 + Math.pow(10, (double) (loserOldElo - winnerOldElo) / 400.0));
        double expectedLoser = 1.0 / (1.0 + Math.pow(10, (double) (winnerOldElo - loserOldElo) / 400.0));

        // 2. Determinar K-Factor
        int kFactorWinner = getKFactor(winner);
        int kFactorLoser = getKFactor(loser);

        // 3. Calcular mudança base de ELO
        int eloChangeWinner = (int) Math.round(kFactorWinner * (1 - expectedWinner));
        int eloChangeLoser = (int) Math.round(kFactorLoser * (0 - expectedLoser));

        // 4. Adicionar bônus de winstreak (se aplicável)
        int winstreakBonus = 0;
        // CORREÇÃO APLICADA AQUI
        if ((Boolean) ConfigManager.get("matchmaking-settings.winstreak-bonus.enabled") &&
                winner.getWinstreak() >= (int)ConfigManager.get("matchmaking-settings.winstreak-bonus.min-streak-for-bonus")) {
            winstreakBonus = ConfigManager.get("matchmaking-settings.winstreak-bonus.bonus-elo-per-win");
            int bonusCap = ConfigManager.get("matchmaking-settings.winstreak-bonus.bonus-cap");
            if (winstreakBonus > bonusCap) {
                winstreakBonus = bonusCap;
            }
        }
        eloChangeWinner += winstreakBonus;

        // 5. Aplicar limites de ganho/perda do config
        int maxGain = ConfigManager.get("matchmaking-settings.max-elo-gain");
        int maxLoss = ConfigManager.get("matchmaking-settings.max-elo-loss");
        eloChangeWinner = Math.min(eloChangeWinner, maxGain);
        eloChangeLoser = Math.max(eloChangeLoser, -maxLoss); // Perda é negativa

        // 6. Atualizar stats do vencedor
        winner.setElo(winnerOldElo + eloChangeWinner);
        winner.setWins(winner.getWins() + 1);
        winner.setWinstreak(winner.getWinstreak() + 1);
        if (winner.getWinstreak() > winner.getBestWinstreak()) {
            winner.setBestWinstreak(winner.getWinstreak());
        }
        winner.setMatchesPlayed(winner.getMatchesPlayed() + 1);

        // 7. Atualizar stats do perdedor
        loser.setElo(loserOldElo + eloChangeLoser);
        loser.setLosses(loser.getLosses() + 1);
        loser.setWinstreak(0); // Zera a winstreak
        loser.setMatchesPlayed(loser.getMatchesPlayed() + 1);

        // 8. Aplicar piso mínimo de ELO
        int minElo = ConfigManager.get("matchmaking-settings.min-elo");
        if (loser.getElo() < minElo) {
            loser.setElo(minElo);
        }

        // 9. Salvar tudo no banco de dados
        PlayerStatsService.updatePlayerStats(winner);
        PlayerStatsService.updatePlayerStats(loser);
    }

    private static int getKFactor(PlayerStats player) {
        // CORREÇÃO APLICADA AQUI
        if ((Boolean) ConfigManager.get("matchmaking-settings.provisional.enabled") &&
                player.getMatchesPlayed() < (int)ConfigManager.get("matchmaking-settings.provisional.matches")) {
            return ConfigManager.get("matchmaking-settings.provisional.k_provisional");
        }

        String tierName = player.getTier();
        String formattedTier = tierName.substring(0, 1).toUpperCase() + tierName.substring(1).toLowerCase();
        return ConfigManager.get("matchmaking-settings.k_by_tier." + formattedTier);
    }
}