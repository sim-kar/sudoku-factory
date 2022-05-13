/**
 * Interface for Tiles
 */
public interface Tile {

    /**
     * Get the correct value for the tile
     * @return the correct value
     */
    public int getCorrectVal();

    /**
     * Sets whether tile is editable
     * @param editable
     */
    public void setEditable(boolean editable);

    /**
     * Get whether the tile is editable
     * @return
     */
    public boolean isEditable();

    /**
     * Get the current value (user input) of the cell
     * @return the current value
     */
    public int getCurrentVal();

    /**
     * Sets the current value of the cell
     * @param value
     */
    public void setCurrentVal(int value);

    /**
     * Clears the entered value
     */
    public void clear();

    /**
     * Checks whether the entered value (current value) equals the correct value
     * @return
     */
    public boolean check();
}
