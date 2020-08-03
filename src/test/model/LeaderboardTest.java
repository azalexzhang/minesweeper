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
    private List<Long> testScores;

    @BeforeEach
    void runBefore() {
        leaderboard = new Leaderboard();
        testScores = new ArrayList<>();
    }

    @Test
    void testAddScoreToLeaderboardNotFull() {
        try {
            testScores =
                    leaderboard.getLeaderboard("./data/testAddScoreToLeaderboardNotFull.txt");
            long testScore = 80;
            assertEquals(5, leaderboard.getLeaderboardIndex(testScores, testScore));
        } catch (IOException e) {
            fail("Unexpected IOException\n" + e);
        }
    }
}