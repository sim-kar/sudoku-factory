import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SudokuBoard implements Board{
    Map<Position, Section> rows = new HashMap<>();
    Map<Position, Section> columns = new HashMap<>();
    Map<Position, Section> blocks = new HashMap<>();

    /**
     * The SudokuBoard contains 9x9 Tiles and 27 Sections (one for each row, column, and 9 3x3 blocks
     *
     * @param rows The rows as an array of Sections
     * @param columns The columns as an array of Sections
     * @param blocks The blocks as an array of Sections
     */
    public SudokuBoard(Section[] rows, Section[] columns, Section[] blocks) {

        // Populate the rows Map
        for (int i = 0; i < rows.length; i++) {
            Set<Tile> tiles = rows[i].getTiles();
            int finalI = i;
            tiles.forEach(tile -> {
                Position position = tile.getPosition();
                this.rows.put(position, rows[finalI]);
            });
        }

        // Populate the columns Map
        for (int i = 0; i < columns.length; i++) {
            Set<Tile> tiles = columns[i].getTiles();
            int finalI = i;
            tiles.forEach(tile -> {
                Position position = tile.getPosition();
                this.columns.put(position, columns[finalI]);
            });
        }

        // Populate the blocks Map
        for (int i = 0; i < blocks.length; i++) {
            Set<Tile> tiles = blocks[i].getTiles();
            int finalI = i;
            tiles.forEach(tile -> {
                Position position = tile.getPosition();
                this.blocks.put(position, blocks[finalI]);
            });
        }

    }

    @Override
    public Section getRow(int x) {
        return this.rows.get(new Position(x,0));
    }

    @Override
    public Section getRow(Position xy) {
        return this.rows.get(xy);
    }

    @Override
    public Section getColumn(int y) {
        return this.columns.get(new Position(0,y));
    }

    @Override
    public Section getColumn(Position xy) {
        return this.columns.get(xy);
    }

    @Override
    public Section getBlock(Position xy) {
        return this.blocks.get(xy);
    }

    @Override
    public Tile getTile(Position xy) {
        return null;
    }

    @Override
    public void setTile(Position xy, int value) {
    }

    @Override
    public boolean isCorrect() {
        return false;
    }

    @Override
    public List<Section> getIncorrectSections() {
        return null;
    }

    @Override
    public void clear() {
    }
}
