package com.sim_kar.sudoku_factory.gui;

import com.sim_kar.sudoku_factory.sudoku.Factory;
import com.sim_kar.sudoku_factory.sudoku.Position;
import com.sim_kar.sudoku_factory.sudoku.Section;
import com.sim_kar.sudoku_factory.sudoku.SudokuBoard;
import com.sim_kar.sudoku_factory.sudoku.SudokuFactory;
import com.sim_kar.sudoku_factory.sudoku.SudokuSection;
import com.sim_kar.sudoku_factory.sudoku.SudokuTile;
import com.sim_kar.sudoku_factory.sudoku.Tile;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class SudokuModelTest {
    @Mock
    Factory factoryMock;
    Model model;

    /**
     * Set up a simple 3x3 board that will be created in SudokuModel for testing.
     * Beside the rows and columns, it will have three blocks:
     * <br>
     * Block 0 has the positions (0, 0), (1, 0), (0, 1).
     * Block 1 has the positions (2, 0), (1, 1), (2, 1).
     * Block 2 has the positions (0, 2), (1, 2), (2, 2).
     * <br>
     * The tiles in row 1 are editable and empty.
     */
    @BeforeEach
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    void setup() throws InterruptedException {
        factoryMock = mock(SudokuFactory.class);

        /*
        board:                     solution:
        -------------------        -------------------
        |  1     2  |  3  |        |  1     2  |  3  |
        |      -----      |        |      -----      |
        |  0  |  0     0  |        |  4  |  5     6  |
        -------------------  ->    -------------------
        |  7     8     9  |        |  7     8     9  |
        -------------------        -------------------

        The dividers show how the board is divided into three blocks.
         */
        Tile tile1 = new SudokuTile(1, new Position(0, 0));
        Tile tile2 = new SudokuTile(2, new Position(1, 0));
        Tile tile3 = new SudokuTile(3, new Position(2, 0));
        Tile tile4 = new SudokuTile(4, new Position(0, 1));
        Tile tile5 = new SudokuTile(5, new Position(1, 1));
        Tile tile6 = new SudokuTile(6, new Position(2, 1));
        Tile tile7 = new SudokuTile(7, new Position(0, 2));
        Tile tile8 = new SudokuTile(8, new Position(1, 2));
        Tile tile9 = new SudokuTile(9, new Position(2, 2));

        tile4.setEditable(true);
        tile4.clear();
        tile5.setEditable(true);
        tile5.clear();
        tile6.setEditable(true);
        tile6.clear();

        Section[] rows = new Section[]{
                new SudokuSection(new HashSet<>(Arrays.asList(tile1, tile2, tile3))),
                new SudokuSection(new HashSet<>(Arrays.asList(tile4, tile5, tile6))),
                new SudokuSection(new HashSet<>(Arrays.asList(tile7, tile8, tile9)))
        };
        Section[] columns = new Section[]{
                new SudokuSection(new HashSet<>(Arrays.asList(tile1, tile4, tile7))),
                new SudokuSection(new HashSet<>(Arrays.asList(tile2, tile5, tile8))),
                new SudokuSection(new HashSet<>(Arrays.asList(tile3, tile6, tile9)))
        };
        Section[] block = new Section[]{
                new SudokuSection(new HashSet<>(Arrays.asList(tile1, tile2, tile4))),
                new SudokuSection(new HashSet<>(Arrays.asList(tile3, tile5, tile6))),
                new SudokuSection(new HashSet<>(Arrays.asList(tile7, tile8, tile9))),
        };

        // to make the model testable, the factory returns the board defined above
        // instead of creating a new random one
        when(factoryMock.create(anyInt())).thenReturn(new SudokuBoard(rows, columns, block));

        model = new SudokuModel(factoryMock);

        /*
        since create puzzle doesn't return anything, but rather sets a private field in the model
        it cannot be mocked. And since the board is created in a background thread, we have to wait
        for it. The thread only returns the above board, so it should be fast, but the thread still
        has to be created and run which isn't instantaneous.
         */
        CountDownLatch latch = new CountDownLatch(1);
        model.createPuzzle(40, latch);
        latch.await();
    }

    /**
     * Changes a value in the created puzzle, then creates a new puzzle and compares the value at
     * the same position in the new puzzle to ensure that the new puzzle has overwritten the old
     * one.
     * The created puzzles are always the same from {@link SudokuModelTest#setup()}.
     */
    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Creating a new puzzle sets the puzzle board to the new one")
    void creatingAPuzzleSetsTheNewPuzzle() throws InterruptedException {
        Position position = new Position(0, 0);

        int valueInPuzzle = model.getValueAt(position);

        Tile newTile = new SudokuTile(9, position);
        Section[] newSection = new Section[]{
                new SudokuSection(new HashSet<>(List.of(newTile)))
        };

        when(factoryMock.create(anyInt())).thenReturn(
                new SudokuBoard(newSection, newSection, newSection));

        CountDownLatch latch = new CountDownLatch(1);
        model.createPuzzle(40, latch);
        latch.await();

        int valueInNewPuzzle = model.getValueAt(position);

        assertNotEquals(valueInPuzzle, valueInNewPuzzle);
    }

    @Test
    @DisplayName("Getting a value at a position returns the correct current value")
    void gettingAValueAtAPositionReturnsTheCorrectValues() {
        assertEquals(1, model.getValueAt(new Position(0, 0)));
    }

    @Test
    @DisplayName("Getting a value at a position that is empty returns 0")
    void gettingAnEmptyValueAtAPositionReturnsZero() {
        assertEquals(0, model.getValueAt(new Position(0, 1)));
    }

    @Test
    @DisplayName("Can set the value at a position that is editable")
    void canSetValueAtEditablePosition() {
        Position position = new Position(0, 1);
        int originalValue = model.getValueAt(position);
        model.setValueAt(position, 9);
        int newValue = model.getValueAt(position);

        assertNotEquals(originalValue, newValue);
    }

    @Test
    @DisplayName("Can check if a position is editable or not")
    void canCheckIfPositionIsEditable() {
        assertAll(
                () -> assertFalse(model.isEditable(new Position(0, 0))),
                () -> assertTrue(model.isEditable(new Position(0, 1)))
        );
    }

    /**
     * Setting an incorrect value at (0, 1) should return all incorrect sections on the board from
     * {@link SudokuModelTest#setup()}: row 1, column 0 and block 0.
     */
    @Test
    @DisplayName("Can get all positions in sections with mistakes made by a user")
    void canGetSectionsWithUserMistakes() {
        Set<Position> sectionsWithUserMistakes = new HashSet<>(Arrays.asList(
                // column
                new Position( 0, 0),
                new Position( 0, 1),
                new Position( 0, 2),
                // row
                new Position( 1, 1),
                new Position( 2, 1),
                // block; see @BeforeAll above for an illustration
                new Position( 0, 0),
                new Position( 1, 0),
                new Position( 0, 1)
        ));


        model.setValueAt(new Position(0, 1), 9);

        // Position has an equals method, so two sets containing positions are comparable
        assertEquals(sectionsWithUserMistakes, model.getSectionsWithMistakes());
    }

    /**
     * There is an empty tile in each row, so all positions in those sections should be returned,
     * which is every single position on the board.
     */
    @Test
    @DisplayName("Can get all positions in sections with mistakes when counting empty tiles")
    void canGetSectionsWithAllMistakes() {
        // since there is a mistake in each column, the set should contain all positions
        Set<Position> sectionsWithMistakes = new HashSet<>();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                sectionsWithMistakes.add(new Position(x, y));
            }
        }

        assertEquals(sectionsWithMistakes, model.getSectionsWithMistakes(false));
    }

    /**
     * Sets two duplicate values in editable positions: (0, 1) with value 1 has a duplicate in
     * its column and block; (1, 1) with value 2 has a duplicate in its column.
     */
    @Test
    @DisplayName("Can get all editable positions that are duplicates")
    void canGetAllEditableDuplicates() {
        Position duplicate = new Position(0, 1);
        Position duplicate2 = new Position(1, 1);
        Set<Position> duplicatePositions = new HashSet<>(Arrays.asList(duplicate, duplicate2));

        model.setValueAt(duplicate, 1);
        model.setValueAt(duplicate2, 2);

        assertEquals(duplicatePositions, model.getDuplicates());
    }

    /**
     * Sets a duplicate at position (0, 1) with value 1. Position (0, 0) in the same column and
     * block is also 1, so they are duplicates.
     */
    @Test
    @DisplayName("Can get all positions that are duplicates")
    void canGetAllDuplicates() {
        Position originalValue = new Position(0, 0);
        Position duplicate = new Position(0, 1);
        Set<Position> duplicatePositions = new HashSet<>(Arrays.asList(
                originalValue,
                duplicate
        ));

        model.setValueAt(duplicate, 1);

        assertEquals(duplicatePositions, model.getDuplicates(false));
    }

    @Test
    @DisplayName("Registered observers are notified when a value on the board is changed")
    void observersAreNotifiedWhenBoardIsChanged() {
        BoardChangeObserver observerMock = mock(BoardChangeObserver.class);

        model.registerObserver(observerMock);
        model.setValueAt(new Position(0, 1), 4);

        verify(observerMock).updateBoard();
    }

    /**
     * Filling out the empty tiles on row 1 with the values 4, 5, 6 solves the board.
     */
    @Test
    @DisplayName("Registered observers are notified when the board is solved")
    void observersAreNotifiedWhenBoardIsSolved() {
        BoardSolvedObserver observerMock = mock(BoardSolvedObserver.class);

        model.registerObserver(observerMock);
        model.setValueAt(new Position(0, 1), 4);
        model.setValueAt(new Position(1, 1), 5);
        model.setValueAt(new Position(2, 1), 6);

        verify(observerMock).solved();
    }

    @Test
    @DisplayName("Removed observers are not notified when a value on the board is changed")
    void removedObserversAreNotifiedWhenBoardIsChanged() {
        BoardChangeObserver observerMock = mock(BoardChangeObserver.class);

        model.registerObserver(observerMock);
        model.setValueAt(new Position(0, 1), 4);

        model.removeObserver(observerMock);
        model.setValueAt(new Position(0, 1), 5);

        verify(observerMock, times(1)).updateBoard();
    }

    /**
     * Filling out the empty tiles on row 1 with the values 4, 5, 6 solves the board.
     */
    @Test
    @DisplayName("Removed observers are not notified when the board is solved")
    void removedObserversAreNotNotifiedWhenBoardIsSolved() {
        BoardSolvedObserver observerMock = mock(BoardSolvedObserver.class);

        model.registerObserver(observerMock);
        model.setValueAt(new Position(0, 1), 4);
        model.setValueAt(new Position(1, 1), 5);
        model.setValueAt(new Position(2, 1), 6);

        model.removeObserver(observerMock);
        model.setValueAt(new Position(0, 1), 4);

        verify(observerMock, times(1)).solved();
    }
}