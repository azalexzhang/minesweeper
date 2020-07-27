package model;

import java.util.ArrayList;
import java.util.Random;

// Represents the Minesweeper board.
public class Board {
    public static final int X_DIMENSION = 16;
    public static final int Y_DIMENSION = 16;
    public static final int NUMBER_OF_MINES = 30;

    private ArrayList<ArrayList<Square>> board; // Each ArrayList<Square> within the ArrayList
                                                     // represents a column.

    // EFFECTS: constructs a board
    public Board() {
        this.board = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: generates new board of dimensions X_DIMENSION by Y_DIMENSION
    public void generateBoard() throws Exception {
        Random random = new Random();
        ArrayList<Square> arr = new ArrayList<>();
        int mines = 0;

        for (int i = 0; i < X_DIMENSION; i++) {
            for (int j = 0; j < Y_DIMENSION; j++) {
                if (mines < NUMBER_OF_MINES && random.nextBoolean()) {
                    arr.add(new Square(true, true, false));
                    mines++;
                } else {
                    arr.add(new Square(false, true, false));
                }
            }

            board.add(arr);
            arr = new ArrayList<>();
        }

        if (mines != NUMBER_OF_MINES) {
            throw new Exception();
        }
    }

    // EFFECTS: returns current board in form of ArrayList
    public ArrayList<ArrayList<Square>> getBoard() {
        return board;
    }

    // MODIFIES: this
    // EFFECTS: changes selected square from covered to uncovered. If square has no adjacent mines, uncovers
    //          all surrounding squares that are still covered.
    public void uncoverSquare(int x, int y) {
        if (this.getCoveredStatusByCoordinates(x, y)) {
            this.getSquareByCoordinates(x, y).setCoveredStatus();

            if (allAdjacentSquaresNoMine(x, y)) {
                this.uncoverSurroundingSquares(x, y);
            }
        }
    }

    private void uncoverSurroundingSquares(int x, int y) {
        if (x == 0 && y == 0) {
            this.uncoverSurroundingSquaresBottomLeft();
        } else if (x == 0 && y == Y_DIMENSION - 1) {
            this.uncoverSurroundingSquaresTopLeft();
        } else if (x == X_DIMENSION - 1 && y == Y_DIMENSION - 1) {
            this.uncoverSurroundingSquaresTopRight();
        } else if (x == X_DIMENSION - 1 && y == 0) {
            this.uncoverSurroundingSquaresBottomRight();
        } else if (x == 0) {
            this.uncoverSurroundingSquaresLeft(y);
        } else if (y == 0) {
            this.uncoverSurroundingSquaresBottom(x);
        } else if (x == X_DIMENSION - 1) {
            this.uncoverSurroundingSquaresRight(y);
        } else if (y == Y_DIMENSION - 1) {
            this.uncoverSurroundingSquaresTop(x);
        } else {
            this.uncoverSurroundingSquaresMiddle(x, y);
        }
    }

    private void uncoverSurroundingSquaresTopLeft() {
    }

    private void uncoverSurroundingSquaresBottomLeft() {
    }

    private void uncoverSurroundingSquaresTopRight() {
    }

    private void uncoverSurroundingSquaresBottomRight() {
    }

    private void uncoverSurroundingSquaresLeft(int y) {
    }

    private void uncoverSurroundingSquaresBottom(int x) {
    }

    private void uncoverSurroundingSquaresRight(int y) {
    }

    private void uncoverSurroundingSquaresTop(int x) {
    }

    private void uncoverSurroundingSquaresMiddle(int x, int y) {
    }

    // EFFECTS: returns true if all surrounding squares around the square at the given coordinates have
    //          no mines, using nine different helpers
    public boolean allAdjacentSquaresNoMine(int x, int y) {
        if (x == 0 && y == 0) {
            return allAdjacentSquaresNoMineBottomLeft();
        } else if (x == 0 && y == Y_DIMENSION - 1) {
            return allAdjacentSquaresNoMineTopLeft();
        } else if (x == X_DIMENSION - 1 && y == Y_DIMENSION - 1) {
            return allAdjacentSquaresNoMineTopRight();
        } else if (x == X_DIMENSION - 1 && y == 0) {
            return allAdjacentSquaresNoMineBottomRight();
        } else if (x == 0) {
            return allAdjacentSquaresNoMineLeft(y);
        } else if (y == 0) {
            return allAdjacentSquaresNoMineBottom(x);
        } else if (x == X_DIMENSION - 1) {
            return allAdjacentSquaresNoMineRight(y);
        } else if (y == Y_DIMENSION - 1) {
            return allAdjacentSquaresNoMineTop(x);
        } else {
            return allAdjacentSquaresNoMineMiddle(x, y);
        }
    }

    // EFFECTS: returns true if all surrounding squares around the square at the top right corner have
    //          no mines
    public boolean allAdjacentSquaresNoMineTopRight() {
        return false;
    }

    // EFFECTS: returns true if all surrounding squares around the square at the top left corner have
    //          no mines
    public boolean allAdjacentSquaresNoMineTopLeft() {
        return false;
    }

    // EFFECTS: returns true if all surrounding squares around the square at the bottom right corner have
    //          no mines
    public boolean allAdjacentSquaresNoMineBottomRight() {
        return false;
    }

    // EFFECTS: returns true if all surrounding squares around the square at the bottom left corner have
    //          no mines
    public boolean allAdjacentSquaresNoMineBottomLeft() {
        return false;
    }

    // EFFECTS: returns true if all surrounding squares around the square at the top edge have no mines
    public boolean allAdjacentSquaresNoMineTop(int x) {
        return false;
    }

    // EFFECTS: returns true if all surrounding squares around the square at the bottom edge have no mines
    public boolean allAdjacentSquaresNoMineBottom(int x) {
        return false;
    }

    // EFFECTS: returns true if all surrounding squares around the square at the left edge have no mines
    public boolean allAdjacentSquaresNoMineLeft(int y) {
        return false;
    }

    // EFFECTS: returns true if all surrounding squares around the square at the right edge have no mines
    public boolean allAdjacentSquaresNoMineRight(int y) {
        return false;
    }

    // EFFECTS: returns true if all surrounding squares around the square in the middle have no mines
    public boolean allAdjacentSquaresNoMineMiddle(int x, int y) {
        return false;
    }

    // MODIFIES: this
    // EFFECTS: flags selected square to indicate mine location
    public void flagSquare(int x, int y) {
        this.getSquareByCoordinates(x, y).setFlaggedStatus(!this.getFlaggedStatusByCoordinates(x, y));
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

        private boolean getFlaggedStatus() {
            return flagged;
        }

        private void setCoveredStatus() {
            this.covered = false;
        }

        private void setFlaggedStatus(boolean flaggedStatus) {
            this.flagged = flaggedStatus;
        }
    }
}