package com.raishxn.legendsarena.config;

import java.util.List;

// Importa a classe do modelo 'Tier'
import com.raishxn.legendsarena.config.models.Tier;

public class TiersConfig {

    // O seu tiers.yml deve começar com "tiers:"
    private List<Tier> tiers;

    public List<Tier> getTiers() {
        return tiers;
    }

    public void setTiers(List<Tier> tiers) {
        this.tiers = tiers;
    }
}