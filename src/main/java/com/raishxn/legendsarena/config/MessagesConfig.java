package com.raishxn.legendsarena.config;

public class MessagesConfig {

    private GeneralMessages general;
    private ErrorMessages errors;
    private MatchMessages match;

    public GeneralMessages getGeneral() { return general; }
    public void setGeneral(GeneralMessages general) { this.general = general; }

    public ErrorMessages getErrors() { return errors; }
    public void setErrors(ErrorMessages errors) { this.errors = errors; }

    public MatchMessages getMatch() { return match; }
    public void setMatch(MatchMessages match) { this.match = match; }

    public static class GeneralMessages {
        private String pluginPrefix;
        private String queueJoin;
        private String queueLeave;

        public String getPluginPrefix() { return pluginPrefix; }
        public void setPluginPrefix(String pluginPrefix) { this.pluginPrefix = pluginPrefix; }
        public String getQueueJoin() { return queueJoin; }
        public void setQueueJoin(String queueJoin) { this.queueJoin = queueJoin; }
        public String getQueueLeave() { return queueLeave; }
        public void setQueueLeave(String queueLeave) { this.queueLeave = queueLeave; }
    }

    public static class ErrorMessages {
        private String alreadyInQueue;
        private String notInQueue;
        private String tierNotFound;
        private String noPermissionTier;

        public String getAlreadyInQueue() { return alreadyInQueue; }
        public void setAlreadyInQueue(String alreadyInQueue) { this.alreadyInQueue = alreadyInQueue; }
        public String getNotInQueue() { return notInQueue; }
        public void setNotInQueue(String notInQueue) { this.notInQueue = notInQueue; }
        public String getTierNotFound() { return tierNotFound; }
        public void setTierNotFound(String tierNotFound) { this.tierNotFound = tierNotFound; }
        public String getNoPermissionTier() { return noPermissionTier; }
        public void setNoPermissionTier(String noPermissionTier) { this.noPermissionTier = noPermissionTier; }
    }

    public static class MatchMessages {
        private String matchFound;
        private String eloChange;

        public String getMatchFound() { return matchFound; }
        public void setMatchFound(String matchFound) { this.matchFound = matchFound; }
        public String getEloChange() { return eloChange; }
        public void setEloChange(String eloChange) { this.eloChange = eloChange; }
    }
}