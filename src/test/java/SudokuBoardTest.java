import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SudokuBoardTest {
    SudokuSection[] rows = new SudokuSection[1];
    SudokuSection[] columns = new SudokuSection[1];
    SudokuSection[] blocks = new SudokuSection[1];
    SudokuSection sectionRow, sectionColumn, sectionBlock;
    SudokuBoard board;

    @BeforeEach
    @DisplayName("Initialize variables for testing and create a SudokuBoard")
    void initVars() {
        Set<Tile> tiles;

        //Initialize a row
        tiles = new HashSet<>();
        tiles.add(new SudokuTile(3,new Position(2,4)));
        tiles.add(new SudokuTile(1,new Position(2,0)));
        tiles.add(new SudokuTile(7,new Position(2,8)));

        sectionRow = new SudokuSection(tiles);
        rows = new SudokuSection[1];
        rows[0] = sectionRow;

        // Initialize a column
        tiles = new HashSet<>();
        tiles.add(new SudokuTile(3,new Position(0,2)));
        tiles.add(new SudokuTile(1,new Position(4,2)));
        tiles.add(new SudokuTile(7,new Position(7,2)));

        sectionColumn = new SudokuSection(tiles);
        columns = new SudokuSection[1];
        columns[0] = sectionColumn;

        // Initialize a block
        tiles = new HashSet<>();
        tiles.add(new SudokuTile(3,new Position(3,3)));
        tiles.add(new SudokuTile(1,new Position(4,4)));
        tiles.add(new SudokuTile(7,new Position(5,5)));

        sectionBlock = new SudokuSection(tiles);
        blocks = new SudokuSection[1];
        blocks[0] = sectionBlock;


        board = new SudokuBoard(rows, columns, blocks);
    }

    @Test
    @DisplayName("Get the row-section for a certain row number")
    void boardReturnsRowSectionFromRowNumber() {
        Assertions.assertEquals(sectionRow, board.getRow(2));
    }

    @Test
    @DisplayName("Get the row-section containing a certain Position")
    void boardReturnsRowSectionFromPosition() {
        Assertions.assertEquals(sectionRow, board.getRow(new Position(2,0)));
    }

    @Test
    @DisplayName("Get the column-section for a certain column number")
    void boardReturnsColumnSectionFromRowNumber() {
        Assertions.assertEquals(sectionColumn, board.getColumn(2));
    }

    @Test
    @DisplayName("Get the column-section containing a certain Position")
    void boardReturnsColumnSectionFromPosition() {
        Assertions.assertEquals(sectionColumn, board.getColumn(new Position(0,2)));
    }

    @Test
    @DisplayName("Get the block-section for a certain Position")
    void createBoardReturnsBlockSection() {
        Assertions.assertEquals(sectionBlock, board.getBlock(new Position(4,4)));
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