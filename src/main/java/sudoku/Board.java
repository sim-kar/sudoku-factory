package sudoku;

import java.util.List;

/**
 * A sudoku.Board represents the game board and holds references to all Sections and Tiles of the game
 */
public interface Board {

    /**
     * Gets a sudoku.Section representing a horizontal row
     *
     * @param y The y-position of the row
     * @return The sudoku.Section for the row
     */
    public Section getRow(int y);

    /**
     * Gets a sudoku.Section representing a horizontal row
     *
     * @param xy The sudoku.Position of any sudoku.Tile on the row
     * @return The sudoku.Section for the row
     */
    public Section getRow(Position xy);

    /**
     * Gets a sudoku.Section representing a vertical line (column)
     *
     * @param x The X-position of the column
     * @return The sudoku.Section for the column
     */
    public Section getColumn(int x);

    /**
     * Gets a sudoku.Section representing a vertical line (column)
     *
     * @param xy The sudoku.Position of any sudoku.Tile on the column
     * @return The sudoku.Section for the column
     */
    public Section getColumn(Position xy);

    /**
     * Get a sudoku.Section representing a block
     *
     * @param xy The position of any sudoku.Tile in the block
     * @return The sudoku.Section for the block
     */
    public Section getBlock(Position xy);

    /**
     * Gets a sudoku.Tile
     *
     * @param xy The sudoku.Position of the sudoku.Tile
     * @return The sudoku.Tile
     */
    public Tile getTile(Position xy);

    /**
     * Sets the value of the sudoku.Tile at the specified position, only if the sudoku.Tile is editable
     *
     * @param xy The sudoku.Position of the sudoku.Tile
     * @param value The new value of the sudoku.Tile
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
     * Clears the values of all Tiles of the sudoku.Board that are editable.
     */
    public void clear();
}
