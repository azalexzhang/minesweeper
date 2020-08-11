package ui.graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observer;

// Represents the graphics of a square on the board.
public class GridSquare extends JComponent {
    public static final int SQUARE_SIZE = 32;

    // image fields
    private static BufferedImage coveredImage = null;
    private static BufferedImage flaggedImage = null;
    private static BufferedImage mineImage = null;
    private static BufferedImage[] uncoveredImages = new BufferedImage[9];

    // Initialize image fields with their corresponding images
    static {
        try {
            flaggedImage = ImageIO.read(new File("./data/images/flagged.png"));
            coveredImage = ImageIO.read(new File("./data/images/covered.png"));
            mineImage = ImageIO.read(new File("./data/images/mine.png"));

            for (int i = 0; i < uncoveredImages.length; i++) {
                uncoveredImages[i] = ImageIO.read(new File("./data/images/uncovered" + i + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int xx;
    private int yy;

    private int adjacentMines;
    private boolean covered;
    private boolean containsMine;
    private boolean flagged;

    // EFFECTS: constructs a covered, un-flagged square in the grid with x-coordinate, y-coordinate, number of
    //          adjacent mines and status of whether or not it contains a mine
    public GridSquare(int x, int y, int adjacentMines, boolean containsMine, Observer observer) {
        this.xx = x;
        this.yy = y;
        this.adjacentMines = adjacentMines;
        this.containsMine = containsMine;
        this.covered = true;
        this.flagged = false;
        addMouseListener(new Listener(x, y, observer));
    }

    // EFFECTS: draws the image for each square based on their status
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(selectImage(), 0, 0, null);
    }

    // EFFECTS: returns image based on square's status
    public Image selectImage() {
        if (covered) {
            if (flagged) {
                return flaggedImage;
            } else {
                return coveredImage;
            }
        } else if (containsMine) {
            return mineImage;
        } else {
            return uncoveredImages[adjacentMines];
        }
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
}
