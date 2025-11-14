package com.raishxn.legendsarena.config;

public class SeasonsConfig {

    private CurrentSeason currentSeason;

    public CurrentSeason getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(CurrentSeason currentSeason) {
        this.currentSeason = currentSeason;
    }

    public static class CurrentSeason {
        private int id;
        private String name;
        private String startDate;
        private String endDate;
        private String eloResetMethod;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }

        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }

        public String getEloResetMethod() { return eloResetMethod; }
        public void setEloResetMethod(String eloResetMethod) { this.eloResetMethod = eloResetMethod; }
    }
}