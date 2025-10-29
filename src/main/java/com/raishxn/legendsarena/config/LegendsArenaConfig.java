package com.raishxn.legendsarena.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.raishxn.legendsarena.config.categories.RankConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

import java.util.Collections;
import java.util.List;

@ConfigPath("config/legendsarena/config.yml")
@ConfigSerializable
public class LegendsArenaConfig extends AbstractYamlConfig {

    private General general = new General();
    private Elo elo = new Elo();
    private List<RankConfig> ranks = Collections.singletonList(new RankConfig());

    public LegendsArenaConfig() {
        super();
    }

    public General getGeneral() {
        return this.general;
    }

    public Elo getElo() {
        return this.elo;
    }

    public List<RankConfig> getRanks() {
        return this.ranks;
    }

    @ConfigSerializable
    public static class General {
        private int baselineElo = 1000;

        public int getBaselineElo() {
            return this.baselineElo;
        }
    }

    @ConfigSerializable
    public static class Elo {
        private int provisionalMatches = 10;
        private int provisionalKFactor = 40;

        public int getProvisionalMatches() {
            return provisionalMatches;
        }

        public int getProvisionalKFactor() {
            return provisionalKFactor;
        }
    }
}