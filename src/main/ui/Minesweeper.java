package ui;

import model.Board;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static model.Board.*;

// Stores data about the current game being played. The design of this UI class is relatively similar to
// the TellerApp program.
public class Minesweeper {
    private static final String LEADERBOARD_FILE = "./data/leaderboard.txt";
    private static final int MAX_LEADERBOARD_SIZE = 50;
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
        String in;

        while (keepGoing) {
            displayInstructions();
            displayMenu();
            in = input.next();

            if (in.equalsIgnoreCase("q")) {
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
                + "\nFor example, the square on the bottom left corner has coordinates (0, 0).");
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
            runGame();
        } else if (in.equalsIgnoreCase("L")) {
            viewLeaderboard();
        } else {
            System.out.println("Invalid input, please try again.\n");
        }
    }

    // MODIFIES: this
    // EFFECTS: runs a Minesweeper game
    private void runGame() {
        board = new Board();
        boolean gameOver = false;
        long start = System.nanoTime();

        generateNewBoard();

        while (!gameOver) {
            displayBoard();
            System.out.println("Press 1 to uncover and 2 to flag.");
            String uf = input.next();
            System.out.print("Enter the x- and y-coordinate of the square you want to modify: ");
            int xxCoord = Integer.parseInt(input.next());
            int yyCoord = Integer.parseInt(input.next());
            gameOver = modifySelectedSquare(uf, xxCoord, yyCoord);
        }

        long finish = System.nanoTime();
        long timeElapsed = (finish - start) / 1000000000;

        if (gameWon()) {
            System.out.println("Your time is: " + timeElapsed + " seconds");
            try {
                addScoreToLeaderboard(timeElapsed);
            } catch (Exception e) {
                System.err.println("Encountered exception: " + e.getMessage());
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: generates a new board
    private void generateNewBoard() {
        try {
            board.generateBoard();
        } catch (Exception e) {
            System.out.println("An error occurred while generating board: The number of mines on the board"
                    + " generated was invalid.");
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
            System.out.print("* ");
        } else {
            System.out.print(board.getSurroundingMines(x, y) + " ");
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

    private int getLeaderboardIndex(ArrayList<Long> scores, long timeElapsed) {
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
    private void addScoreToLeaderboard(long timeElapsed) throws Exception {
        FileReader file = new FileReader(LEADERBOARD_FILE);
        BufferedReader buffer = new BufferedReader(file);
        ArrayList<Long> scores = new ArrayList<>();
        String scoreEntry = buffer.readLine();
        while (scoreEntry != null) {
            scores.add(Long.parseLong(scoreEntry));
            scoreEntry = buffer.readLine();
        }
        buffer.close();
        file.close();
        int index = getLeaderboardIndex(scores, timeElapsed);
        if (index != -1) {
            scores.add(index, timeElapsed);
            if (scores.size() > MAX_LEADERBOARD_SIZE) {
                scores.remove(scores.size() - 1);
            }
            writeScoresToFile(scores);
        }
    }

    // EFFECTS: writes all leaderboard data back to the file that stores them
    private void writeScoresToFile(ArrayList<Long> scores) {
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
            System.out.println("FileNotFoundException: ");
        } catch (IOException e) {
            System.out.println("IOException: ");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: displays current leaderboard
    private void viewLeaderboard() {
        try {
            FileReader file = new FileReader(LEADERBOARD_FILE);
            BufferedReader buffer = new BufferedReader(file);
            ArrayList<String> scores = new ArrayList<>();
            String scoreEntry = "";

            while (scoreEntry != null) {
                scores.add(scoreEntry);
                scoreEntry = buffer.readLine();
            }

            for (String s : scores) {
                System.out.println(s);
            }

            buffer.close();
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: ");
        } catch (IOException e) {
            System.out.println("IOException: ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
