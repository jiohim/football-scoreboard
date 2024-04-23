package com.sportradar.scoreboard;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrentScoreboard implements Scoreboard<ConcurrentMatch> {

    private CopyOnWriteArrayList<ConcurrentMatch> matches;

    public ConcurrentScoreboard() {
        matches = new CopyOnWriteArrayList<>();
    }

    public synchronized void startMatch(String homeTeam, String awayTeam) {
        if (homeTeam == null || awayTeam == null || homeTeam.isEmpty() || awayTeam.isEmpty()) {
            throw new IllegalArgumentException("Team names cannot be null or empty");
        }
        for (ConcurrentMatch match : matches) {
            if (match.containsTeams(homeTeam, awayTeam)) {
                throw new IllegalArgumentException("Match between these teams is already in progress");
            }
        }
        matches.add(new ConcurrentMatch(homeTeam, awayTeam));
    }


    public synchronized void updateScore(ConcurrentMatch match, int homeScore, int awayScore) {
        match.updateScore(homeScore, awayScore);
    }

    public synchronized void finishMatch(ConcurrentMatch match) {
        if (!matches.contains(match)) {
            throw new IllegalArgumentException("Match not found in scoreboard");
        }
        matches.remove(match);
    }

    public synchronized List<ConcurrentMatch> getMatchesOrderedByScore() {
        matches.sort(Comparator.comparingInt(ConcurrentMatch::getTotalScore)
                .reversed()
                .thenComparing(Comparator.comparing(matches::indexOf)));
        return matches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConcurrentScoreboard that = (ConcurrentScoreboard) o;

        return Objects.equals(matches, that.matches);
    }

    @Override
    public int hashCode() {
        return matches != null ? matches.hashCode() : 0;
    }
}

