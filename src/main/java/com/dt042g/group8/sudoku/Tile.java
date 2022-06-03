package com.dt042g.group8.sudoku;

/**
 * A tile in on game board that contains a correct value, and a current value.
 * A tile can be made editable, which will clear it's current value. The current value can be
 * changed if it is editable, and it can be compared to the correct value to see if they match.
 */
public interface Tile {

    /**
     * Get the correct value for the tile
     *
     * @return the correct value
     */
    int getCorrectValue();

    /**
     * Sets whether tile is editable
     *
     * @param editable true or false
     */
    void setEditable(boolean editable);

    /**
     * Get whether the tile is editable
     *
     * @return whether the tile is editable
     */
    boolean isEditable();

    /**
     * Get the current value (user input) of the cell
     *
     * @return the current value
     */
    int getCurrentValue();

    /**
     * Sets the current value of the cell, if the tile is editable
     *
     * @param value of the cell
     */
    void setCurrentValue(int value);

    /**
     * Clears the entered value if the tile is editable
     */
    void clear();

    /**
     * Checks whether the entered value (current value) equals the correct value
     *
     * @return whether current value is the correct value
     */
    boolean check();

    /**
     * Get the Position of the Tile
     *
     * @return the Position
     */
    public Position getPosition();
}
