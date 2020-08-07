package ui;

import model.Board;
import model.Leaderboard;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static model.Board.*;
import static model.Leaderboard.LEADERBOARD_FILE;

// Stores data about the current game being played. The design of this UI class is relatively similar to
// the TellerApp program.
public class Minesweeper extends JPanel implements ActionListener {
    private Leaderboard leaderboard;
    private Board board;
    private Scanner input;

    // EFFECTS: runs the Minesweeper application
    public Minesweeper() {
        runMinesweeper();
    }

    // MODIFIES: this
    // EFFECTS: runs the program and reads user input
    private void runMinesweeper() {
        boolean keepGoing = true;
        input = new Scanner(System.in);
        leaderboard = new Leaderboard();
        String in;

        while (keepGoing) {
            displayInstructions();
            displayMenu();
            in = input.next();

            if (in.equalsIgnoreCase("Q")) {
                keepGoing = false;
            } else {
                processInput(in);
            }
        }

        System.out.println("Thanks for playing!");
    }

    // EFFECTS: prints out set of instructions on how to play
    private void displayInstructions() {
        System.out.println("\nMINESWEEPER\n");
        System.out.println("- The goal of Minesweeper is to clear a board with individual squares, without"
                + " touching any mines.");
        System.out.println("- You can either select a square to uncover it, or flag it to indicate the"
                + " location of a mine.");
        System.out.println("- You must enter the coordinates of the square you want to uncover or flag."
                + "\nFor example, the square on the BOTTOM LEFT corner has coordinates (0, 0).");
        System.out.println("- If you manage to clear all squares that don't hold a mine, you win the game."
                + "\nIf you uncover a square that holds a mine, however, the game is over!\n");
    }

    // EFFECTS: displays menu of options
    private void displayMenu() {
        System.out.println("Enter P to start game.");
        System.out.println("Enter L to view high scores.");
        System.out.println("Enter Q to quit.");
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void processInput(String in) {
        if (in.equalsIgnoreCase("P")) {
            try {
                runGame();
            } catch (IOException e) {
                System.out.println("ERROR\n" + e);
            }
        } else if (in.equalsIgnoreCase("L")) {
            viewLeaderboard();
        } else {
            System.out.println("Invalid input, please try again.\n");
        }
    }

    // MODIFIES: this
    // EFFECTS: runs a Minesweeper game
    private void runGame() throws IOException {
        board = new Board();
        boolean gameOver = false;
        long start = System.nanoTime();

        generateNewBoard();

        while (!gameOver) {
            gameOver = gameControls();
        }

        long finish = System.nanoTime();
        long timeElapsed = (finish - start) / 1000000000;

        if (gameWon()) {
            System.out.println("Your time is: " + timeElapsed + " seconds");
            leaderboard.addScoreToLeaderboard(timeElapsed, LEADERBOARD_FILE);
        }
    }

    private boolean gameControls() {
        boolean gameOver;
        displayBoard();
        System.out.println("Press 1 to uncover and 2 to flag.");
        String uf = input.next();
        System.out.print("Enter the x-coordinate of the square you want to modify: ");
        int xxCoord = Integer.parseInt(input.next());
        System.out.print("Enter the y-coordinate of the square you want to modify: ");
        int yyCoord = Integer.parseInt(input.next());
        gameOver = modifySelectedSquare(uf, xxCoord, yyCoord);
        return gameOver;
    }

    // MODIFIES: this
    // EFFECTS: generates a new board
    private void generateNewBoard() {
        try {
            board.generateBoard();
        } catch (Exception e) {
            System.out.println("ERROR\n" + e);
        }
    }

    // EFFECTS: displays board by printing a different character for each square depending on its
    //          properties
    private void displayBoard() {
        for (int j = Y_DIMENSION - 1; j >= 0; j--) {
            for (int i = 0; i < X_DIMENSION; i++) {
                printSquareStatus(i, j);
            }
            System.out.print("\n");
        }
    }

    // EFFECTS: determines the character to be printed out based on whether or not the square is uncovered,
    //          the number of mines surrounding it, whether or not it is flagged or whether or not a mine
    //          is revealed
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

    // MODIFIES: this
    // EFFECTS: uncovers or flags given square depending on user input; also returns game over status (true
    //          if a mine is uncovered or if the game has been won, false otherwise)
    private boolean modifySelectedSquare(String uf, int x, int y) {
        if (uf.equals("1")) {
            if (x >= X_DIMENSION || y >= Y_DIMENSION || x < 0 || y < 0) {
                System.out.println("Invalid input, please try again.\n");
            } else {
                board.uncoverSquare(x, y);
                if (board.getMineStatusByCoordinates(x, y)) {
                    displayBoard();
                    System.out.println("You detonated a mine.");
                    System.out.println("GAME OVER\n");
                    return true;
                } else if (gameWon()) {
                    displayBoard();
                    System.out.println("You win!");
                    return true;
                }
            }
        } else if (uf.equals("2")) {
            board.flagSquare(x, y);
        } else {
            System.out.println("Invalid input, please try again.\n");
        }

        return false;
    }

    // EFFECTS: checks board to see if all squares (besides the ones containing mines) are uncovered
    private boolean gameWon() {
        int uncovered = 0;
        for (int j = Y_DIMENSION - 1; j >= 0; j--) {
            for (int i = 0; i < X_DIMENSION; i++) {
                if (!board.getCoveredStatusByCoordinates(i, j)) {
                    if (board.getMineStatusByCoordinates(i, j)) {
                        return false;
                    } else {
                        uncovered++;
                    }
                }
            }
        }
        return uncovered == X_DIMENSION * Y_DIMENSION - NUMBER_OF_MINES;
    }

    // EFFECTS: displays the leaderboard
    private void viewLeaderboard() {
        System.out.println("\nHIGH SCORES");
        List<Long> scores = leaderboard.getLeaderboard();

        for (long s : scores) {
            System.out.println((scores.indexOf(s) + 1) + ". " + s + " seconds");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
