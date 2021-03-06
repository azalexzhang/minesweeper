package model;

import persistence.Writer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static persistence.Reader.readLeaderboard;

// Represents a leaderboard with high scores
public class Leaderboard {
    public static final String LEADERBOARD_FILE_EASY = "./data/leaderboard/leaderboardEasy.txt";
    public static final String LEADERBOARD_FILE_MEDIUM = "./data/leaderboard/leaderboardMedium.txt";
    public static final String LEADERBOARD_FILE_HARD = "./data/leaderboard/leaderboardHard.txt";

    private static final int MAX_LEADERBOARD_SIZE = 10;

    private List<Double> scores;

    public Leaderboard() {
        scores = new ArrayList<>();
    }

    // EFFECTS: gets the index in the leaderboard where the new score should be added;
    //          if not a new high score, returns -1
    public int getLeaderboardIndex(List<Double> scores, double timeElapsed) {
        for (double s : scores) {
            if (timeElapsed < s) {
                return scores.indexOf(s);
            }
        }
        if (scores.size() < MAX_LEADERBOARD_SIZE) {
            return scores.size();
        }

        return -1;
    }

    // EFFECTS: adds score (in time elapsed) to leaderboard if the time is low enough
    public void addScoreToLeaderboard(double timeElapsed, String leaderboardFile) throws IOException {
        scores = readLeaderboard(new File(leaderboardFile));

        int index = getLeaderboardIndex(scores, timeElapsed);

        if (index != -1) {
            scores.add(index, timeElapsed);
            if (scores.size() > MAX_LEADERBOARD_SIZE) {
                scores.remove(scores.size() - 1);
            }
            writeScoresToFile(scores, leaderboardFile);
        }
    }

    // EFFECTS: writes all leaderboard data back to the file that stores them
    public void writeScoresToFile(List<Double> scores, String leaderboardFile) throws IOException {
        Writer writer = new Writer(new File(leaderboardFile));

        for (double s : scores) {
            writer.write(s);
        }

        writer.close();
    }

    // EFFECTS: returns leaderboard scores
    public List<Double> getLeaderboard() {
        return scores;
    }
}
