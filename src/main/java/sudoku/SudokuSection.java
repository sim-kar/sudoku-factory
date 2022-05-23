package sudoku;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A Sudoku section represents a row, a column, or a 3x3 group of tiles in the game board.
 * All its tiles have unique (correct) numbers.
 */
public class SudokuSection implements Section{

    private Map<Position, Tile> tiles = new HashMap<>();

    /**
     * Adding all the passed Tiles to a Map, with everyone's sudoku.Position as its key.
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
        Set<Tile> allTiles = new HashSet<>();
        tiles.forEach((position, tile) -> allTiles.add(tile));
        return allTiles;
    }

    @Override
    public boolean isCorrect() {
        for (Tile tile : tiles.values()) {
            if (!tile.check()) return false;
        }

        return true;
    }

    @Override
    public Set<Tile> getIncorrectTiles() {
        Set<Tile> incorrectTiles = new HashSet<>();
        for (Tile tile : tiles.values()) {
            if (!tile.check()) incorrectTiles.add(tile);
        }
        return incorrectTiles;
    }
}
