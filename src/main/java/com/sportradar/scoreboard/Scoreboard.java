package pl.live.scoreboard;

import java.util.List;

public interface Scoreboard<T extends Match> extends Board{

    void startMatch(String homeTeam, String awayTeam);

    void updateScore(T match, int homeScore, int awayScore);

    List<ConcurrentMatch> getMatchesOrderedByScore();

    void finishMatch(T match);
}
