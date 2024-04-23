package com.sportradar.scoreboard;


import java.util.List;

public interface Scoreboard<T extends Match> extends Board{

    FootballMatch startMatch(String homeTeam, String awayTeam);

    void updateScoreboard(T match, int homeScore, int awayScore);

    List<FootballMatch> getMatchesOrderedByScore();

    void finishMatch(T match);
}
