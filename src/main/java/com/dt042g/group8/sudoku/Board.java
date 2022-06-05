package com.dt042g.group8.sudoku;

import java.util.List;

/**
 * Represents a number puzzle game board that holds references to all Sections and Tiles of the game
 */
public interface Board {

    /**
     * Gets a Section representing a horizontal row
     *
     * @param y The y-position of the row
     * @return The Section for the row
     */
    public Section getRow(int y);

    /**
     * Gets a Section representing a horizontal row
     *
     * @param xy The Position of any Tile on the row
     * @return The Section for the row
     */
    public Section getRow(Position xy);

    /**
     * Gets a Section representing a vertical line (column)
     *
     * @param x The X-position of the column
     * @return The Section for the column
     */
    public Section getColumn(int x);

    /**
     * Gets a Section representing a vertical line (column)
     *
     * @param xy The Position of any Tile on the column
     * @return The Section for the column
     */
    public Section getColumn(Position xy);

    /**
     * Get a Section representing a block
     *
     * @param xy The position of any Tile in the block
     * @return The Section for the block
     */
    public Section getBlock(Position xy);

    /**
     * Gets a Tile
     *
     * @param xy The Position of the Tile
     * @return The Tile
     */
    public Tile getTile(Position xy);

    /**
     * Sets the value of the Tile at the specified position, only if the Tile is editable
     *
     * @param xy The Position of the Tile
     * @param value The new value of the Tile
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
     * Clears the values of all Tiles of the Board that are editable.
     */
    public void clear();
}
