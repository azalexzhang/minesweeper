package ui;

import model.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static model.Board.X_DIMENSION;
import static model.Board.Y_DIMENSION;

// Displays the board graphics.
public class GraphicalBoard extends JPanel implements Observer {
    private Board board;
    private ArrayList<ArrayList<GridSquare>> grid;

    // EFFECTS: constructs a graphical board
    public GraphicalBoard(Board board) {
        this.grid = new ArrayList<>();
        this.board = board;
        setLayout(null);
        for (int i = 0; i < X_DIMENSION; i++) {
            ArrayList<GridSquare> arr = new ArrayList<>();
            for (int j = 0; j < Y_DIMENSION; j++) {
                GridSquare g = new GridSquare(i,
                        j,
                        board.getSurroundingMines(i, j),
                        board.getMineStatusByCoordinates(i, j),
                        this);
                g.setBounds(i * GridSquare.SQUARE_SIZE,
                        j * GridSquare.SQUARE_SIZE,
                        GridSquare.SQUARE_SIZE,
                        GridSquare.SQUARE_SIZE);
                g.repaint();
                add(g);
                arr.add(g);
            }
            grid.add(arr);
        }
        setPreferredSize(new Dimension(X_DIMENSION * GridSquare.SQUARE_SIZE,
                Y_DIMENSION * GridSquare.SQUARE_SIZE));
    }

    // EFFECTS: updates grid squares with the current state of the board
    private void updateGridSquares() {
        for (int i = 0; i < X_DIMENSION; i++) {
            for (int j = 0; j < Y_DIMENSION; j++) {
                grid.get(i).get(j).setCovered(board.getCoveredStatusByCoordinates(i, j));
                grid.get(i).get(j).setFlagged(board.getFlaggedStatusByCoordinates(i, j));
            }
        }
    }

    // EFFECTS: updates the graphics of the board based on the mouse event received
    @Override
    public void update(Observable o, Object arg) {
        System.err.println("update");
        System.err.println(((MouseEvent) arg).getModifiers());
        System.err.println(((Listener) o).getX() + " " +  ((Listener) o).getY());
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
}

/*
 * each cell is its own component and has their own MouseListener
 * the cells only listen for mouse clicks
 * graphical runner contains the cells and listens to them
 * fires onclick event when mouse is clicked
 * runner updates board
 * runner can also draw board by itself
 * Listener stores coordinates
 */