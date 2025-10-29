package com.raishxn.legendsarena.database;

import java.util.UUID;

public class PlayerStats {

    private final UUID playerUuid;
    private final String playerName;
    private final String queueId; // <-- CAMPO ADICIONADO
    private int elo;
    private int wins;
    private int losses;
    private int winstreak;
    private int matchesPlayed;
    private final int season;

    // CONSTRUTOR ATUALIZADO
    public PlayerStats(UUID playerUuid, String playerName, String queueId, int elo, int wins, int losses, int winstreak, int matchesPlayed, int season) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.queueId = queueId;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
        this.winstreak = winstreak;
        this.matchesPlayed = matchesPlayed;
        this.season = season;
    }

    // GETTERS E SETTERS
    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    // GETTER ADICIONADO
    public String getQueueId() {
        return queueId;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getWinstreak() {
        return winstreak;
    }

    public void setWinstreak(int winstreak) {
        this.winstreak = winstreak;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getSeason() {
        return season;
    }
}