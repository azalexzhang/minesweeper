package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.Board.*;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    Board board;

    @BeforeEach
    void runBefore() {
        board = new Board();
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
    void testUncoverSquare() {
        try {
            board.generateBoard();
            board.uncoverSquare(0, 0);
            assertFalse(board.getCoveredStatusByCoordinates(0, 0));

            if (!board.getMineStatusByCoordinates(0, 1)
                    && !board.getMineStatusByCoordinates(1, 0)
                    && !board.getMineStatusByCoordinates(1, 1)) {
                assertFalse(board.getCoveredStatusByCoordinates(0, 1));
                assertFalse(board.getCoveredStatusByCoordinates(1, 0));
                assertFalse(board.getCoveredStatusByCoordinates(1, 1));
            } else {
                assertTrue(board.getCoveredStatusByCoordinates(0, 1));
                assertTrue(board.getCoveredStatusByCoordinates(1, 0));
                assertTrue(board.getCoveredStatusByCoordinates(1, 1));
            }
        } catch (Exception e) {
            fail("Unexpected exception thrown while generating board");
        }
    }

    @Test
    void testAllAdjacentSquaresNoMine() {

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