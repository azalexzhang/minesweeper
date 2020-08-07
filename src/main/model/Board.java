package model;

import java.util.ArrayList;
import java.util.Random;

// Represents a Minesweeper board.
public class Board {
    public static final int X_DIMENSION = 9;
    public static final int Y_DIMENSION = 9;
    public static final int NUMBER_OF_MINES = 9;

    private final ArrayList<ArrayList<Square>> board; // Each ArrayList<Square> within the ArrayList
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

    // MODIFIES: this
    // EFFECTS: if selected square is covered, uncover it. If square has no adjacent mines, uncovers all
    //          surrounding squares that are still covered.
    public void uncoverSquare(int x, int y) {
        if (this.getCoveredStatusByCoordinates(x, y)) {
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

        // EFFECTS: returns true if square contains a mine; false otherwise
        private boolean getContainsMine() {
            return containsMine;
        }

        // EFFECTS: returns true if square is covered; false otherwise
        private boolean getCoveredStatus() {
            return covered;
        }

        // EFFECTS: returns true if square is flagged; false otherwise
        private boolean getFlaggedStatus() {
            return flagged;
        }

        // EFFECTS: if the square's covered status is true, changes to false
        private void setCoveredStatus() {
            this.covered = false;
        }

        // EFFECTS: sets flagged status to boolean value passed as parameter
        private void setFlaggedStatus(boolean flaggedStatus) {
            this.flagged = flaggedStatus;
        }

        // EFFECTS: if false, sets mine status to true
        private void setContainsMine() {
            this.containsMine = true;
        }
    }
}