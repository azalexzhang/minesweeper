package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class LeaderboardTest {
    private Leaderboard leaderboard;
    private List<Double> testScores;

    @BeforeEach
    void runBefore() {
        leaderboard = new Leaderboard();
        testScores = new ArrayList<>();
    }

    @Test
    void testAddScoreToLeaderboardNotFull() {
        try {
            leaderboard.addScoreToLeaderboard(80,
                    "./data/testAddScoreToLeaderboardNotFull.txt");
            testScores = leaderboard.getLeaderboard();

            List<Long> expected = new ArrayList<>();
            expected.add((long) 60);
            expected.add((long) 61);
            expected.add((long) 65);
            expected.add((long) 73);
            expected.add((long) 78);
            expected.add((long) 80);
            assertEquals(expected, testScores);

            testScores = new ArrayList<>();
            leaderboard.addScoreToLeaderboard(61,
                    "./data/testAddScoreToLeaderboardNotFull.txt");
            testScores = leaderboard.getLeaderboard();
        } catch (IOException e) {
            fail("Unexpected IOException\n" + e);
        }
    }

    @Test
    void testAddScoreToLeaderboardFull() {
        long testScore = 70;

        try {
            leaderboard.addScoreToLeaderboard(testScore,
                    "./data/testAddScoreToLeaderboardFull.txt");
            testScores = leaderboard.getLeaderboard();
        } catch (IOException e) {
            fail("Unexpected IOException\n" + e);
        }

        List<Long> expected = new ArrayList<>();
        expected.add((long) 48);
        expected.add((long) 54);
        expected.add((long) 61);
        expected.add((long) 62);
        expected.add((long) 69);
        expected.add((long) 70);
        expected.add((long) 73);
        expected.add((long) 81);
        expected.add((long) 82);
        expected.add((long) 84);
        assertEquals(expected, testScores);
    }
}