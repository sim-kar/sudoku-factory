import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;


public class SudokuSectionTest {
    @Test
    @DisplayName("Creating a Section and getting a Tile at a specific position")
    public void createdSectionReturnsCorrectTile() {
        Set<Tile> tiles = new HashSet<>();

        Tile tile1 = new SudokuTile(1, new Position(2,2));
        Tile tile2 = new SudokuTile(5, new Position(2,5));
        Tile tile3 = new SudokuTile(8, new Position(9,9));

        tiles.add(tile1);
        tiles.add(tile2);
        tiles.add(tile3);

        SudokuSection section = new SudokuSection(tiles);
        Assertions.assertEquals(tile1, section.getTile(new Position(2,2)));
        Assertions.assertEquals(tile2, section.getTile(new Position(2,5)));
        Assertions.assertEquals(tile3, section.getTile(new Position(9,9)));

    }
}
