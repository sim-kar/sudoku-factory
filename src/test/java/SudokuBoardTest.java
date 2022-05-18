import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SudokuBoardTest {
    SudokuSection[] rows = new SudokuSection[1];
    SudokuSection[] columns = new SudokuSection[1];
    SudokuSection[] blocks = new SudokuSection[1];
    SudokuSection section_row2, section_col2;
    SudokuBoard board;

    @BeforeEach
    @DisplayName("Initialize variables for testing")
    void initVars() {
        Set<Tile> tiles;

        //Initialize a row
        tiles = new HashSet<>();
        tiles.add(new SudokuTile(3,new Position(2,4)));
        tiles.add(new SudokuTile(1,new Position(2,0)));
        tiles.add(new SudokuTile(7,new Position(2,8)));

        section_row2 = new SudokuSection(tiles);
        rows = new SudokuSection[1];
        rows[0] = section_row2;

        // Initialize a column
        tiles = new HashSet<>();
        tiles.add(new SudokuTile(3,new Position(0,2)));
        tiles.add(new SudokuTile(1,new Position(4,2)));
        tiles.add(new SudokuTile(7,new Position(7,2)));

        section_col2 = new SudokuSection(tiles);
        columns = new SudokuSection[1];
        columns[0] = section_col2;

        // Initialize a block



        board = new SudokuBoard(rows, columns, blocks);
    }

    @Test
    @DisplayName("Create a board with a row and get the section for the row")
    void createBoardReturnsRowSection() {
        Assertions.assertEquals(section_row2, board.getRow(2));
        Assertions.assertEquals(section_row2, board.getRow(new Position(2,0)));
    }

    @Test
    @DisplayName("Create a board with a column and get the section for the column")
    void createBoardReturnsColumnSection() {
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