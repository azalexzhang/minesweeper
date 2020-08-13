package ui.graphics;

import persistence.Reader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static model.Leaderboard.LEADERBOARD_FILE_EASY;

public class GraphicalLeaderboard extends JTextArea {
    public GraphicalLeaderboard() {
        setFont(getFont().deriveFont(18f));
        setEditable(false);

        try {
            List<Double> scores = Reader.readLeaderboard(new File(LEADERBOARD_FILE_EASY));
            this.append("HIGH SCORES\n");
            if (scores.size() == 0) {
                this.append("There are no high scores to display.");
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
