package com.dt042g.group8.gui;

import com.dt042g.group8.sudoku.Position;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

class SudokuViewTest {
    @Mock Model modelMock;
    @Mock Controller controllerMock;

    @BeforeEach
    void setup() {
        modelMock = mock(Model.class);
        controllerMock = mock(Controller.class);
    }

    @Test
    @DisplayName("Created view registers as board change observer")
    void createdViewRegistersAsBoardChangeObserver() {
        SudokuView view = new SudokuView(modelMock, controllerMock);

        verify(modelMock).registerObserver((BoardChangeObserver) view);
    }

    @Test
    @DisplayName("Created view registers as board solved observer")
    void createdViewRegistersAsBoardSolvedObserver() {
        SudokuView view = new SudokuView(modelMock, controllerMock);

        verify(modelMock).registerObserver((BoardSolvedObserver) view);
    }


    /**
     * Tests for the helper method getBlockIndex. If this method needs to be changed or
     * removed for refactoring don't hesitate to disable or remove this nested class!
     *
     * Each block contains 9 tiles, and each tile has an (x, y) position starting at (0, 0).
     * Indexes of all blocks:
     * | 0 | 1 | 2 |
     * | 3 | 4 | 5 |
     * | 6 | 7 | 8 |
     */
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("Get Block Index Tests")
    class GetBlockIndexTest {
        Method getBlockIndex;
        Model modelMock;
        Controller controllerMock;

        /**
         * Get the private helper method getBlockIndex(int x, int y) using reflection.
         */
        @BeforeEach
        void setup() throws NoSuchMethodException {
            getBlockIndex = SudokuView.class.getDeclaredMethod(
                    "getBlockIndex", int.class, int.class
            );
            getBlockIndex.setAccessible(true);

            modelMock = mock(Model.class);
            controllerMock = mock(Controller.class);
        }

        /**
         * The first block contains the tiles at the positions (0, 0) through (2, 2).
         */
        @Test
        @DisplayName("First position in first block gets the index of the first block")
        void firstPositionInFirstBlockGetsIndexOfFirstBlock()
                throws InvocationTargetException, IllegalAccessException {
            int x = 0;
            int y = 0;
            int blockIndex = (int) getBlockIndex.invoke(
                    new SudokuView(modelMock, controllerMock),
                    x,
                    y
            );

            assertEquals(0, blockIndex);
        }

        /**
         * The first block contains the tiles at the positions (0, 0) through (2, 2).
         */
        @Test
        @DisplayName("Last position in first block gets the index of the first block")
        void lastPositionInFirstBlockGetsIndexOfFirstBlock()
                throws InvocationTargetException, IllegalAccessException {
            int x = 2;
            int y = 2;
            int blockIndex = (int) getBlockIndex.invoke(
                    new SudokuView(modelMock, controllerMock),
                    x,
                    y
            );

            assertEquals(0, blockIndex);
        }

        /**
         * The second block contains the tiles at the positions (3, 0) through (5, 3).
         */
        @Test
        @DisplayName("First position in second block gets the index of the second block")
        void firstPositionInSecondBlockGetsIndexOfSecondBlock()
                throws InvocationTargetException, IllegalAccessException {
            int x = 3;
            int y = 0;
            int blockIndex = (int) getBlockIndex.invoke(
                    new SudokuView(modelMock, controllerMock),
                    x,
                    y
            );

            assertEquals(1, blockIndex);
        }

        /**
         * The fourth block contains the tiles at the positions (0, 3) through (2, 5).
         * Tests that getting the index of a block on another "row" works.
         */
        @Test
        @DisplayName("First position in fourth block gets the index of the fourth block")
        void firstPositionInFourthBlockGetsIndexOfFourthBlock()
                throws InvocationTargetException, IllegalAccessException {
            int x = 0;
            int y = 3;
            int blockIndex = (int) getBlockIndex.invoke(
                    new SudokuView(modelMock, controllerMock),
                    x,
                    y
            );

            assertEquals(3, blockIndex);
        }
    }

    /**
     * Tests for the helper method getPositions. If this method needs to be changed or
     * removed for refactoring don't hesitate to disable or remove this nested class!
     *
     * Returns a list of all 81 {@link com.dt042g.group8.sudoku.Position} on a Sudoku board,
     * from (0, 0) to (8, 8).
     */
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("Get Positions Tests")
    class GetPositionsTest {
        Method getPositions;
        Model modelMock;
        Controller controllerMock;

        /**
         * Get the private helper method getBlockIndex(int x, int y) using reflection.
         */
        @BeforeEach
        void setup() throws NoSuchMethodException {
            getPositions = SudokuView.class.getDeclaredMethod(
                    "getPositions");
            getPositions.setAccessible(true);

            modelMock = mock(Model.class);
            controllerMock = mock(Controller.class);
        }

        @Test
        @DisplayName("Returns a list of 81 positions")
        void getPositionsReturns81Positions()
                throws InvocationTargetException, IllegalAccessException {
            List<Position> positions =
                    (List<Position>) getPositions.invoke(new SudokuView(modelMock, controllerMock));

            assertEquals(81, positions.size());
        }

        @Test
        @DisplayName("First position is at coordinates 0, 0")
        void getPositionsReturnsFirstPositionAt00()
                throws InvocationTargetException, IllegalAccessException {
            Position first = new Position(0, 0);
            List<Position> positions =
                    (List<Position>) getPositions.invoke(new SudokuView(modelMock, controllerMock));

            assertEquals(first, positions.get(0));
        }

        /**
         * x-values should increment before y-values.
         */
        @Test
        @DisplayName("Second position is at coordinates 1, 0")
        void getPositionsReturnsSecondPositionAt10()
                throws InvocationTargetException, IllegalAccessException {
            Position second = new Position(1, 0);
            List<Position> positions =
                    (List<Position>) getPositions.invoke(new SudokuView(modelMock, controllerMock));

            assertEquals(second, positions.get(1));
        }

        @Test
        @DisplayName("Last position is at coordinates 8, 8")
        void getPositionsReturnsLastPositionAt88()
                throws InvocationTargetException, IllegalAccessException {
            Position last = new Position(8, 8);
            List<Position> positions =
                    (List<Position>) getPositions.invoke(new SudokuView(modelMock, controllerMock));

            assertEquals(last, positions.get(positions.size() - 1));
        }
    }
}