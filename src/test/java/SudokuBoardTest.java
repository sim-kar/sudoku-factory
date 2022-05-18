import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SudokuBoardTest {

    @Test
    @DisplayName("Create a board with a row and get the section for the row")
    void createBoardReturnsRowSection() {

        Set<Tile> tiles = new HashSet<>();
        tiles.add(new SudokuTile(3,new Position(2,4)));
        tiles.add(new SudokuTile(1,new Position(2,0)));
        tiles.add(new SudokuTile(7,new Position(2,8)));

        SudokuSection section_row2 = new SudokuSection(tiles);
        SudokuSection[] rows;
        rows = new SudokuSection[1];
        rows[0] = section_row2;
        SudokuBoard board = new SudokuBoard(rows, null, null);

        Assertions.assertEquals(section_row2, board.getRow(2));
        Assertions.assertEquals(section_row2, board.getRow(new Position(2,0)));

    }

    @Test
    @DisplayName("Create a board with a column and get the section for the column")
    void createBoardReturnsColumnSection() {

        Set<Tile> tiles = new HashSet<>();
        tiles.add(new SudokuTile(3,new Position(0,2)));
        tiles.add(new SudokuTile(1,new Position(4,2)));
        tiles.add(new SudokuTile(7,new Position(7,2)));

        SudokuSection section_col2 = new SudokuSection(tiles);
        SudokuSection[] columns;
        columns = new SudokuSection[1];
        columns[0] = section_col2;
        SudokuBoard board = new SudokuBoard(null, columns, null);

        Assertions.assertEquals(section_col2, board.getColumn(2));
        Assertions.assertEquals(section_col2, board.getColumn(new Position(0,2)));
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