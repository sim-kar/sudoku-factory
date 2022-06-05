package com.dt042g.group8.sudoku;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;


public class SudokuSectionTest {
    Set<Tile> tiles;
    Tile tile1, tile2, tile3;

    /**
     * Creates a set of tiles that can be used to create a section.
     * The tiles are:
     * Position (2, 2), value 1.
     * Position (2, 5), value 5.
     * Position (9, 9), value 8.
     */
    @BeforeEach
    public void setupFakeSet() {
        tiles = new HashSet<>();
        tile1 = new SudokuTile(1, new Position(2,2));
        tile2 = new SudokuTile(5, new Position(2,5));
        tile3 = new SudokuTile(8, new Position(9,9));

        tiles.add(tile1);
        tiles.add(tile2);
        tiles.add(tile3);
    }

    @Test
    @DisplayName("Getting a tile at a specific position from a section returns the correct tile")
    public void createdSectionReturnsCorrectTile() {
        SudokuSection section = new SudokuSection(tiles);
        Assertions.assertEquals(tile1, section.getTile(new Position(2,2)));
        Assertions.assertEquals(tile2, section.getTile(new Position(2,5)));
        Assertions.assertEquals(tile3, section.getTile(new Position(9,9)));
    }

    @Test
    @DisplayName("Getting all tiles from a section returns all tiles")
    public void createdSectionReturnsCorrectTiles() {
        SudokuSection section = new SudokuSection(tiles);
        Set<Tile> returnedTiles;
        returnedTiles = section.getTiles();

        Assertions.assertEquals(tiles, returnedTiles);
    }

    @Test
    @DisplayName("The section is correct if all tiles in it are correct")
    public void createdSectionHasOnlyCorrectValues() {
        SudokuSection section = new SudokuSection(tiles);
        Assertions.assertTrue(section.isCorrect());
    }

    /**
     * Changes the value of a tile in the section from the correct value 8 to the
     * incorrect value 9.
     */
    @Test
    @DisplayName("The section is not correct if all tiles in it are not correct")
    public void createdSectionHasIncorrectValues() {
        tile3.setEditable(true);
        tile3.setCurrentValue(9);
        SudokuSection section = new SudokuSection(tiles);
        Assertions.assertFalse(section.isCorrect());
    }

    @Test
    @DisplayName("Getting incorrect tiles from a section returns all incorrect tiles")
    public void createdSectionHasIncorrectValuesReturnsIncorrectTiles() {
        tile3.setEditable(true);
        tile3.setCurrentValue(9);
        SudokuSection section = new SudokuSection(tiles);

        Set<Tile> incorrectTiles = new HashSet<>();
        incorrectTiles.add(tile3);

        Assertions.assertEquals(incorrectTiles, section.getIncorrectTiles());
    }

    @Test
    @DisplayName("Getting incorrect tiles from a section without any returns an empty set")
    public void createdSectionHasCorrectValuesReturnsEmpty() {
        SudokuSection section = new SudokuSection(tiles);
        Assertions.assertTrue(section.getIncorrectTiles().isEmpty());
    }

}
