package com.raishxn.legendsarena.config;

import com.raishxn.legendsarena.config.models.EloTier;
import java.util.List;

public class ElosConfig {

    // O nome da variável deve ser "ranks" para bater com o YAML
    // O seu elos.yml deve começar com "ranks:"
    private List<EloTier> ranks;

    public List<EloTier> getRanks() {
        return ranks;
    }

    public void setRanks(List<EloTier> ranks) {
        this.ranks = ranks;
    }
}