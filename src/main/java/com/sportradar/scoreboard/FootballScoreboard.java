package com.sportradar.scoreboard;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class FootballScoreboard implements Scoreboard<FootballMatch> {

    private ConcurrentLinkedDeque<FootballMatch> matches;

    public FootballScoreboard() {
        matches = new ConcurrentLinkedDeque<>();
    }

    /**
     * Starts a new football match between the specified home and away teams.
     *
     * @param homeTeam The name of the home team.
     * @param awayTeam The name of the away team.
     * @return The newly created FootballMatch object representing match in progress.
     * @throws IllegalArgumentException If either team name is null or empty, or if a team involved in a match already.
     */
    public synchronized FootballMatch startMatch(String homeTeam, String awayTeam) {
        if (homeTeam == null || awayTeam == null || homeTeam.isEmpty() || awayTeam.isEmpty()) {
            throw new IllegalArgumentException("Team names cannot be null or empty");
        }
        for (FootballMatch match : matches) {
            if (match.containsTeams(homeTeam, awayTeam)) {
                throw new IllegalArgumentException("One of the teams is already involved in another match");
            }
        }
        FootballMatch newMatch = new FootballMatch(homeTeam, awayTeam);
        matches.addFirst(newMatch);
        return newMatch;
    }

    /**
     * Updates the score of the specified football match, reordering the matches based on starting from recent.
     *
     * @param match The football match to update.
     * @param homeScore The new home team score.
     * @param awayScore The new away team score.
     */
    public synchronized void updateScoreboard(FootballMatch match, int homeScore, int awayScore) {
        matches.remove(match);
        match.updateScore(homeScore, awayScore);
        matches.addFirst(match);
    }

    public synchronized void finishMatch(FootballMatch match) {
        if (!matches.contains(match)) {
            throw new IllegalArgumentException("Match not found in scoreboard");
        }
        matches.remove(match);
    }

    /**
     * This method creates a new CopyOnWriteArrayList of football matches sorted by total score, from highest to lowest.
     * @return A list of football matches ordered by total score.
     */
    public List<FootballMatch> getMatchesOrderedByScore() {
        CopyOnWriteArrayList<FootballMatch> sortedMatches = new CopyOnWriteArrayList<>(matches);
        return sortedMatches
                .stream()
                .sorted(Comparator
                        .comparingInt(FootballMatch::getTotalScore)
                        .reversed())
                .collect(Collectors.toList());
    }

    public Deque<FootballMatch> getAllMatches() {
        return matches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FootballScoreboard that = (FootballScoreboard) o;

        return Objects.equals(matches, that.matches);
    }

    @Override
    public int hashCode() {
        return matches != null ? matches.hashCode() : 0;
    }
}

