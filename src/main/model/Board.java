package model;

import persistence.Writer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static persistence.Reader.readColumn;
import static persistence.Reader.readDimensionsAndTimeData;

// Represents a Minesweeper board.
public class Board {
    public static final int X_DIMENSION = 24;
    public static final int Y_DIMENSION = 24;
    public static final int NUMBER_OF_MINES = 100;
    public static final String DIMENSIONS_TIME_FILE = "./data/save/dimensionsAndTime.txt";
    public static final String COLUMN_FILE = "./data/save/column";
    public static final String DELIMITER = ",";

    private ArrayList<ArrayList<Square>> board; // Each ArrayList<Square> within the ArrayList
                                                // represents a column.

    // EFFECTS: constructs a board in the form of an ArrayList of ArrayLists
    public Board() {
        this.board = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: generates new board of dimensions X_DIMENSION by Y_DIMENSION
    public void generateBoard() {
        Random random = new Random();

        for (int j = 0; j < X_DIMENSION; j++) {
            ArrayList<Square> arr = new ArrayList<>();
            for (int k = 0; k < Y_DIMENSION; k++) {
                arr.add(new Square(false, true, false));
            }

            board.add(arr);
        }

        for (int i = 1; i <= NUMBER_OF_MINES; i++) {
            int x = random.nextInt(X_DIMENSION);
            int y = random.nextInt(Y_DIMENSION);
            while (this.getSquareByCoordinates(x, y).getContainsMine()) {
                x = random.nextInt(X_DIMENSION);
                y = random.nextInt(Y_DIMENSION);
            }
            this.getSquareByCoordinates(x, y).setContainsMine();
        }
    }

    // EFFECTS: saves the current board to file
    public void saveBoard(double timeElapsedSoFar) throws IOException {
        Writer writer = new Writer(new File(DIMENSIONS_TIME_FILE));

        writer.write(X_DIMENSION);
        writer.write(Y_DIMENSION);
        writer.write(timeElapsedSoFar);
        writer.close();

        for (ArrayList<Square> column : board) {
            writer = new Writer(new File(COLUMN_FILE + board.indexOf(column) + ".txt"));
            for (Square square : column) {
                String containsMine = String.valueOf(square.getContainsMine());
                String covered = String.valueOf(square.getCoveredStatus());
                String flagged = String.valueOf(square.getFlaggedStatus());
                writer.write(containsMine + DELIMITER + covered + DELIMITER + flagged);
            }
            writer.close();
        }
    }

    // EFFECTS: loads board from file
    public double loadBoard() throws IOException {
        File file = new File(DIMENSIONS_TIME_FILE);

        if (file.exists()) {
            List<String> dimensionsAndTimeData = readDimensionsAndTimeData(file);
            int x = (int) Double.parseDouble(dimensionsAndTimeData.get(0));
            double timeSoFar = Double.parseDouble(dimensionsAndTimeData.get(2));
            file.delete();

            for (int i = 0; i < x; i++) {
                ArrayList<Square> column = new ArrayList<>();
                file = new File(COLUMN_FILE + i + ".txt");

                if (file.exists()) {
                    ArrayList<String> savedColumn = readColumn(file);

                    for (String s : savedColumn) {
                        Square square = new Square(splitString(s).get(0), splitString(s).get(1),
                                splitString(s).get(2));
                        column.add(square);
                    }

                    board.add(column);
                    file.delete();
                }
            }

            return timeSoFar;
        }

        return 0;
    }

    // EFFECTS: returns a list of booleans parsed from strings obtained by splitting line on DELIMITER
    private ArrayList<Boolean> splitString(String line) {
        String[] splits = line.split(DELIMITER);
        ArrayList<Boolean> booleans = new ArrayList<>();

        for (String s : splits) {
            booleans.add(Boolean.parseBoolean(s));
        }

        return booleans;
    }

    // MODIFIES: this
    // EFFECTS: if selected square is covered, uncover it. If square has no adjacent mines, uncovers all
    //          surrounding squares that are still covered.
    public void uncoverSquare(int x, int y) {
        if (this.getCoveredStatusByCoordinates(x, y) && !this.getFlaggedStatusByCoordinates(x, y)) {
            this.getSquareByCoordinates(x, y).setCoveredStatus();

            if (this.allAdjacentSquaresNoMine(x, y) && !this.getMineStatusByCoordinates(x, y)) {
                this.uncoverSurroundingSquares(x, y);
            }
        }
    }

    // EFFECTS: uncovers all squares surrounding the one given by its coordinates
    public void uncoverSurroundingSquares(int x, int y) {
        // loop over all adjacent squares
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int nextX = x + dx;
                int nextY = y + dy;
                if (nextX < 0 || nextX >= X_DIMENSION) {
                    continue;
                }
                if (nextY < 0 || nextY >= Y_DIMENSION) {
                    continue;
                }
                if (board.get(nextX).get(nextY).covered) {
                    this.uncoverSquare(nextX, nextY);
                }
            }
        }
    }

    // EFFECTS: returns true if all surrounding squares around the square at the given coordinates have
    //          no mines
    public boolean allAdjacentSquaresNoMine(int x, int y) {
        // similar for loop as uncoverSurroundingSquares
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int nextX = x + dx;
                int nextY = y + dy;
                if (nextX < 0 || nextX >= X_DIMENSION) {
                    continue;
                }
                if (nextY < 0 || nextY >= Y_DIMENSION) {
                    continue;
                }
                if (board.get(nextX).get(nextY).containsMine) {
                    return false;
                }
            }
        }

        return true;
    }

    // EFFECTS: returns the number of surrounding mines
    public int getSurroundingMines(int x, int y) {
        int mines = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int nextX = x + dx;
                int nextY = y + dy;
                if (nextX < 0 || nextX >= X_DIMENSION) {
                    continue;
                }
                if (nextY < 0 || nextY >= Y_DIMENSION) {
                    continue;
                }
                if (board.get(nextX).get(nextY).containsMine) {
                    mines++;
                }
            }
        }

        return mines;
    }

    // MODIFIES: this
    // EFFECTS: flags selected square to indicate mine location
    public void flagSquare(int x, int y) {
        if (this.getCoveredStatusByCoordinates(x, y)) {
            this.getSquareByCoordinates(x, y).setFlaggedStatus(!this.getFlaggedStatusByCoordinates(x, y));
        }
    }

    // EFFECTS: checks board to see if all squares (besides the ones containing mines) are uncovered
    public boolean gameWon() {
        int uncovered = 0;
        for (int j = Y_DIMENSION - 1; j >= 0; j--) {
            for (int i = 0; i < X_DIMENSION; i++) {
                if (!this.getCoveredStatusByCoordinates(i, j)) {
                    if (this.getMineStatusByCoordinates(i, j)) {
                        return false;
                    } else {
                        uncovered++;
                    }
                }
            }
        }

        return uncovered == X_DIMENSION * Y_DIMENSION - NUMBER_OF_MINES;
    }

    // EFFECTS: checks board to see if any square containing a mine is uncovered
    public boolean gameLost() {
        for (int i = 0; i < X_DIMENSION; i++) {
            for (int j = 0; j < Y_DIMENSION; j++) {
                if (this.getMineStatusByCoordinates(i, j) && !this.getCoveredStatusByCoordinates(i, j)) {
                    return true;
                }
            }
        }

        return false;
    }

    // EFFECTS: returns x-dimension of board
    public int getXDimension() {
        return board.size();
    }

    // EFFECTS: returns y-dimension of board
    public int getYDimension() {
        return board.get(0).size();
    }

    // EFFECTS: returns the square with the specified coordinates
    public Square getSquareByCoordinates(int x, int y) {
        return board.get(x).get(y);
    }

    // EFFECTS: returns true if square with given coordinates contains a mine; false otherwise
    public boolean getMineStatusByCoordinates(int x, int y) {
        return this.getSquareByCoordinates(x, y).getContainsMine();
    }

    // EFFECTS: returns true if square with given coordinates is covered; false otherwise
    public boolean getCoveredStatusByCoordinates(int x, int y) {
        return this.getSquareByCoordinates(x, y).getCoveredStatus();
    }

    // EFFECTS: returns true if square with given coordinates is flagged; false otherwise
    public boolean getFlaggedStatusByCoordinates(int x, int y) {
        return this.getSquareByCoordinates(x, y).getFlaggedStatus();
    }

    // Represents an individual square on the board.
    private class Square {
        boolean containsMine;
        boolean covered;
        boolean flagged;

        // EFFECTS: constructs a square with statuses checking if it contains a mine, is covered, and is
        //          flagged
        private Square(boolean mine, boolean cover, boolean flag) {
            this.containsMine = mine;
            this.covered = cover;
            this.flagged = flag;
        }

        private boolean getContainsMine() {
            return containsMine;
        }

        private boolean getCoveredStatus() {
            return covered;
        }

        private boolean getFlaggedStatus() {
            return flagged;
        }

        private void setCoveredStatus() {
            this.covered = false;
        }

        private void setFlaggedStatus(boolean flaggedStatus) {
            this.flagged = flaggedStatus;
        }

        private void setContainsMine() {
            this.containsMine = true;
        }
    }
}