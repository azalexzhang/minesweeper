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

            List<Double> expected = new ArrayList<>();
            expected.add(60.0);
            expected.add(61.0);
            expected.add(65.0);
            expected.add(73.0);
            expected.add(78.0);
            expected.add(80.0);
            assertEquals(expected, testScores);
        } catch (IOException e) {
            fail("Unexpected IOException\n" + e);
        }
    }

    @Test
    void testAddScoreToLeaderboardFull() {
        double testScore = 70;

        try {
            leaderboard.addScoreToLeaderboard(testScore,
                    "./data/testAddScoreToLeaderboardFull.txt");
            testScores = leaderboard.getLeaderboard();
        } catch (IOException e) {
            fail("Unexpected IOException\n" + e);
        }

        List<Double> expected = new ArrayList<>();
        expected.add(48.0);
        expected.add(54.0);
        expected.add(61.0);
        expected.add(62.0);
        expected.add(69.0);
        expected.add(70.0);
        expected.add(73.0);
        expected.add(81.0);
        expected.add(82.0);
        expected.add(84.0);
        assertEquals(expected, testScores);
    }
}