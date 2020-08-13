package ui.graphics;

import persistence.Reader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static model.Leaderboard.*;

// Represents a graphical leaderboard.
public class GraphicalLeaderboard extends JTextArea {
    // EFFECTS: constructs a graphical leaderboard
    public GraphicalLeaderboard() {
        setFont(getFont().deriveFont(24f));
        setEditable(false);

        this.append("HIGH SCORES\n\nEasy\n");
        addScores(LEADERBOARD_FILE_EASY);
        this.append("Medium\n");
        addScores(LEADERBOARD_FILE_MEDIUM);
        this.append("Hard\n");
        addScores(LEADERBOARD_FILE_HARD);
    }

    // EFFECTS: Appends the high scores of each difficulty level
    private void addScores(String leaderboardFile) {
        try {
            List<Double> scores = Reader.readLeaderboard(new File(leaderboardFile));
            if (scores.size() == 0) {
                this.append("There are no high scores to display for this difficulty mode.");
            } else {
                for (double s : scores) {
                    this.append((scores.indexOf(s) + 1) + ". " + s + " seconds\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
