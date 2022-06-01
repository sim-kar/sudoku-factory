package com.dt042g.group8.sudoku;

import java.util.List;

/**
 * A com.dt042g.group8.sudoku.Board represents the game board and holds references to all Sections and Tiles of the game
 */
public interface Board {

    /**
     * Gets a com.dt042g.group8.sudoku.Section representing a horizontal row
     *
     * @param y The y-position of the row
     * @return The com.dt042g.group8.sudoku.Section for the row
     */
    public Section getRow(int y);

    /**
     * Gets a com.dt042g.group8.sudoku.Section representing a horizontal row
     *
     * @param xy The com.dt042g.group8.sudoku.Position of any com.dt042g.group8.sudoku.Tile on the row
     * @return The com.dt042g.group8.sudoku.Section for the row
     */
    public Section getRow(Position xy);

    /**
     * Gets a com.dt042g.group8.sudoku.Section representing a vertical line (column)
     *
     * @param x The X-position of the column
     * @return The com.dt042g.group8.sudoku.Section for the column
     */
    public Section getColumn(int x);

    /**
     * Gets a com.dt042g.group8.sudoku.Section representing a vertical line (column)
     *
     * @param xy The com.dt042g.group8.sudoku.Position of any com.dt042g.group8.sudoku.Tile on the column
     * @return The com.dt042g.group8.sudoku.Section for the column
     */
    public Section getColumn(Position xy);

    /**
     * Get a com.dt042g.group8.sudoku.Section representing a block
     *
     * @param xy The position of any com.dt042g.group8.sudoku.Tile in the block
     * @return The com.dt042g.group8.sudoku.Section for the block
     */
    public Section getBlock(Position xy);

    /**
     * Gets a com.dt042g.group8.sudoku.Tile
     *
     * @param xy The com.dt042g.group8.sudoku.Position of the com.dt042g.group8.sudoku.Tile
     * @return The com.dt042g.group8.sudoku.Tile
     */
    public Tile getTile(Position xy);

    /**
     * Sets the value of the com.dt042g.group8.sudoku.Tile at the specified position, only if the com.dt042g.group8.sudoku.Tile is editable
     *
     * @param xy The com.dt042g.group8.sudoku.Position of the com.dt042g.group8.sudoku.Tile
     * @param value The new value of the com.dt042g.group8.sudoku.Tile
     */
    public void setTile(Position xy, int value);

    /**
     * Checks whether all Tiles have the correct value
     *
     * @return Whether all Tiles have the correct value
     */
    public boolean isCorrect();

    /**
     * Gets a List of Sections that contain Tiles with at least one incorrect value
     *
     * @return A list of Sections
     */
    public List<Section> getIncorrectSections();

    /**
     * Clears the values of all Tiles of the com.dt042g.group8.sudoku.Board that are editable.
     */
    public void clear();
}
