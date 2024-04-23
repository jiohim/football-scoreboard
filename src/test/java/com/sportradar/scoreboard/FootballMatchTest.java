package com.sportradar.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FootballMatchTest {

    private FootballMatch match;

    @BeforeEach
    public void setUp() {
        match = new FootballMatch("Team A", "Team B");
    }
    @Test
    public void testUpdateScoreWithNegativeScores() {
        assertThrows(IllegalArgumentException.class, () -> match.updateScore(-1, 2));
        assertThrows(IllegalArgumentException.class, () -> match.updateScore(1, -2));
        assertThrows(IllegalArgumentException.class, () -> match.updateScore(-1, -2));
    }

    @Test
    public void testContainsTeams() {
        assertTrue(match.containsTeams("Team A"));
        assertTrue(match.containsTeams("Team B"));
        assertFalse(match.containsTeams("Team C"));
        assertTrue(match.containsTeams("Team A", "Team B"));
        assertTrue(match.containsTeams("Team B", "Team C"));
        assertFalse(match.containsTeams("Team C", "Team D"));
        assertFalse(match.containsTeams("Team C", "Team D" , "Team E"));
    }
}
