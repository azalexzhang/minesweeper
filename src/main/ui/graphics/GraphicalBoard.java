package ui.graphics;

import model.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

// Displays the board graphics.
public class GraphicalBoard extends JPanel implements Observer {
    private Board board;
    private ArrayList<ArrayList<GridSquare>> grid;
    private int xx;
    private int yy;

    // EFFECTS: constructs a graphical board
    public GraphicalBoard(Board board) {
        this.grid = new ArrayList<>();
        this.board = board;
        this.xx = board.getXDimension();
        this.yy = board.getYDimension();
        setLayout(null);
        setPreferredSize(new Dimension(xx * GridSquare.SQUARE_SIZE,
                yy * GridSquare.SQUARE_SIZE));
    }

    // MODIFIES: this
    // EFFECTS: generates new graphical board
    public void generateGraphicalBoard() {
        for (int i = 0; i < xx; i++) {
            ArrayList<GridSquare> arr = new ArrayList<>();
            for (int j = 0; j < yy; j++) {
                GridSquare g = new GridSquare(i, j, board.getSurroundingMines(i, j),
                        board.getMineStatusByCoordinates(i, j), this);
                g.setBounds(i * GridSquare.SQUARE_SIZE, j * GridSquare.SQUARE_SIZE,
                        GridSquare.SQUARE_SIZE, GridSquare.SQUARE_SIZE);
                g.repaint();
                add(g);
                arr.add(g);
            }
            grid.add(arr);
        }
    }

    // MODIFIES: GridSquare
    // EFFECTS: updates grid squares with the current state of the board
    private void updateGridSquares() {
        for (int i = 0; i < xx; i++) {
            for (int j = 0; j < yy; j++) {
                grid.get(i).get(j).setCovered(board.getCoveredStatusByCoordinates(i, j));
                grid.get(i).get(j).setFlagged(board.getFlaggedStatusByCoordinates(i, j));
            }
        }

        displayBoard(xx, yy);
    }

    // EFFECTS: updates the graphics of the board based on the mouse event received
    @Override
    public void update(Observable o, Object arg) {
        if (SwingUtilities.isLeftMouseButton((MouseEvent) arg)) {
            // update with new uncovered squares
            board.uncoverSquare(((Listener) o).getX(), ((Listener) o).getY());
        } else if (SwingUtilities.isRightMouseButton((MouseEvent) arg)) {
            // update with new flagged square
            board.flagSquare(((Listener) o).getX(), ((Listener) o).getY());
        }

        updateGridSquares();
        repaint();
    }

    private void displayBoard(int x, int y) {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                printSquareStatus(i, j);
            }
            System.out.print("\n");
        }

        System.out.println("\n");
    }

    private void printSquareStatus(int x, int y) {
        if (board.getCoveredStatusByCoordinates(x, y)) {
            if (board.getFlaggedStatusByCoordinates(x, y)) {
                System.out.print("F ");
            } else {
                System.out.print(". ");
            }
        } else if (board.getMineStatusByCoordinates(x, y)) {
            System.out.print("X ");
        } else if (board.getSurroundingMines(x, y) > 0) {
            System.out.print(board.getSurroundingMines(x, y) + " ");
        } else {
            System.out.print("* ");
        }
    }
}