package sudoku;

import java.util.Set;

/**
 * A section of a game board represents a group of tiles with no duplicate numbers.
 * In Sudoku it can be a row, a column, or a 3 x 3 box.
 */
public interface Section {

    /**
     * Get a tile at a certain position of the section
     *
     * @param xy the position of the tile in relation to the section
     * @return the tile
     */
    public Tile getTile(Position xy);

    /**
     * Get all the tiles of the section
     *
     * @return the tiles
     */
    public Set<Tile> getTiles();

    /**
     * Checks if all the tiles of the section have the correct number
     *
     * @return whether all tiles are correct
     */
    public boolean isCorrect();

    /**
     * Get a set of those tiles which have incorrect numbers
     *
     * @return set of incorrect tiles
     */
    public Set<Tile> getIncorrectTiles();

}
