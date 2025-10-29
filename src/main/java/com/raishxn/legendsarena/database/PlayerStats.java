package com.raishxn.legendsarena.database;

import java.util.UUID;

/**
 * Esta classe (POJO) serve como um contêiner para transportar os dados
 * das estatísticas de um jogador de forma organizada pelo código.
 */
public class PlayerStats {
    private UUID playerUuid;
    private String playerName;
    private String tier;
    private int season;
    private int elo;
    private int wins;
    private int losses;
    private int winstreak;
    private int matchesPlayed;
    private int bestWinstreak;

    // --- Getters ---
    public UUID getPlayerUuid() { return playerUuid; }
    public String getPlayerName() { return playerName; }
    public String getTier() { return tier; }
    public int getSeason() { return season; }
    public int getElo() { return elo; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getWinstreak() { return winstreak; }
    public int getMatchesPlayed() { return matchesPlayed; }
    public int getBestWinstreak() { return bestWinstreak; }

    // --- Setters ---
    public void setPlayerUuid(UUID playerUuid) { this.playerUuid = playerUuid; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setTier(String tier) { this.tier = tier; }
    public void setSeason(int season) { this.season = season; }
    public void setElo(int elo) { this.elo = elo; }
    public void setWins(int wins) { this.wins = wins; }
    public void setLosses(int losses) { this.losses = losses; }
    public void setWinstreak(int winstreak) { this.winstreak = winstreak; }
    public void setMatchesPlayed(int matchesPlayed) { this.matchesPlayed = matchesPlayed; }
    public void setBestWinstreak(int bestWinstreak) { this.bestWinstreak = bestWinstreak; }
}