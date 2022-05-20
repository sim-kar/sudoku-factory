import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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

    /**
     * 17 is the lowest limit of clues (filled in numbers in a Sudoku puzzle) where it is possible
     * to have a single solution. Finding a new 17 hint puzzle is an astronomical challenge. For
     * our program to run decently fast we set the lower limit a big higher.
     */
    @Test
    @DisplayName("Creating board with less than 25 clues throws exception")
    void creatingBoardWithLessThan25CluesThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(24));
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
        for (int i = 0; i < 9; i++) {
            Section row = board.getRow(i);

            for (Tile tile : row.getTiles()) {
                int x = tile.getPosition().getX();
                int y = tile.getPosition().getY();
                matrixOfBoard[y][x] = tile.getCurrentValue();
            }
        }

        int[][] matrixOfBoard2 = new int[9][9];
        for (int i = 0; i < 9; i++) {
            Section row = board2.getRow(i);

            for (Tile tile : row.getTiles()) {
                int x = tile.getPosition().getX();
                int y = tile.getPosition().getY();
                matrixOfBoard2[y][x] = tile.getCurrentValue();
            }
        }

        assertNotEquals(matrixOfBoard, matrixOfBoard2);
    }

    @Test
    @DisplayName("Created boards are unique")
    void createdBoardsAreUnique() {
        Board board = factory.create(30);

        int[][] matrixOfBoard = new int[9][9];
        for (int i = 0; i < 9; i++) {
            Section row = board.getRow(i);

            for (Tile tile : row.getTiles()) {
                int x = tile.getPosition().getX();
                int y = tile.getPosition().getY();
                matrixOfBoard[y][x] = tile.getCurrentValue();
            }
        }

        assertTrue(solver.isUnique(matrixOfBoard));
    }

    // Using the test instance annotation allows the use non-static methods in parameterized tests
    // which allows us to use such tests in this nested class
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("Conforms to Sudoku rules")
    class SudokuRulesTest {
        Random random = new Random(0L);
        Solver solver = new SudokuSolver(random);
        Factory factory = new SudokuFactory(solver);
        Board board = factory.create(40);

        Set<Section> rows() {
            Set<Section> rows = new HashSet<>();
            for (int i = 0; i < 9; i++) {
                rows.add(board.getRow(i));
            }
            return rows;
        }

        Set<Section> columns() {
            Set<Section> columns = new HashSet<>();
            for (int i = 0; i < 9; i++) {
                columns.add(board.getColumn(i));
            }
            return columns;
        }

        Set<Section> blocks() {
            Set<Section> blocks = new HashSet<>();
            for (int y = 0; y <= 6; y += 3) {
                for (int x = 0; x <= 6; x += 3) {
                    blocks.add(board.getBlock(new Position(x, y)));
                }
            }
            return blocks;
        }


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

        @ParameterizedTest
        @MethodSource(value = "rows")
        @DisplayName("Each row on created board has nine tiles")
        void rowsOnBoardHaveNineTiles(Section row) {
            assertEquals(9, row.getTiles().size());
        }

        @ParameterizedTest
        @MethodSource(value = "columns")
        @DisplayName("Each column on created board has nine tiles")
        void columnsOnBoardHaveNineTiles(Section column) {
            assertEquals(9, column.getTiles().size());
        }

        @ParameterizedTest
        @MethodSource(value = "blocks")
        @DisplayName("Each block on created board has nine tiles")
        void blocksOnBoardHaveNineTiles(Section block) {
            assertEquals(9, block.getTiles().size());
        }

        @ParameterizedTest
        @MethodSource(value = "rows")
        @DisplayName("Created board only contains current values >=0")
        void createdBoardOnlyContainsCurrentValuesGreaterThanOrEqualsZero(Section row) {
            Set<Integer> currentValues = row.getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());

            assertTrue(Collections.min(currentValues) >= 0);
        }

        @ParameterizedTest
        @MethodSource(value = "rows")
        @DisplayName("Created board only contains current values <=9")
        void createdBoardOnlyContainsCurrentValuesLessThanOrEqualsNine(Section row) {
            Set<Integer> currentValues = row.getTiles()
                    .stream()
                    .map(Tile::getCurrentValue)
                    .collect(Collectors.toSet());

            assertTrue(Collections.max(currentValues) <= 9);
        }

        @ParameterizedTest
        @MethodSource(value = "rows")
        @DisplayName("Created board only contains correct values >=1")
        void createdBoardOnlyContainsCorrectValuesGreaterThanOrEqualsToOne(Section row) {
            Set<Integer> correctValues = row.getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());

            assertTrue(Collections.min(correctValues) >= 1);
        }

        @ParameterizedTest
        @MethodSource(value = "rows")
        @DisplayName("Created board only contains correct values <=9")
        void createdBoardOnlyContainsCorrectValuesLessThanOrEqualsNine(Section row) {
            Set<Integer> correctValues = row.getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());

            assertTrue(Collections.max(correctValues) <= 9);
        }

        @ParameterizedTest
        @MethodSource(value = "rows")
        @DisplayName("The correct values in each row contain no duplicates")
        void correctValuesInRowsContainNoDuplicates(Section row) {
            Set<Integer> correctValues = row.getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());

            assertEquals(9, correctValues.size());
        }

        @ParameterizedTest
        @MethodSource(value = "columns")
        @DisplayName("The correct values in each column contain no duplicates")
        void correctValuesInColumnsContainNoDuplicates(Section column) {
            Set<Integer> correctValues = column.getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());

            assertEquals(9, correctValues.size());
        }

        @ParameterizedTest
        @MethodSource(value = "blocks")
        @DisplayName("The correct values in each block contain no duplicates")
        void correctValuesInBlocksContainNoDuplicates(Section block) {
            Set<Integer> correctValues = block.getTiles()
                    .stream()
                    .map(Tile::getCorrectValue)
                    .collect(Collectors.toSet());

            assertEquals(9, correctValues.size());
        }
    }
}