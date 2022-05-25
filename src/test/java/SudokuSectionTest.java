import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sudoku.Position;
import sudoku.SudokuSection;
import sudoku.SudokuTile;
import sudoku.Tile;
import java.util.HashSet;
import java.util.Set;


public class SudokuSectionTest {
    Set<Tile> tiles;
    Tile tile1, tile2, tile3;

    @BeforeEach
    public void setupFakeSet() {
        tiles = new HashSet<>();
        tile1 = new SudokuTile(1, new Position(2,2));
        tile2 = new SudokuTile(5, new Position(2,5));
        tile3 = new SudokuTile(8, new Position(9,9));

        tiles.add(tile1);
        tiles.add(tile2);
        tiles.add(tile3);
    }

    @Test
    @DisplayName("Creating a sudoku.Section and getting a sudoku.Tile at a specific position")
    public void createdSectionReturnsCorrectTile() {
        SudokuSection section = new SudokuSection(tiles);
        Assertions.assertEquals(tile1, section.getTile(new Position(2,2)));
        Assertions.assertEquals(tile2, section.getTile(new Position(2,5)));
        Assertions.assertEquals(tile3, section.getTile(new Position(9,9)));
    }

    @Test
    @DisplayName("Creating a sudoku.Section and getting all Tiles from it")
    public void createdSectionReturnsCorrectTiles() {
        SudokuSection section = new SudokuSection(tiles);
        Set<Tile> returnedTiles;
        returnedTiles = section.getTiles();

        Assertions.assertEquals(tiles, returnedTiles);
    }

    @Test
    @DisplayName("Creating a sudoku.Section of Tiles that all have correct values")
    public void createdSectionHasOnlyCorrectValues() {
        SudokuSection section = new SudokuSection(tiles);
        Assertions.assertTrue(section.isCorrect());
    }

    @Test
    @DisplayName("Creating a sudoku.Section of Tiles that NOT all have correct values")
    public void createdSectionHasIncorrectValues() {
        tile3.setEditable(true);
        tile3.setCurrentValue(9);
        SudokuSection section = new SudokuSection(tiles);
        Assertions.assertFalse(section.isCorrect());
    }

    @Test
    @DisplayName("Creating a sudoku.Section of Tiles that NOT all have correct values, and get the incorrect ones")
    public void createdSectionHasIncorrectValuesReturnsIncorrectTiles() {
        tile3.setEditable(true);
        tile3.setCurrentValue(9);
        SudokuSection section = new SudokuSection(tiles);

        Set<Tile> incorrectTiles = new HashSet<>();
        incorrectTiles.add(tile3);

        Assertions.assertEquals(incorrectTiles, section.getIncorrectTiles());
    }

    @Test
    @DisplayName("Creating a sudoku.Section of Tiles that all have correct values, and get the incorrect ones")
    public void createdSectionHasCorrectValuesReturnsNull() {
        SudokuSection section = new SudokuSection(tiles);
        Assertions.assertTrue(section.getIncorrectTiles().isEmpty());
    }

}
