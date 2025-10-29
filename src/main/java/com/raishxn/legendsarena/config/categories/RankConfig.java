package com.raishxn.legendsarena.config.categories;

import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

@ConfigSerializable
public class RankConfig {

    private String rankName = "Bronze";
    private int minElo = 0;
    private int maxElo = 1200;
    private int kFactor = 20;

    public RankConfig() {
    }

    public String getRankName() {
        return this.rankName;
    }

    public int getMinElo() {
        return this.minElo;
    }

    public int getMaxElo() {
        return this.maxElo;
    }

    public int getKFactor() {
        return this.kFactor;
    }
}