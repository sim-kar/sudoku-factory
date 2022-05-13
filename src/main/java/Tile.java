/**
 * A tile in a game board that contains a correct value, and a editable value input from user
 */
public interface Tile {

    /**
     * Get the correct value for the tile
     * @return the correct value
     */
    public int getCorrectValue();

    /**
     * Sets whether tile is editable
     * @param editable true or false
     */
    public void setEditable(boolean editable);

    /**
     * Get whether the tile is editable
     * @return whether the tile is editable
     */
    public boolean isEditable();

    /**
     * Get the current value (user input) of the cell
     * @return the current value
     */
    public int getCurrentValue();

    /**
     * Sets the current value of the cell
     * @param value of the cell
     */
    public void setCurrentValue(int value);

    /**
     * Clears the entered value
     */
    public void clear();

    /**
     * Checks whether the entered value (current value) equals the correct value
     * @return whether current value is the correct value
     */
    public boolean check();
}
