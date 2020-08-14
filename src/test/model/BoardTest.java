package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static model.Board.*;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board;
    Random random;

    @BeforeEach
    void runBefore() {
        board = new Board();
        random = new Random();
    }

    @Test
    void testNumberOfMines() {
        int mines = 0;

        board.generateBoard(X_DIMENSION_EASY, Y_DIMENSION_EASY, NUMBER_OF_MINES_EASY);
        // check that the number of mines is equal to NUMBER_OF_MINES
        mines = getBoardMines(mines);

        assertEquals(NUMBER_OF_MINES_EASY, mines);
    }

    @Test
    void testDimensions() {
        board.generateBoard(X_DIMENSION_EASY, Y_DIMENSION_EASY, NUMBER_OF_MINES_EASY);
        assertEquals(X_DIMENSION_EASY, board.getXDimension());
        assertEquals(Y_DIMENSION_EASY, board.getYDimension());
    }

    @Test
    void testLoadBoardNonexistentDimensionsAndTimeDataFile() {
        try {
            board.generateBoard(X_DIMENSION_EASY, Y_DIMENSION_EASY, NUMBER_OF_MINES_EASY);
            board.saveBoard(1000000000.0, X_DIMENSION_EASY, Y_DIMENSION_EASY,
                    "./data/testSave/dimensionsAndTime.txt", "./data/testSave/testColumn");
            double testTime = board.loadBoard("./data/testSave/thisFileDoesNotExist.txt",
                    "./data/testSave/testColumn");
            assertEquals(0, testTime);
        } catch (IOException e) {
            fail("Unexpected IOException\n" + e);
        }
    }

    @Test
    void testLoadBoard() {
        try {
            board.generateBoard(X_DIMENSION_EASY, Y_DIMENSION_EASY, NUMBER_OF_MINES_EASY);
            board.saveBoard(1000000000.0, X_DIMENSION_EASY, Y_DIMENSION_EASY,
                    "./data/testSave/dimensionsAndTime.txt", "./data/testSave/testColumn");
            double testTime = board.loadBoard("./data/testSave/dimensionsAndTime.txt",
                    "./data/testSave/testColumn");
            assertEquals(1000000000.0, testTime);
            assertEquals(X_DIMENSION_EASY, board.getBoard().size());
            assertEquals(Y_DIMENSION_EASY, board.getBoard().get(0).size());
            assertEquals(NUMBER_OF_MINES_EASY, getBoardMines(0));
            for (File file: new File("./data/testSave").listFiles()) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        } catch (IOException e) {
            fail("Unexpected IOException\n" + e);
        }
    }

    @Test
    void testLoadBoardNonexistentColumnFiles() {
        try {
            board.generateBoard(X_DIMENSION_EASY, Y_DIMENSION_EASY, NUMBER_OF_MINES_EASY);
            board.saveBoard(1000000000.0, X_DIMENSION_EASY, Y_DIMENSION_EASY,
                    "./data/testSave/dimensionsAndTime.txt", "./data/testSave/testColumn");
            double testTime = board.loadBoard("./data/testSave/dimensionsAndTimeData.txt",
                    "./data/testSave/thisFileDoesNotExist");
            assertEquals(0, testTime);
            for (File file: new File("./data/testSave").listFiles()) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        } catch (IOException e) {
            fail("Unexpected IOException\n" + e);
        }
    }

    @Test
    void testUncoverBottomCornerSquare() {
        int x = 0;
        int y = 0;
        board.generateBoard(X_DIMENSION_EASY, Y_DIMENSION_EASY, NUMBER_OF_MINES_EASY);
        board.uncoverSquare(x, y);
        assertFalse(board.getCoveredStatusByCoordinates(x, y));
        uncoverAllAdjacentSquares(board, x, y);
    }

    @Test
    void testUncoverTopCornerSquare() {
        int x = 0;
        int y = Y_DIMENSION_EASY - 1;
        board.generateBoard(X_DIMENSION_EASY, Y_DIMENSION_EASY, NUMBER_OF_MINES_EASY);
        board.uncoverSquare(x, y);
        assertFalse(board.getCoveredStatusByCoordinates(x, y));
        uncoverAllAdjacentSquares(board, x, y);
    }

    @Test
    void testUncoverTopBottomEdgeSquare() {
        int x = X_DIMENSION_EASY / 2;
        int y = 0;
        board.generateBoard(X_DIMENSION_EASY, Y_DIMENSION_EASY, NUMBER_OF_MINES_EASY);
        board.uncoverSquare(x, y);
        assertFalse(board.getCoveredStatusByCoordinates(x, y));
        uncoverAllAdjacentSquares(board, x, y);
    }

    @Test
    void testUncoverLeftRightEdgeSquare() {
        int x = 0;
        int y = Y_DIMENSION_EASY / 2;
        board.generateBoard(X_DIMENSION_EASY, Y_DIMENSION_EASY, NUMBER_OF_MINES_EASY);
        board.uncoverSquare(x, y);
        assertFalse(board.getCoveredStatusByCoordinates(x, y));
        uncoverAllAdjacentSquares(board, x, y);
    }

    @Test
    void testUncoverMiddleSquare() {
        int x = X_DIMENSION_EASY / 2;
        int y = Y_DIMENSION_EASY / 2;
        board.generateBoard(X_DIMENSION_EASY, Y_DIMENSION_EASY, NUMBER_OF_MINES_EASY);
        board.uncoverSquare(x, y);
        assertFalse(board.getCoveredStatusByCoordinates(x, y));
        uncoverAllAdjacentSquares(board, x, y);
    }

    @Test
    void testGetSurroundingMines() {
        int mines;

        board.generateBoard(X_DIMENSION_EASY, Y_DIMENSION_EASY, NUMBER_OF_MINES_EASY);
        mines = loopOverAdjacentMines(board, 0, 0);
        assertEquals(mines, board.getSurroundingMines(0, 0));
        mines = loopOverAdjacentMines(board, X_DIMENSION_EASY - 1, 0);
        assertEquals(mines, board.getSurroundingMines(X_DIMENSION_EASY - 1, 0));
        mines = loopOverAdjacentMines(board, X_DIMENSION_EASY - 1, Y_DIMENSION_EASY - 1);
        assertEquals(mines, board.getSurroundingMines(X_DIMENSION_EASY - 1, Y_DIMENSION_EASY - 1));
        mines = loopOverAdjacentMines(board, 0, Y_DIMENSION_EASY - 1);
        assertEquals(mines, board.getSurroundingMines(0, Y_DIMENSION_EASY - 1));

        mines = loopOverAdjacentMines(board, X_DIMENSION_EASY / 2, 0);
        assertEquals(mines, board.getSurroundingMines(X_DIMENSION_EASY / 2, 0));
        mines = loopOverAdjacentMines(board, X_DIMENSION_EASY / 2, Y_DIMENSION_EASY - 1);
        assertEquals(mines, board.getSurroundingMines(X_DIMENSION_EASY / 2, Y_DIMENSION_EASY - 1));
        mines = loopOverAdjacentMines(board, X_DIMENSION_EASY - 1, Y_DIMENSION_EASY / 2);
        assertEquals(mines, board.getSurroundingMines(X_DIMENSION_EASY - 1, Y_DIMENSION_EASY / 2));
        mines = loopOverAdjacentMines(board, 0, Y_DIMENSION_EASY / 2);
        assertEquals(mines, board.getSurroundingMines(0, Y_DIMENSION_EASY / 2));

        mines = loopOverAdjacentMines(board, X_DIMENSION_EASY / 2, Y_DIMENSION_EASY / 2);
        assertEquals(mines, board.getSurroundingMines(X_DIMENSION_EASY / 2, Y_DIMENSION_EASY / 2));
    }

    @Test
    void testGameWon() {
        board.generateBoard(X_DIMENSION_EASY, Y_DIMENSION_EASY, NUMBER_OF_MINES_EASY);
        assertFalse(board.gameWon());

        for (int x = 0; x < X_DIMENSION_EASY; x++) {
            for (int y = 0; y < Y_DIMENSION_EASY; y++) {
                if (!board.getMineStatusByCoordinates(x, y)) {
                    board.uncoverSquare(x, y);
                }
            }
        }

        assertTrue(board.gameWon());

        for (int x = 0; x < X_DIMENSION_EASY; x++) {
            for (int y = 0; y < Y_DIMENSION_EASY; y++) {
                board.uncoverSquare(x, y);
            }
        }

        assertFalse(board.gameWon());
    }

    @Test
    void testGameLost() {
        board.generateBoard(X_DIMENSION_EASY, Y_DIMENSION_EASY, NUMBER_OF_MINES_EASY);
        assertFalse(board.gameLost());

        for (int x = 0; x < X_DIMENSION_EASY; x++) {
            for (int y = 0; y < Y_DIMENSION_EASY; y++) {
                board.uncoverSquare(x, y);

                if (board.getMineStatusByCoordinates(x, y)) {
                    assertTrue(board.gameLost());
                    break;
                }
            }
        }
    }

    private void uncoverAllAdjacentSquares(Board board, int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int nextX = x + dx;
                int nextY = y + dy;
                if (!(nextX < 0 || nextX >= X_DIMENSION_EASY || nextY < 0 || nextY >= Y_DIMENSION_EASY)) {
                    if (board.allAdjacentSquaresNoMine(x, y) && !board.getMineStatusByCoordinates(x, y)) {
                        assertFalse(board.getCoveredStatusByCoordinates(nextX, nextY));
                    } else if (!board.getMineStatusByCoordinates(x, y)) {
                        if (!board.getCoveredStatusByCoordinates(nextX, nextY)) {
                            displayBoard(X_DIMENSION_EASY, Y_DIMENSION_EASY);
                            System.err.println(x + " " + y);
                        }
                        assertTrue(board.getCoveredStatusByCoordinates(nextX, nextY));
                    }
                }
            }
        }
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

    private int loopOverAdjacentMines(Board board, int x, int y) {
        int mines = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int nextX = x + dx;
                int nextY = y + dy;
                if (!(nextX < 0 || nextX >= X_DIMENSION_EASY || nextY < 0 || nextY >= Y_DIMENSION_EASY)) {
                    if (board.getMineStatusByCoordinates(nextX, nextY)) {
                        mines++;
                    }
                }
            }
        }

        return mines;
    }

    private int getBoardMines(int mines) {
        for (int i = 0; i < X_DIMENSION_EASY; i++) {
            for (int j = 0; j < Y_DIMENSION_EASY; j++) {
                if (board.getMineStatusByCoordinates(i, j)) {
                    mines++;
                }
            }
        }
        return mines;
    }

    @Test
    void testFlagSquare() {
        board.generateBoard(X_DIMENSION_EASY, Y_DIMENSION_EASY, NUMBER_OF_MINES_EASY);
        board.flagSquare(0, 0);
        assertTrue(board.getFlaggedStatusByCoordinates(0, 0));
        board.flagSquare(0, 0);
        assertFalse(board.getFlaggedStatusByCoordinates(0, 0));
        board.uncoverSquare(0, 0);
        board.flagSquare(0, 0);
        assertFalse(board.getFlaggedStatusByCoordinates(0, 0));
    }
}