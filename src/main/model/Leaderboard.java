package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import persistence.Saveable;
import persistence.Writer;

import static persistence.Reader.readLeaderboard;

// Represents a leaderboard with high scores
public class Leaderboard {
    private static final String LEADERBOARD_FILE = "./data/leaderboard.txt";
    private static final int MAX_LEADERBOARD_SIZE = 10;

    private List<Long> scores;

    public Leaderboard() {
        scores = new ArrayList<>();
    }

    // EFFECTS: gets the index in the leaderboard where the new score should be added;
    //          if not a new high score, returns -1
    public int getLeaderboardIndex(List<Long> scores, long timeElapsed) {
        for (long s : scores) {
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
    public void addScoreToLeaderboard(long timeElapsed) {
        try {
            scores = readLeaderboard(new File(LEADERBOARD_FILE));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int index = getLeaderboardIndex(scores, timeElapsed);

        if (index != -1) {
            System.out.println("NEW HIGH SCORE!");
            scores.add(index, timeElapsed);
            if (scores.size() > MAX_LEADERBOARD_SIZE) {
                scores.remove(scores.size() - 1);
            }
            writeScoresToFile();
        }
    }

    // EFFECTS: writes all leaderboard data back to the file that stores them
    public void writeScoresToFile() {
        try {
            Writer writer = new Writer(new File(LEADERBOARD_FILE));

            for (long s : scores) {
                writer.write(s);
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: returns leaderboard scores
    public List<Long> getLeaderboard() throws IOException {
        return readLeaderboard(new File(LEADERBOARD_FILE));
    }
}
