package ui;

import model.Board;

import javax.swing.*;

public class GraphicalRunner extends JFrame {
    private GraphicalBoard graphicalBoard;
    private Board board = new Board();

    public static void main(String[] args) {
        new GraphicalRunner();
    }

    // EFFECTS: runs the graphics of the game
    public GraphicalRunner() {
        board.generateBoard();
        graphicalBoard = new GraphicalBoard(board);
        add(graphicalBoard);
        pack();
        setVisible(true);
    }
}
