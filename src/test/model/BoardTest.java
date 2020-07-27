package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.Board.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

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

        try {
            board.generateBoard();
            // check that the number of mines is equal to NUMBER_OF_MINES
            for (int i = 0; i < X_DIMENSION; i++) {
                for (int j = 0; j < Y_DIMENSION; j++) {
                    if (board.getMineStatusByCoordinates(i, j)) {
                        mines++;
                    }
                }
            }
        } catch (Exception e) {
            fail("Unexpected exception thrown while generating board");
        }


        assertEquals(NUMBER_OF_MINES, mines);
    }

    @Test
    void testDimensions() {
        try {
            board.generateBoard();
        } catch (Exception e) {
            fail("Unexpected exception thrown while generating board");
        }
        assertEquals(X_DIMENSION, board.getXDimension());
        assertEquals(Y_DIMENSION, board.getYDimension());
    }

    @Test
    void testUncoverBottomCornerSquare() {
        try {
            int x = 0;
            int y = 0;
            board.generateBoard();
            board.uncoverSquare(x, y);
            assertFalse(board.getCoveredStatusByCoordinates(x, y));
            testUncoverAllAdjacentSquares(board, x, y);

            x = X_DIMENSION - 1;
            board.uncoverSquare(x, y);
            assertFalse(board.getCoveredStatusByCoordinates(x, y));
            testUncoverAllAdjacentSquares(board, x, y);
        } catch (Exception e) {
            fail("Unexpected exception thrown while generating board");
        }
    }

    @Test
    void testUncoverTopCornerSquare() {
        try {
            int x = 0;
            int y = Y_DIMENSION - 1;
            board.generateBoard();
            board.uncoverSquare(x, y);
            assertFalse(board.getCoveredStatusByCoordinates(x, y));
            testUncoverAllAdjacentSquares(board, x, y);

            x = X_DIMENSION - 1;
            board.uncoverSquare(x, y);
            assertFalse(board.getCoveredStatusByCoordinates(x, y));
            testUncoverAllAdjacentSquares(board, x, y);
        } catch (Exception e) {
            fail("Unexpected exception thrown while generating board");
        }
    }

    @Test
    void testUncoverTopBottomEdgeSquare() {
        try {
            int x = X_DIMENSION / 2;
            int y = 0;
            board.generateBoard();
            board.uncoverSquare(x, y);
            assertFalse(board.getCoveredStatusByCoordinates(x, y));
            testUncoverAllAdjacentSquares(board, x, y);

            y = Y_DIMENSION - 1;
            board.uncoverSquare(x, y);
            assertFalse(board.getCoveredStatusByCoordinates(x, y));
            testUncoverAllAdjacentSquares(board, x, y);
        } catch (Exception e) {
            fail("Unexpected exception thrown while generating board");
        }
    }

    @Test
    void testUncoverLeftRightEdgeSquare() {
        try {
            int x = 0;
            int y = Y_DIMENSION / 2;
            board.generateBoard();
            board.uncoverSquare(x, y);
            assertFalse(board.getCoveredStatusByCoordinates(x, y));
            testUncoverAllAdjacentSquares(board, x, y);

            x = X_DIMENSION - 1;
            board.uncoverSquare(x, y);
            assertFalse(board.getCoveredStatusByCoordinates(x, y));
            testUncoverAllAdjacentSquares(board, x, y);
        } catch (Exception e) {
            fail("Unexpected exception thrown while generating board");
        }
    }

    @Test
    void testUncoverMiddleSquare() {
        try {
            int x = X_DIMENSION / 2;
            int y = Y_DIMENSION / 2;
            board.generateBoard();
            board.uncoverSquare(x, y);
            assertFalse(board.getCoveredStatusByCoordinates(x, y));
            testUncoverAllAdjacentSquares(board, x, y);
        } catch (Exception e) {
            fail("Unexpected exception thrown while generating board");
        }
    }

    void testUncoverAllAdjacentSquares(Board board, int x, int y) {
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

    @Test
    void testFlagSquare() {
        try {
            board.generateBoard();
            board.flagSquare(0, 0);
            assertTrue(board.getFlaggedStatusByCoordinates(0, 0));
        } catch (Exception e) {
            fail("Unexpected exception thrown while generating board");
        }
    }
}