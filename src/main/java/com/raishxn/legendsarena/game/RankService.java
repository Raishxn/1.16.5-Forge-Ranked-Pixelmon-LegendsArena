package com.raishxn.legendsarena.game;

import com.raishxn.legendsarena.config.ConfigManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RankService {

    // Cache para evitar ler o config a toda a hora
    private static List<String> rankOrder;

    public static String getRankFromElo(int elo) {
        // O LinkedHashMap preserva a ordem de inserção, mas vamos garantir a ordem para o cálculo de diferença
        Map<String, Map<String, Integer>> eloRanks = ConfigManager.get("elo-ranks");
        if (eloRanks == null) {
            return "Unranked";
        }

        for (Map.Entry<String, Map<String, Integer>> entry : eloRanks.entrySet()) {
            String rankName = entry.getKey();
            Map<String, Integer> ranges = entry.getValue();
            int min = ranges.get("min");
            int max = ranges.get("max");

            if (elo >= min && elo <= max) {
                return rankName;
            }
        }
        return "Unranked";
    }

    public static int getRankDifference(String rank1, String rank2) {
        if (rankOrder == null) {
            // Carrega a ordem dos ranks a partir do config na primeira vez
            Map<String, Map<String, Integer>> eloRanks = ConfigManager.get("elo-ranks");
            if (eloRanks == null) return 999;
            rankOrder = new ArrayList<>(eloRanks.keySet());
        }

        int index1 = rankOrder.indexOf(rank1);
        int index2 = rankOrder.indexOf(rank2);

        if (index1 == -1 || index2 == -1) {
            return 999; // Rank não encontrado, retorna uma diferença grande para impedir o match
        }

        return Math.abs(index1 - index2);
    }
}