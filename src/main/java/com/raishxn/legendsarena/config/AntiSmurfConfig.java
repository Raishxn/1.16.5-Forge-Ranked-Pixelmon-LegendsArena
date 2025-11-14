package com.raishxn.legendsarena.config;

public class AntiSmurfConfig {

    private boolean enabled;
    private Detection detection;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Detection getDetection() {
        return detection;
    }

    public void setDetection(Detection detection) {
        this.detection = detection;
    }

    public static class Detection {
        private int minGames;
        private double winRateThreshold;
        private String action;

        public int getMinGames() { return minGames; }
        public void setMinGames(int minGames) { this.minGames = minGames; }

        public double getWinRateThreshold() { return winRateThreshold; }
        public void setWinRateThreshold(double winRateThreshold) { this.winRateThreshold = winRateThreshold; }

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
    }
}