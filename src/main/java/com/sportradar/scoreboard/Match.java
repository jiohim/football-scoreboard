package com.sportradar.scoreboard;

public interface Match {

    String getHomeTeam();

    String getAwayTeam();

    int getHomeScore();

    int getAwayScore();

    void updateScore(int homeScore, int awayScore);

    int getTotalScore();
}