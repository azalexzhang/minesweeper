package model;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;

// Represents a leaderboard with high scores
public class Leaderboard {
    private static final String LEADERBOARD_FILE = "./data/leaderboard.txt";
    private static final int MAX_LEADERBOARD_SIZE = 10;

    private ArrayList<Long> scores;

    public Leaderboard() {
        scores = new ArrayList<>();
    }

    public int getLeaderboardIndex(ArrayList<Long> scores, long timeElapsed) {
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
    // ** I took a Java programming related class in grade 12, and this method uses similar mechanisms to
    // what I was taught in that class. An example here:
    // https://repl.it/@AlexZhang/WritingDatatoaFile#Main.java
    public void addScoreToLeaderboard(long timeElapsed) throws Exception {
        FileReader file = new FileReader(LEADERBOARD_FILE);
        BufferedReader buffer = new BufferedReader(file);
        String scoreEntry = buffer.readLine();

        while (scoreEntry != null) {
            scores.add(Long.parseLong(scoreEntry));
            scoreEntry = buffer.readLine();
        }

        buffer.close();
        file.close();

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
            FileWriter file = new FileWriter(LEADERBOARD_FILE);
            BufferedWriter buffer = new BufferedWriter(file);

            for (long s : scores) {
                buffer.write(String.valueOf(s), 0, String.valueOf(s).length());
                buffer.newLine();
            }

            buffer.close();
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: displays current leaderboard
    public void viewLeaderboard() {
        try {
            FileReader file = new FileReader(LEADERBOARD_FILE);
            BufferedReader buffer = new BufferedReader(file);
            String scoreEntry = buffer.readLine();

            while (scoreEntry != null) {
                scores.add(Long.parseLong(scoreEntry));
                scoreEntry = buffer.readLine();
            }

            System.out.println("\nHIGH SCORES");
            for (long s : scores) {
                System.out.println((scores.indexOf(s) + 1) + ". " + s + " seconds");
            }

            buffer.close();
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
