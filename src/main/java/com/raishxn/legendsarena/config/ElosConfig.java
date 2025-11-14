package com.raishxn.legendsarena.config;

import com.raishxn.legendsarena.config.models.EloTier;

import java.util.List;

public class ElosConfig {

    private List<EloTier> tiers;

    public List<EloTier> getTiers() {
        return tiers;
    }

    public void setTiers(List<EloTier> tiers) {
        this.tiers = tiers;
    }
}