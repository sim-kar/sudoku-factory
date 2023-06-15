package com.sim_kar.sudoku_factory.sudoku;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SudokuTileTest {
    SudokuTile tile;
    Position position = new Position(1,2);

    @Test
    @DisplayName("Getting the correct value from a tile returns the correct value")
    void compareCorrectNumber() {
        tile = new SudokuTile(1, position);
        assertEquals(1, tile.getCorrectValue());

        tile = new SudokuTile(9, position);
        assertEquals(9, tile.getCorrectValue());
    }

    @Test
    @DisplayName("Setting a tile to editable makes it editable")
    void SetGetEditable() {
        tile = new SudokuTile(1, position);

        tile.setEditable(true);
        assertTrue(tile.isEditable());

        tile.setEditable(false);
        assertFalse(tile.isEditable());
    }

    @Test
    @DisplayName("Setting the current value of an editable tile updates the value")
    void setGetCurrentVal() {
        tile = new SudokuTile(1, position);
        tile.setEditable(true);
        tile.setCurrentValue(1);
        assertEquals(1, tile.getCurrentValue());

        tile.setCurrentValue(9);
        assertEquals(9, tile.getCurrentValue());
    }

    @Test
    @DisplayName("Setting the current value of a non-editable tile does not update the value")
    void setCurrentNotEditable() {
        tile = new SudokuTile(1, position);
        tile.setEditable(true);
        tile.setCurrentValue(1);
        tile.setEditable(false);
        tile.setCurrentValue(2);
        assertEquals(1, tile.getCurrentValue());
    }

    @Test
    @DisplayName("Clearing an editable tile returns a current value of 0")
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
    @DisplayName("Clearing an non-editable tile does not change the current value")
    void cannotClearNonEditable() {
        tile = new SudokuTile(1, position);
        tile.setEditable(false);
        tile.clear();
        assertEquals(1, tile.getCurrentValue());
    }

    @Test
    @DisplayName("Checking a tile with same current and correct value returns true")
    void sameCurrentCorrectValueIsTrue() {
        tile = new SudokuTile(1, position);
        tile.setCurrentValue(1);
        assertTrue(tile.check());
    }

    @Test
    @DisplayName("Checking a tile with different current and correct value should return false")
    void sameCurrentCorrectValueIsFalse() {
        tile = new SudokuTile(1, position);
        tile.setEditable(true);
        tile.setCurrentValue(9);
        assertFalse(tile.check());
    }

    @Test
    @DisplayName("Getting the position of a tile returns the correct position")
    void CreateSudokuTileAndGetPosition() {
        tile = new SudokuTile(1, position);
        assertEquals(position, tile.getPosition());
    }
}