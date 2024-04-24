package com.sportradar.scoreboard;

import java.util.Objects;

public class FootballMatch implements Match {

    private String homeTeam;
    private String awayTeam;
    private int homeScore;
    private int awayScore;

    public FootballMatch(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = 0;
        this.awayScore = 0;
    }

    public synchronized void updateScore(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public int getTotalScore() {
        return homeScore + awayScore;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    /**
     * Checks if the football match involves any of the specified teams.
     *
     * @param teams The teams to check.
     * @return True if any of the teams is involved in the match, otherwise false.
     */
    public boolean containsTeams(String... teams) {
        for (String team : teams) {
            if (homeTeam.equals(team) || awayTeam.equals(team)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FootballMatch)) return false;
        FootballMatch that = (FootballMatch) o;
        return getHomeScore() == that.getHomeScore() && getAwayScore() == that.getAwayScore()
                && Objects.equals(getHomeTeam(), that.getHomeTeam())
                && Objects.equals(getAwayTeam(), that.getAwayTeam());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHomeTeam(), getAwayTeam(), getHomeScore(), getAwayScore());
    }
}