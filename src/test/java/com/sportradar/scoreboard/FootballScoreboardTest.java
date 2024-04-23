package com.sportradar.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class ScoreboardTest {

    private FootballScoreboard scoreboard;

    @BeforeEach
    public void setUp() {
        scoreboard = new FootballScoreboard();
    }

    @Test
    public void testStartMatch() {
        scoreboard.startMatch("Mexico", "Canada");
        List<FootballMatch> matches = scoreboard.getMatchesOrderedByScore();
        assertEquals(1, matches.size());
        assertEquals("Mexico", matches.get(0).getHomeTeam());
        assertEquals("Canada", matches.get(0).getAwayTeam());
        assertEquals(0, matches.get(0).getHomeScore());
        assertEquals(0, matches.get(0).getAwayScore());
    }

    @Test
    public void testStartMatchWithTeamAlreadyInvolved() {
        scoreboard.startMatch("Mexico", "Canada");

        assertThrows(IllegalArgumentException.class, () -> scoreboard.startMatch("Spain", "Canada"));
        assertThrows(IllegalArgumentException.class, () -> scoreboard.startMatch("Italy", "Mexico"));
        assertThrows(IllegalArgumentException.class, () -> scoreboard.startMatch("Canada", "Mexico"));
    }

    @Test
    public void testStartMatchWithNullTeams() {
        assertThrows(IllegalArgumentException.class, () -> scoreboard.startMatch(null, "Canada"));
        assertThrows(IllegalArgumentException.class, () -> scoreboard.startMatch("Mexico", null));
        assertThrows(IllegalArgumentException.class, () -> scoreboard.startMatch(null, null));
    }

    @Test
    public void testUpdateScore() {
        scoreboard.startMatch("Mexico", "Canada");
        List<FootballMatch> matches = scoreboard.getMatchesOrderedByScore();
        scoreboard.updateScoreboard(matches.get(0), 3, 1);
        assertEquals(3, matches.get(0).getHomeScore());
        assertEquals(1, matches.get(0).getAwayScore());
    }

    @Test
    public void testFinishMatch() {
        scoreboard.startMatch("Mexico", "Canada");
        Deque<FootballMatch> matches = scoreboard.getAllMatches();
        scoreboard.finishMatch(matches.getLast());
        assertEquals(0, matches.size());
    }

    @Test
    public void testFinishMatchWithNonExistingMatch() {
        FootballMatch match = new FootballMatch("Team A", "Team B");
        assertThrows(IllegalArgumentException.class, () -> scoreboard.finishMatch(match));
    }

    @Test
    public void testGetMatchesOrderedByScore() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.startMatch("Germany", "France");
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.startMatch("Argentina", "Australia");

        List<FootballMatch> matches = scoreboard.getMatchesOrderedByScore();

        scoreboard.updateScoreboard(matches.get(4), 0, 5);
        scoreboard.updateScoreboard(matches.get(3), 10, 2);
        scoreboard.updateScoreboard(matches.get(2), 2, 2);
        scoreboard.updateScoreboard(matches.get(1), 6, 6);
        scoreboard.updateScoreboard(matches.get(0), 3, 1);

        List<FootballMatch> summary = scoreboard.getMatchesOrderedByScore();
        List<FootballMatch> anotherSummary = scoreboard.getMatchesOrderedByScore();

        //Checking for idempotent
        assertEquals(summary , anotherSummary);

        assertEquals("Uruguay", summary.get(0).getHomeTeam());
        assertEquals("Italy", summary.get(0).getAwayTeam());
        assertEquals(6, summary.get(0).getHomeScore());
        assertEquals(6, summary.get(0).getAwayScore());

        assertEquals("Spain", summary.get(1).getHomeTeam());
        assertEquals("Brazil", summary.get(1).getAwayTeam());
        assertEquals(10, summary.get(1).getHomeScore());
        assertEquals(2, summary.get(1).getAwayScore());

        assertEquals("Mexico", summary.get(2).getHomeTeam());
        assertEquals("Canada", summary.get(2).getAwayTeam());
        assertEquals(0, summary.get(2).getHomeScore());
        assertEquals(5, summary.get(2).getAwayScore());

        assertEquals("Argentina", summary.get(3).getHomeTeam());
        assertEquals("Australia", summary.get(3).getAwayTeam());
        assertEquals(3, summary.get(3).getHomeScore());
        assertEquals(1, summary.get(3).getAwayScore());

        assertEquals("Germany", summary.get(4).getHomeTeam());
        assertEquals("France", summary.get(4).getAwayTeam());
        assertEquals(2, summary.get(4).getHomeScore());
        assertEquals(2, summary.get(4).getAwayScore());
    }

    @Test
    public void testGetMatchesOrderedByScoreIsEmpty() {
        assertTrue(scoreboard.getMatchesOrderedByScore().isEmpty());
    }

    /**
     * Test case for concurrent access to updating match scores from multiple threads.
     *
     * @throws InterruptedException if any thread is interrupted
     */
    @Test
    public void testConcurrentAccess() throws InterruptedException {
        FootballScoreboard scoreboard = new FootballScoreboard();
        Thread thread1 = new Thread(() -> {
            FootballMatch match = scoreboard.startMatch("Mexico", "Canada");
            for (int i = 0; i < 1000; i++) {
                scoreboard.updateScoreboard(match, match.getHomeScore() + 1, match.getAwayScore() + 1);
            }
        });
        Thread thread2 = new Thread(() -> {
            FootballMatch match = scoreboard.startMatch("Team C", "Team D");
            for (int i = 0; i < 1000; i++) {
                scoreboard.updateScoreboard(match, match.getHomeScore() + 1, match.getAwayScore() + 1);
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertEquals(1000, scoreboard.getMatchesOrderedByScore().get(0).getHomeScore());
        assertEquals(1000, scoreboard.getMatchesOrderedByScore().get(1).getHomeScore());
        assertEquals(1000, scoreboard.getMatchesOrderedByScore().get(0).getAwayScore());
        assertEquals(1000, scoreboard.getMatchesOrderedByScore().get(1).getAwayScore());
    }
}