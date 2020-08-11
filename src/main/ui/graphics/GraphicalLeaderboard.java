package ui.graphics;

import persistence.Reader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static model.Leaderboard.LEADERBOARD_FILE;

public class GraphicalLeaderboard extends JTextArea {
    public GraphicalLeaderboard() {
        setEditable(false);

        try {
            List<Long> scores = Reader.readLeaderboard(new File(LEADERBOARD_FILE));
            this.append("HIGH SCORES\n");
            for (long s : scores) {
                String text = Long.toString(s);
                this.append((scores.indexOf(s) + 1) + ". " + text + " seconds\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}