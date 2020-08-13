package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;

import static model.Board.*;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
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

        board.generateBoard();
        // check that the number of mines is equal to NUMBER_OF_MINES
        mines = getBoardMines(mines);

        assertEquals(NUMBER_OF_MINES, mines);
    }

    @Test
    void testDimensions() {
        board.generateBoard();
        assertEquals(X_DIMENSION, board.getXDimension());
        assertEquals(Y_DIMENSION, board.getYDimension());
    }

    @Test
    void testLoadBoard() {
        try {
            board.generateBoard();
            board.saveBoard(1000000000.0,
                    "./data/testSave/dimensionsAndTime.txt",
                    "./data/testSave/testColumn");
            double testTime = board.loadBoard("./data/testSave/dimensionsAndTime.txt",
                    "/data/testSave/testColumn");
            assertEquals(1000000000.0, testTime);
            assertEquals(X_DIMENSION, board.getBoard().size());
            assertEquals(Y_DIMENSION, board.getBoard().get(0).size());
            assertEquals(NUMBER_OF_MINES, getBoardMines(0));
        } catch (IOException e) {
            fail("Unexpected IOException\n" + e);
        }
    }

    @Test
    void testLoadBoardNonexistentDimensionsAndTimeDataFile() {
        try {
            board.generateBoard();
            board.saveBoard(0,
                    "./data/testSave/dimensionsAndTime.txt",
                    "./data/testSave/testColumn");
            double testTime = board.loadBoard("./data/testSave/thisFileDoesNotExist.txt",
                    "./data/testSave/testColumn");
            assertEquals(0, testTime);
        } catch (IOException e) {
            fail("Unexpected IOException\n" + e);
        }
    }

    @Test
    void testLoadBoardNonexistentColumnFiles() {
        try {
            board.generateBoard();
            board.saveBoard(0,
                    "./data/testSave/dimensionsAndTime.txt",
                    "./data/testSave/testColumn");
            double testTime = board.loadBoard("./data/testSave/dimensionsAndTimeData.txt",
                    "./data/testSave/thisFileDoesNotExist");
            assertEquals(0, testTime);
        } catch (IOException e) {
            fail("Unexpected IOException\n" + e);
        }
    }

    @Test
    void testUncoverBottomCornerSquare() {
        int x = 0;
        int y = 0;
        board.generateBoard();
        board.uncoverSquare(x, y);
        assertFalse(board.getCoveredStatusByCoordinates(x, y));
        uncoverAllAdjacentSquares(board, x, y);

        x = X_DIMENSION - 1;
        board.uncoverSquare(x, y);
        assertFalse(board.getCoveredStatusByCoordinates(x, y));
        uncoverAllAdjacentSquares(board, x, y);
    }

    @Test
    void testUncoverTopCornerSquare() {
        int x = 0;
        int y = Y_DIMENSION - 1;
        board.generateBoard();
        board.uncoverSquare(x, y);
        assertFalse(board.getCoveredStatusByCoordinates(x, y));
        uncoverAllAdjacentSquares(board, x, y);

        x = X_DIMENSION - 1;
        board.uncoverSquare(x, y);
        assertFalse(board.getCoveredStatusByCoordinates(x, y));
        uncoverAllAdjacentSquares(board, x, y);
    }

    @Test
    void testUncoverTopBottomEdgeSquare() {
        int x = X_DIMENSION / 2;
        int y = 0;
        board.generateBoard();
        board.uncoverSquare(x, y);
        assertFalse(board.getCoveredStatusByCoordinates(x, y));
        uncoverAllAdjacentSquares(board, x, y);

        y = Y_DIMENSION - 1;
        board.uncoverSquare(x, y);
        assertFalse(board.getCoveredStatusByCoordinates(x, y));
        uncoverAllAdjacentSquares(board, x, y);
    }

    @Test
    void testUncoverLeftRightEdgeSquare() {
        int x = 0;
        int y = Y_DIMENSION / 2;
        board.generateBoard();
        board.uncoverSquare(x, y);
        assertFalse(board.getCoveredStatusByCoordinates(x, y));
        uncoverAllAdjacentSquares(board, x, y);

        x = X_DIMENSION - 1;
        board.uncoverSquare(x, y);
        assertFalse(board.getCoveredStatusByCoordinates(x, y));
        uncoverAllAdjacentSquares(board, x, y);
    }

    @Test
    void testUncoverMiddleSquare() {
        int x = X_DIMENSION / 2;
        int y = Y_DIMENSION / 2;
        board.generateBoard();
        board.uncoverSquare(x, y);
        assertFalse(board.getCoveredStatusByCoordinates(x, y));
        uncoverAllAdjacentSquares(board, x, y);
    }

    @Test
    void testGetSurroundingMines() {
        int mines = 0;

        board.generateBoard();
        mines = loopOverAdjacentMines(board, 0, 0, mines);
        assertEquals(mines, board.getSurroundingMines(0, 0));
        mines = loopOverAdjacentMines(board, X_DIMENSION - 1, 0, mines);
        assertEquals(mines, board.getSurroundingMines(X_DIMENSION - 1, 0));
        mines = loopOverAdjacentMines(board, X_DIMENSION - 1, Y_DIMENSION - 1, mines);
        assertEquals(mines, board.getSurroundingMines(X_DIMENSION - 1, Y_DIMENSION - 1));
        mines = loopOverAdjacentMines(board, 0, Y_DIMENSION - 1, mines);
        assertEquals(mines, board.getSurroundingMines(0, Y_DIMENSION - 1));

        mines = loopOverAdjacentMines(board, X_DIMENSION / 2, 0, mines);
        assertEquals(mines, board.getSurroundingMines(X_DIMENSION / 2, 0));
        mines = loopOverAdjacentMines(board, X_DIMENSION / 2, Y_DIMENSION - 1, mines);
        assertEquals(mines, board.getSurroundingMines(X_DIMENSION / 2, Y_DIMENSION - 1));
        mines = loopOverAdjacentMines(board, X_DIMENSION - 1, Y_DIMENSION / 2, mines);
        assertEquals(mines, board.getSurroundingMines(X_DIMENSION - 1, Y_DIMENSION / 2));
        mines = loopOverAdjacentMines(board, 0, Y_DIMENSION / 2, mines);
        assertEquals(mines, board.getSurroundingMines(0, Y_DIMENSION / 2));

        mines = loopOverAdjacentMines(board, X_DIMENSION / 2, Y_DIMENSION / 2, mines);
        assertEquals(mines, board.getSurroundingMines(X_DIMENSION / 2, Y_DIMENSION / 2));
    }

    void uncoverAllAdjacentSquares(Board board, int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int nextX = x + dx;
                int nextY = y + dy;
                if (!(nextX < 0 || nextX >= X_DIMENSION || nextY < 0 || nextY >= Y_DIMENSION)) {
                    if (board.allAdjacentSquaresNoMine(x, y) && !board.getMineStatusByCoordinates(x, y)) {
                        assertFalse(board.getCoveredStatusByCoordinates(nextX, nextY));
                    } else {
                        assertTrue(board.getCoveredStatusByCoordinates(nextX, nextY));
                    }
                }
            }
        }
    }

    private int loopOverAdjacentMines(Board board, int x, int y, int mines) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int nextX = x + dx;
                int nextY = y + dy;
                if (!(nextX < 0 || nextX >= X_DIMENSION || nextY < 0 || nextY >= Y_DIMENSION)) {
                    if (board.getMineStatusByCoordinates(nextX, nextY)) {
                        mines++;
                    }
                }
            }
        }

        return mines;
    }

    private int getBoardMines(int mines) {
        for (int i = 0; i < X_DIMENSION; i++) {
            for (int j = 0; j < Y_DIMENSION; j++) {
                if (board.getMineStatusByCoordinates(i, j)) {
                    mines++;
                }
            }
        }
        return mines;
    }

    @Test
    void testFlagSquare() {
        board.generateBoard();
        board.flagSquare(0, 0);
        assertTrue(board.getFlaggedStatusByCoordinates(0, 0));
        board.flagSquare(0, 0);
        assertFalse(board.getFlaggedStatusByCoordinates(0, 0));
        board.uncoverSquare(0, 0);
        board.flagSquare(0, 0);
        assertFalse(board.getFlaggedStatusByCoordinates(0, 0));
    }
}