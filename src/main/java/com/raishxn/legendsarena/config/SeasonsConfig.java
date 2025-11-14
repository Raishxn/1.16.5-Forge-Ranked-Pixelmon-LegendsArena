package com.raishxn.legendsarena.config;

public class SeasonsConfig {

    private String activeSeason;
    private ResetPolicy resetPolicy;

    // Getters e Setters
    public String getActiveSeason() {
        return activeSeason;
    }

    public void setActiveSeason(String activeSeason) {
        this.activeSeason = activeSeason;
    }

    public ResetPolicy getResetPolicy() {
        return resetPolicy;
    }

    public void setResetPolicy(ResetPolicy resetPolicy) {
        this.resetPolicy = resetPolicy;
    }

    // Classe interna para a seção "resetPolicy"
    public static class ResetPolicy {
        private String defaultPolicy; // Mudei de "default" para "defaultPolicy"
        private double preserveFactor;
        private int newPlayerBaseElo;

        // NOTA: "default" é uma palavra-chave em Java.
        // No seu seasons.yml, use "defaultPolicy" para evitar problemas.
        // Ex:
        // resetPolicy:
        //   defaultPolicy: "md5"

        public String getDefaultPolicy() {
            return defaultPolicy;
        }

        public void setDefaultPolicy(String defaultPolicy) {
            this.defaultPolicy = defaultPolicy;
        }

        public double getPreserveFactor() {
            return preserveFactor;
        }

        public void setPreserveFactor(double preserveFactor) {
            this.preserveFactor = preserveFactor;
        }

        public int getNewPlayerBaseElo() {
            return newPlayerBaseElo;
        }

        public void setNewPlayerBaseElo(int newPlayerBaseElo) {
            this.newPlayerBaseElo = newPlayerBaseElo;
        }
    }
}