import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

class SudokuFactoryTest {
    Random random = new Random(0L);
    Solver solver = new SudokuSolver(random);
    Factory factory = new SudokuFactory(solver);

    // FIXME: does lower limit have to be increased?
    /**
     * 17 is the lowest limit of clues (filled in numbers in a Sudoku puzzle) where it is possible
     * to have a single solution.
     */
    @Test
    @DisplayName("Creating board with less than 17 clues throws exception")
    void creatingBoardWithLessThan17CluesThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(16));
    }

    /**
     * 81 is the total number of tiles on a Sudoku board. Therefore, it shouldn't be possible to
     * have more clues (filled in numbers in a Sudoku puzzle) than that.
     */
    @Test
    @DisplayName("Creating board with more than 81 clues throws exception")
    void creatingBoardWithMoreThan81CluesThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(82));
    }

    @Test
    @DisplayName("A board created with 40 clues has 40 clues")
    void createdBoardHasCorrectNumberOfClues() {
        Board board = factory.create(40);
        int allTiles = 81;
        Set<Tile> incorrectTiles = new HashSet<>();
        List<Section> incorrectSections = board.getIncorrectSections();
        for (Section section : incorrectSections) {
            incorrectTiles.addAll(section.getIncorrectTiles());
        }

        assertEquals(40, allTiles - incorrectTiles.size());
    }

    @Test
    @DisplayName("Creating a new board should not return the same board every time")
    void createdBoardsAreRandomized() {
        Board board = factory.create(81);

        // create another board that uses a different seed
        Random random2 = new Random(1L);
        Solver solver2 = new SudokuSolver(random2);
        Factory factory2 = new SudokuFactory(solver2);
        Board board2 = factory2.create(81);

        // SudokuBoard doesn't have an equals method; convert to 2d arrays and compare
        int[][] matrixOfBoard = new int[9][9];
        Section[] rowsInBoard = new Section[]{
                board.getRow(0),
                board.getRow(1),
                board.getRow(2),
                board.getRow(3),
                board.getRow(4),
                board.getRow(5),
                board.getRow(6),
                board.getRow(7),
                board.getRow(8),
        };
        for (Section row : rowsInBoard) {
            for (Tile tile : row.getTiles()) {
                int x = tile.getPosition().getX();
                int y = tile.getPosition().getY();
                matrixOfBoard[y][x] = tile.getCurrentValue();
            }
        }
        int[][] matrixOfBoard2 = new int[9][9];
        Section[] rowsInBoard2 = new Section[]{
                board2.getRow(0),
                board2.getRow(1),
                board2.getRow(2),
                board2.getRow(3),
                board2.getRow(4),
                board2.getRow(5),
                board2.getRow(6),
                board2.getRow(7),
                board2.getRow(8),
        };
        for (Section row : rowsInBoard2) {
            for (Tile tile : row.getTiles()) {
                int x = tile.getPosition().getX();
                int y = tile.getPosition().getY();
                matrixOfBoard2[y][x] = tile.getCurrentValue();
            }
        }

        assertNotEquals(matrixOfBoard, matrixOfBoard2);
    }

    @Nested
    @DisplayName("Conforms to Sudoku rules")
    class SudokuRulesTest {
        Random random = new Random(0L);
        Solver solver = new SudokuSolver(random);
        Factory factory = new SudokuFactory(solver);
        Board board = factory.create(40);


        @Test
        @DisplayName("Created board has nine rows")
        void createdBoardHasNineRows() {
            Set<Section> rows = new HashSet<>();
            for (int i = 0; i < 9; i++) {
                rows.add(board.getRow(i));
            }

            assertAll(
                    () -> assertEquals(9, rows.size()),
                    () -> assertNull(board.getRow(9))
            );
        }

        @Test
        @DisplayName("Created board has nine columns")
        void createdBoardHasNineColumns() {
            Set<Section> columns = new HashSet<>();
            for (int i = 0; i < 9; i++) {
                columns.add(board.getColumn(i));
            }

            assertAll(
                    () -> assertEquals(9, columns.size()),
                    () -> assertNull(board.getColumn(9))
            );
        }

        @Test
        @DisplayName("Created board has nine blocks")
        void createdBoardHasNineBlocks() {
            Set<Section> blocks = new HashSet<>();
            for (int row = 0; row <= 6; row += 3) {
                for (int column = 0; column <= 6; column += 3) {
                    blocks.add(board.getBlock(new Position(column, row)));
                }
            }

            assertAll(
                    () -> assertEquals(9, blocks.size()),
                    () -> assertNull(board.getBlock(new Position(9, 0))),
                    () -> assertNull(board.getBlock(new Position(0, 9)))
            );
        }

        @Test
        @DisplayName("Each row on created board has nine tiles")
        void rowsOnBoardHaveNineTiles() {
            assertAll(
                    () -> assertEquals(9, board.getRow(0)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getRow(1)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getRow(2)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getRow(3)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getRow(4)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getRow(5)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getRow(6)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getRow(7)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getRow(8)
                            .getTiles()
                            .size())
            );
        }

        @Test
        @DisplayName("Each column on created board has nine tiles")
        void columnsOnBoardHaveNineTiles() {
            assertAll(
                    () -> assertEquals(9, board.getColumn(0)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getColumn(1)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getColumn(2)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getColumn(3)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getColumn(4)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getColumn(5)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getColumn(6)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getColumn(7)
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getColumn(8)
                            .getTiles()
                            .size())
            );
        }

        @Test
        @DisplayName("Each block on created board has nine tiles")
        void blocksOnBoardHaveNineTiles() {
            assertAll(
                    () -> assertEquals(9, board.getBlock(new Position(0, 0))
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getBlock(new Position(0, 3))
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getBlock(new Position(0, 6))
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getBlock(new Position(3, 0))
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getBlock(new Position(3, 3))
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getBlock(new Position(3, 6))
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getBlock(new Position(6, 0))
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getBlock(new Position(6, 3))
                            .getTiles()
                            .size()),
                    () -> assertEquals(9, board.getBlock(new Position(6, 6))
                            .getTiles()
                            .size())
            );
        }

        @Test
        @DisplayName("Created board only contains current values >=0")
        void createdBoardOnlyContainsCurrentValuesGreaterThanOrEqualsZero() {
            Set<Integer> row0 = board.getRow(0)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row1 = board.getRow(1)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row2 = board.getRow(2)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row3 = board.getRow(3)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row4 = board.getRow(4)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row5 = board.getRow(5)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row6 = board.getRow(6)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row7 = board.getRow(7)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row8 = board.getRow(8)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());

            assertAll(
                    () -> assertTrue(Collections.min(row0) >= 0),
                    () -> assertTrue(Collections.min(row1) >= 0),
                    () -> assertTrue(Collections.min(row2) >= 0),
                    () -> assertTrue(Collections.min(row3) >= 0),
                    () -> assertTrue(Collections.min(row4) >= 0),
                    () -> assertTrue(Collections.min(row5) >= 0),
                    () -> assertTrue(Collections.min(row6) >= 0),
                    () -> assertTrue(Collections.min(row7) >= 0),
                    () -> assertTrue(Collections.min(row8) >= 0)
            );
        }

        @Test
        @DisplayName("Created board only contains current values <=9")
        void createdBoardOnlyContainsCurrentValuesLessThanOrEqualsNine() {
            Set<Integer> row0 = board.getRow(0)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row1 = board.getRow(1)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row2 = board.getRow(2)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row3 = board.getRow(3)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row4 = board.getRow(4)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row5 = board.getRow(5)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row6 = board.getRow(6)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row7 = board.getRow(7)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());
            Set<Integer> row8 = board.getRow(8)
                    .getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());

            assertAll(
                    () -> assertTrue(Collections.max(row0) <= 9),
                    () -> assertTrue(Collections.max(row1) <= 9),
                    () -> assertTrue(Collections.max(row2) <= 9),
                    () -> assertTrue(Collections.max(row3) <= 9),
                    () -> assertTrue(Collections.max(row4) <= 9),
                    () -> assertTrue(Collections.max(row5) <= 9),
                    () -> assertTrue(Collections.max(row6) <= 9),
                    () -> assertTrue(Collections.max(row7) <= 9),
                    () -> assertTrue(Collections.max(row8) <= 9)
            );
        }

        @Test
        @DisplayName("Created board only contains correct values >=1")
        void createdBoardOnlyContainsCorrectValuesGreaterThanOrEqualsToOne() {
            Set<Integer> row0 = board.getRow(0)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row1 = board.getRow(1)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row2 = board.getRow(2)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row3 = board.getRow(3)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row4 = board.getRow(4)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row5 = board.getRow(5)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row6 = board.getRow(6)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row7 = board.getRow(7)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row8 = board.getRow(8)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());

            assertAll(
                    () -> assertTrue(Collections.min(row0) >= 1),
                    () -> assertTrue(Collections.min(row1) >= 1),
                    () -> assertTrue(Collections.min(row2) >= 1),
                    () -> assertTrue(Collections.min(row3) >= 1),
                    () -> assertTrue(Collections.min(row4) >= 1),
                    () -> assertTrue(Collections.min(row5) >= 1),
                    () -> assertTrue(Collections.min(row6) >= 1),
                    () -> assertTrue(Collections.min(row7) >= 1),
                    () -> assertTrue(Collections.min(row8) >= 1)
            );
        }

        @Test
        @DisplayName("Created board only contains correct values <=9")
        void createdBoardOnlyContainsCorrectValuesLessThanOrEqualsNine() {
            Set<Integer> row0 = board.getRow(0)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row1 = board.getRow(1)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row2 = board.getRow(2)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row3 = board.getRow(3)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row4 = board.getRow(4)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row5 = board.getRow(5)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row6 = board.getRow(6)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row7 = board.getRow(7)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row8 = board.getRow(8)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());

            assertAll(
                    () -> assertTrue(Collections.max(row0) >= 9),
                    () -> assertTrue(Collections.max(row1) >= 9),
                    () -> assertTrue(Collections.max(row2) >= 9),
                    () -> assertTrue(Collections.max(row3) >= 9),
                    () -> assertTrue(Collections.max(row4) >= 9),
                    () -> assertTrue(Collections.max(row5) >= 9),
                    () -> assertTrue(Collections.max(row6) >= 9),
                    () -> assertTrue(Collections.max(row7) >= 9),
                    () -> assertTrue(Collections.max(row8) >= 9)
            );
        }

        @Test
        @DisplayName("The correct values in each row contain no duplicates")
        void correctValuesInRowsContainNoDuplicates() {
            Set<Integer> row0 = board.getRow(0)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row1 = board.getRow(1)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row2 = board.getRow(2)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row3 = board.getRow(3)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row4 = board.getRow(4)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row5 = board.getRow(5)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row6 = board.getRow(6)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row7 = board.getRow(7)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> row8 = board.getRow(8)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());

            assertAll(
                    () -> assertEquals(9, row0.size()),
                    () -> assertEquals(9, row1.size()),
                    () -> assertEquals(9, row2.size()),
                    () -> assertEquals(9, row3.size()),
                    () -> assertEquals(9, row4.size()),
                    () -> assertEquals(9, row5.size()),
                    () -> assertEquals(9, row6.size()),
                    () -> assertEquals(9, row7.size()),
                    () -> assertEquals(9, row8.size())
            );
        }

        @Test
        @DisplayName("The correct values in each column contain no duplicates")
        void correctValuesInColumnsContainNoDuplicates() {
            Set<Integer> column0 = board.getColumn(0)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> column1 = board.getColumn(1)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> column2 = board.getColumn(2)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> column3 = board.getColumn(3)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> column4 = board.getColumn(4)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> column5 = board.getColumn(5)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> column6 = board.getColumn(6)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> column7 = board.getColumn(7)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> column8 = board.getColumn(8)
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());

            assertAll(
                    () -> assertEquals(9, column0.size()),
                    () -> assertEquals(9, column1.size()),
                    () -> assertEquals(9, column2.size()),
                    () -> assertEquals(9, column3.size()),
                    () -> assertEquals(9, column4.size()),
                    () -> assertEquals(9, column5.size()),
                    () -> assertEquals(9, column6.size()),
                    () -> assertEquals(9, column7.size()),
                    () -> assertEquals(9, column8.size())
            );
        }

        @Test
        @DisplayName("The correct values in each block contain no duplicates")
        void correctValuesInBlocksContainNoDuplicates() {
            Set<Integer> block0 = board.getBlock(new Position(0, 0))
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> block1 = board.getBlock(new Position(0, 3))
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> block2 = board.getBlock(new Position(0, 6))
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> block3 = board.getBlock(new Position(3, 0))
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> block4 = board.getBlock(new Position(3, 3))
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> block5 = board.getBlock(new Position(3, 6))
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> block6 = board.getBlock(new Position(6, 0))
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> block7 = board.getBlock(new Position(6, 3))
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());
            Set<Integer> block8 = board.getBlock(new Position(6, 6))
                    .getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());

            assertAll(
                    () -> assertEquals(9, block0.size()),
                    () -> assertEquals(9, block1.size()),
                    () -> assertEquals(9, block2.size()),
                    () -> assertEquals(9, block3.size()),
                    () -> assertEquals(9, block4.size()),
                    () -> assertEquals(9, block5.size()),
                    () -> assertEquals(9, block6.size()),
                    () -> assertEquals(9, block7.size()),
                    () -> assertEquals(9, block8.size())
            );
        }
    }

    // FIXME: how to test that board is unique?
    // generated board is unique (get each tile from board to create int[][], use solver to solve?)
}