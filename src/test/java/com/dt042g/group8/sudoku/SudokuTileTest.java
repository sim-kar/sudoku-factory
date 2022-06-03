package com.dt042g.group8.sudoku;

import org.junit.jupiter.api.*;
import com.dt042g.group8.sudoku.Position;
import com.dt042g.group8.sudoku.SudokuTile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SudokuTileTest {
    SudokuTile tile;
    Position position = new Position(1,2);

    @Test
    @DisplayName("Comparing the retrieved correct number with the one set")
    void compareCorrectNumber() {
        tile = new SudokuTile(1, position);
        assertEquals(1, tile.getCorrectValue());

        tile = new SudokuTile(9, position);
        assertEquals(9, tile.getCorrectValue());
    }

    @Test
    @DisplayName("Tests setting and getting the editable field")
    void SetGetEditable() {
        tile = new SudokuTile(1, position);

        tile.setEditable(true);
        assertTrue(tile.isEditable());

        tile.setEditable(false);
        assertFalse(tile.isEditable());
    }

    @Test
    @DisplayName("Set and get number of current value")
    void setGetCurrentVal() {
        tile = new SudokuTile(1, position);
        tile.setEditable(true);
        tile.setCurrentValue(1);
        assertEquals(1, tile.getCurrentValue());

        tile.setCurrentValue(9);
        assertEquals(9, tile.getCurrentValue());
    }

    @Test
    @DisplayName("Test that it is not possible to change current number while isEditable is false")
    void setCurrentNotEditable() {
        tile = new SudokuTile(1, position);
        tile.setEditable(true);
        tile.setCurrentValue(1);
        tile.setEditable(false);
        tile.setCurrentValue(2);
        assertEquals(1, tile.getCurrentValue());
    }

    @Test
    @DisplayName("Clearing an editable tile should return a current value of 0")
    void canClearEditable() {
        tile = new SudokuTile(1, position);
        tile.setEditable(true);
        tile.setCurrentValue(1);
        tile.clear();
        assertEquals(0, tile.getCurrentValue());

        tile.setCurrentValue(9);
        tile.clear();
        assertEquals(0, tile.getCurrentValue());
    }

    @Test
    @DisplayName("Clearing an non-editable tile should not change the current value")
    void cannotClearNonEditable() {
        tile = new SudokuTile(1, position);
        tile.setEditable(false);
        tile.clear();
        assertEquals(1, tile.getCurrentValue());
    }

    @Test
    @DisplayName("Same current and correct number should return true")
    void sameCurrentCorrectValueIsTrue() {
        tile = new SudokuTile(1, position);
        tile.setCurrentValue(1);
        assertTrue(tile.check());
    }

    @Test
    @DisplayName("Different current and correct number should return false")
    void sameCurrentCorrectValueIsFalse() {
        tile = new SudokuTile(1, position);
        tile.setEditable(true);
        tile.setCurrentValue(9);
        assertFalse(tile.check());
    }

    @Test
    @DisplayName("Creating SudokuTile with new Position, returns Position")
    void CreateSudokuTileAndGetPosition() {
        tile = new SudokuTile(1, position);
        assertEquals(position, tile.getPosition());
    }
}