import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SudokuBoardTest {

    @Test
    void getRow() {

        Set<Tile> tiles = new HashSet<>();
        tiles.add(new SudokuTile(3,new Position(2,4)));
        tiles.add(new SudokuTile(1,new Position(2,0)));
        tiles.add(new SudokuTile(7,new Position(2,8)));

        SudokuSection section_row2 = new SudokuSection(tiles);
        SudokuSection[] rows;
        rows = new SudokuSection[1];
        rows[0] = section_row2;
        SudokuBoard board = new SudokuBoard(rows, null, null);

        Section returnedSection = board.getRow(2);
        Assertions.assertEquals(section_row2, returnedSection);

    }

    @Test
    void testGetRow() {
    }

    @Test
    void getColumn() {
    }

    @Test
    void testGetColumn() {
    }

    @Test
    void getBlock() {
    }

    @Test
    void getTile() {
    }

    @Test
    void setTile() {
    }

    @Test
    void isCorrect() {
    }

    @Test
    void getIncorrectSections() {
    }

    @Test
    void clear() {
    }
}