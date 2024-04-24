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
        assertFalse(match.containsTeams("Team C", "Team D", "Team E"));
    }

    @Test
    void testEqualsAndHashCode() {

        FootballMatch match2 = new FootballMatch("Team A", "Team B");
        match.updateScore(1, 2);
        match2.updateScore(1, 2);

        assertTrue(match.equals(match2));
        assertTrue(match2.equals(match));
        assertEquals(match.hashCode(), match2.hashCode());

        FootballMatch match3 = new FootballMatch("Team E", "Team F");
        match3.updateScore(3, 1);

        assertFalse(match.equals(match3));
        assertFalse(match3.equals(match));

        assertNotEquals(match.hashCode(), match3.hashCode());
    }
}
