import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A Sudoku section represents a row, a column, or a 3x3 group of tiles in the game board.
 * All its tiles have unique (correct) numbers.
 */
public class SudokuSection implements Section{

    private Map<Position, Tile> tiles = new HashMap<>();

    /**
     * Adding all the passed Tiles to a Map, with everyone's Position as its key.
     *
     * @param tiles The tiles to add.
     */
    public SudokuSection(Set<Tile> tiles) {
        tiles.forEach(tile -> {
            this.tiles.put(tile.getPosition(), tile);
        });
    }

    @Override
    public Tile getTile(Position xy) {
        return tiles.get(xy);
    }

    @Override
    public Set<Tile> getTiles() {
        return null;
    }

    @Override
    public boolean isCorrect() {
        return false;
    }

    @Override
    public Set<Tile> getIncorrectTiles() {
        return null;
    }
}
