package com.dt042g.group8.sudoku;

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
    SudokuTile tile;

    /**
     * Set up a board with a few tiles for testing. Has one row, one column and one block
     * with three tiles each:
     * <br>
     * - - 1 - - - - - - <br>
     * - - - - - - - - - <br>
     * 3 - - - 1 - - 7 - <br>
     * - - - 3 - - - - - <br>
     * - - 3 - 1 - - - - <br>
     * - - - - - 7 - - - <br>
     * - - - - - - - - - <br>
     * - - - - - - - - - <br>
     * - - 7 - - - - - - <br>
     * <br>
     * Row 2: (0, 2), (4, 2), (7, 2).
     * Column 2: (2, 0), (2, 4), (2, 8).
     * Block 4: (3, 3), (4, 4), (5, 5)
     */
    @BeforeEach
    @DisplayName("Initialize variables for testing and create a SudokuBoard")
    void initVars() {
        Set<Tile> tiles;

        // Initialize a row
        tiles = new HashSet<>();
        tile = (new SudokuTile(1,new Position(4,2)));
        tiles.add(tile);
        tiles.add(new SudokuTile(3,new Position(0,2)));
        tiles.add(new SudokuTile(7,new Position(7,2)));

        sectionRow = new SudokuSection(tiles);
        rows = new SudokuSection[1];
        rows[0] = sectionRow;

        //Initialize a column
        tiles = new HashSet<>();
        tiles.add(new SudokuTile(3, new Position(2,4)));
        tiles.add(new SudokuTile(1,new Position(2,0)));
        tiles.add(new SudokuTile(7,new Position(2,8)));

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
        assertEquals(sectionRow, board.getRow(2));
    }

    @Test
    @DisplayName("Get the row-section containing a certain Position")
    void boardReturnsRowSectionFromPosition() {
        assertEquals(sectionRow, board.getRow(new Position(0,2)));
    }

    @Test
    @DisplayName("Get the column-section for a certain column number")
    void boardReturnsColumnSectionFromRowNumber() {
        assertEquals(sectionColumn, board.getColumn(2));
    }

    @Test
    @DisplayName("Get the column-section containing a certain Position")
    void boardReturnsColumnSectionFromPosition() {
        assertEquals(sectionColumn, board.getColumn(new Position(2,0)));
    }

    @Test
    @DisplayName("Get the block-section for a certain Position")
    void createBoardReturnsBlockSection() {
        assertEquals(sectionBlock, board.getBlock(new Position(4,4)));
    }

    @Test
    @DisplayName("Get the Tile at a certain Position")
    void boardReturnsCorrectTile() {
        Position position = new Position(4,2);
        assertEquals(tile, board.getTile(position));
    }

    @Test
    @DisplayName("Getting a Tile for a position not on the board throws illegal argument exception")
    void gettingATilePositionNotOnBoardThrowsIllegalArgumentException() {
        Position position = new Position(99,99);

        assertThrows(IllegalArgumentException.class, () -> board.getTile(position));
    }

    /**
     * Changes the value of tile (4, 2) from 1 to 7.
     */
    @Test
    @DisplayName("Set the value of a Tile at a certain Position")
    void setValueOfTileReturnCorrectValue() {
        Position position = new Position(4,2);
        board.getTile(position).setEditable(true);
        board.setTile(position,7);

        assertEquals(7, board.getTile(position).getCurrentValue());
    }

    @Test
    @DisplayName(
            "Setting the value of a Tile at a position not on the board does not throw exception"
    )
    void setValueOfTileNotOnBoardDoesNotThrowException() {
        Position position = new Position(99,99);

        assertDoesNotThrow(() -> board.setTile(position,7));
    }

    @Test
    @DisplayName("Check if the board is correct when all Tiles have the correct value")
    void checkAllTilesReturnTrue() {
        assertTrue(board.isCorrect());
    }

    /**
     * Changes the value of tile (4, 2) from its correct value 1 to the incorrect value 7.
     */
    @Test
    @DisplayName("Check if the board is not correct when all Tiles do not have the correct value")
    void checkAllTilesReturnFalse() {
        Position position = new Position(4,2);
        board.getTile(position).setEditable(true);
        board.setTile(position,7);

        assertFalse(board.isCorrect());
    }

    /**
     * Changes the value of tile (4, 2) in row 2 from its correct value 1 to the incorrect value 7,
     * so that row two becomes incorrect.
     */
    @Test
    @DisplayName("Getting incorrect sections returns a section if it is incorrect")
    void getIncorrectSectionsReturnsList() {
        Position position = new Position(4,2);
        board.getTile(position).setEditable(true);
        board.setTile(position,7);

        assertAll(
                () -> assertEquals(1, board.getIncorrectSections().size()),
                () -> assertEquals(sectionRow, board.getIncorrectSections().get(0))
        );
    }

    /**
     * Sets up a small board with three rows, three columns and one block: <br>
     *
     * 1 4 7 <br>
     * 2 5 8 <br>
     * 3 6 9 <br>
     *
     * Changing the correct value 3 to the incorrect value 1 means that row 2, column 0 and
     * block 0 become incorrect.
     */
    @Test
    @DisplayName("Getting incorrect sections returns all incorrect sections when there are many")
    void getIncorrectSectionsReturnsSeveral() {

        Tile tile1 = new SudokuTile(1,new Position(3,3));
        Tile tile2 = new SudokuTile(2,new Position(3,4));
        Tile tile3 = new SudokuTile(3,new Position(3,5));
        Tile tile4 = new SudokuTile(4,new Position(4,3));
        Tile tile5 = new SudokuTile(5,new Position(4,4));
        Tile tile6 = new SudokuTile(6,new Position(4,5));
        Tile tile7 = new SudokuTile(7,new Position(5,3));
        Tile tile8 = new SudokuTile(8,new Position(5,4));
        Tile tile9 = new SudokuTile(9,new Position(5,5));

        Set<Tile> tilesRow1 = new HashSet<>();
        tilesRow1.add(tile1);
        tilesRow1.add(tile2);
        tilesRow1.add(tile3);

        Set<Tile> tilesRow2 = new HashSet<>();
        tilesRow2.add(tile4);
        tilesRow2.add(tile5);
        tilesRow2.add(tile6);

        Set<Tile> tilesRow3 = new HashSet<>();
        tilesRow3.add(tile7);
        tilesRow3.add(tile8);
        tilesRow3.add(tile9);

        Set<Tile> tilesCol1 = new HashSet<>();
        tilesCol1.add(tile1);
        tilesCol1.add(tile4);
        tilesCol1.add(tile7);

        Set<Tile> tilesCol2 = new HashSet<>();
        tilesCol2.add(tile2);
        tilesCol2.add(tile5);
        tilesCol2.add(tile8);

        Set<Tile> tilesCol3 = new HashSet<>();
        tilesCol3.add(tile3);
        tilesCol3.add(tile6);
        tilesCol3.add(tile9);

        Set<Tile> tilesBlock1 = new HashSet<>();
        tilesBlock1.add(tile1);
        tilesBlock1.add(tile2);
        tilesBlock1.add(tile3);
        tilesBlock1.add(tile4);
        tilesBlock1.add(tile5);
        tilesBlock1.add(tile6);
        tilesBlock1.add(tile7);
        tilesBlock1.add(tile8);
        tilesBlock1.add(tile9);

        Section[] rows = new SudokuSection[3];
        Section[] cols = new SudokuSection[3];
        Section[] blocks = new SudokuSection[1];

        rows[0] = new SudokuSection(tilesRow1);
        rows[1] = new SudokuSection(tilesRow2);
        rows[2] = new SudokuSection(tilesRow3);
        cols[0] = new SudokuSection(tilesCol1);
        cols[1] = new SudokuSection(tilesCol2);
        cols[2] = new SudokuSection(tilesCol3);
        blocks[0] = new SudokuSection(tilesBlock1);

        SudokuBoard board = new SudokuBoard(rows, cols, blocks);
        tile3.setEditable(true);
        tile3.setCurrentValue(1);

        assertAll(
                () -> assertEquals(3, board.getIncorrectSections().size()),
                () -> assertTrue(board.getIncorrectSections().contains(rows[0])),
                () -> assertTrue(board.getIncorrectSections().contains(cols[2])),
                () -> assertTrue(board.getIncorrectSections().contains(blocks[0]))
        );

    }

    @Test
    @DisplayName("Getting incorrect sections returns empty list if there are none")
    void getIncorrectSectionsReturnsEmptyList() {
        assertEquals(0, board.getIncorrectSections().size());
    }

    @Test
    @DisplayName("Clearing the board sets editable tiles to 0")
    void clearAllEditableTilesReturnsZero() {
        Position position = new Position(4,2);
        board.getTile(position).setEditable(true);
        board.clear();
        assertEquals(0, board.getTile(position).getCurrentValue());
    }
}