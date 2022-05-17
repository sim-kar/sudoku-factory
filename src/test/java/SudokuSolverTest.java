import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class SudokuSolverTest {
    Solver solver;
    int[][] empty;

    @BeforeEach
    void setup() {
        Random random = new Random(0L);
        solver = new SudokuSolver(random);
        empty = new int[9][9];
        for (int[] row : empty) {
            Arrays.fill(row, 0);
        }
    }

    @Test
    @DisplayName("Generating a solution with null input throws an error")
    void nullInputToGenerateSolutionThrowsError() {
        assertThrows(IllegalArgumentException.class, () -> solver.generate(null));
    }

    @Test
    @DisplayName("Generating a solution from input with numbers >9 throws error")
    void numbersGreaterThanNineInInputToGenerateSolutionThrowsError() {
        empty[1][1] = 10;
        assertThrows(IllegalArgumentException.class, () -> solver.generate(empty));
    }

    @Test
    @DisplayName("Generating a solution from input with numbers <0 throws error")
    void numbersLessThanZeroInInputToGenerateSolutionThrowsError() {
        empty[1][1] = -1;
        assertThrows(IllegalArgumentException.class, () -> solver.generate(empty));
    }

    @Test
    @DisplayName("Generating a solution from input with duplicates in row throws error")
    void duplicatesInRowInInputToGenerateSolutionThrowsError() {
        empty[0][0] = 1;
        empty[0][8] = 1;
        assertThrows(IllegalArgumentException.class, () -> solver.generate(empty));
    }

    @Test
    @DisplayName("Generating a solution from input with duplicates in column throws error")
    void duplicatesInColumnInInputToGenerateSolutionThrowsError() {
        empty[0][0] = 1;
        empty[8][0] = 1;
        assertThrows(IllegalArgumentException.class, () -> solver.generate(empty));
    }

    @Test
    @DisplayName("Generating a solution from input with duplicates in block throws error")
    void duplicatesInBlockInInputToGenerateSolutionThrowsError() {
        empty[0][0] = 1;
        empty[2][2] = 1;
        assertThrows(IllegalArgumentException.class, () -> solver.generate(empty));
    }

    @Test
    @DisplayName("Generating a solution from board that is too small throws error")
    void tooSmallInputToGenerateSolutionThrowsError() {
        empty = new int[9][8];
        assertThrows(IllegalArgumentException.class, () -> solver.generate(empty));
    }

    @Test
    @DisplayName("Generating a solution from board that is too large throws error")
    void tooLargeInputToGenerateSolutionThrowsError() {
        empty = new int[10][10];
        assertThrows(IllegalArgumentException.class, () -> solver.generate(empty));
    }

    @Nested
    @DisplayName("Sudoku Rules")
    class SudokuRulesTest {
        Solver solver;
        int[][] empty;
        int[][] solution;

        @BeforeEach
        void setup() {
            Random random = new Random(0L);
            solver = new SudokuSolver(random);
            empty = new int[9][9];
            for (int[] row : empty) {
                Arrays.fill(row, 0);
            }
            solution = solver.generate(empty);
        }

        @Test
        @DisplayName("Generated solution only contains numbers 1-9")
        void solutionOnlyHasNumbersOneToNine() {
            Set<Integer> numbers = new HashSet<>();
            for (int[] row : solution) {
                for (int value : row) {
                    numbers.add(value);
                }
            }

            assertAll(
                    () -> assertTrue(Collections.max(numbers) <= 9),
                    () -> assertTrue(Collections.min(numbers) >= 1)
            );
        }


        @Test
        @DisplayName("Rows in generated solution have no duplicates")
        void rowsInSolutionHasNoDuplicates() {
            assertAll(
                    () -> assertEquals(9, Arrays.stream(solution[0])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9,Arrays.stream(solution[1])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9, Arrays.stream(solution[2])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9, Arrays.stream(solution[3])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9, Arrays.stream(solution[4])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9, Arrays.stream(solution[5])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9, Arrays.stream(solution[6])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9, Arrays.stream(solution[7])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9, Arrays.stream(solution[8])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size())
            );
        }

        @Test
        @DisplayName("Columns in generated solution have no duplicates")
        void columnsInSolutionHasNoDuplicates() {
            assertAll(
                    () -> assertEquals(9, IntStream.range(0, 9)
                            .map(row -> solution[row][0])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9, IntStream.range(0, 9)
                            .map(row -> solution[row][1])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9, IntStream.range(0, 9)
                            .map(row -> solution[row][2])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9, IntStream.range(0, 9)
                            .map(row -> solution[row][3])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9, IntStream.range(0, 9)
                            .map(row -> solution[row][4])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9, IntStream.range(0, 9)
                            .map(row -> solution[row][5])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9, IntStream.range(0, 9)
                            .map(row -> solution[row][6])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9, IntStream.range(0, 9)
                            .map(row -> solution[row][7])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size()),
                    () -> assertEquals(9, IntStream.range(0, 9)
                            .map(row -> solution[row][8])
                            .boxed()
                            .collect(Collectors.toSet())
                            .size())
            );
        }

        @Test
        @DisplayName("Blocks in generated solution have no duplicates")
        void blocksInSolutionHasNoDuplicates() {
            Map<Position, Set<Integer>> blocks = Map.of(
                    new Position(0, 0), new HashSet<>(),
                    new Position(0, 1), new HashSet<>(),
                    new Position(0, 2), new HashSet<>(),
                    new Position(1, 0), new HashSet<>(),
                    new Position(1, 1), new HashSet<>(),
                    new Position(1, 2), new HashSet<>(),
                    new Position(2, 0), new HashSet<>(),
                    new Position(2, 1), new HashSet<>(),
                    new Position(2, 2), new HashSet<>()
            );
            for (int row = 0; row < 9; row++) {
                for (int column = 0; column < 9; column++) {
                    // dividing the row and column index by 3 (floor division) gets the index
                    // of the correct block
                    Position xy = new Position(row / 3, column / 3);

                    blocks.get(xy).add(solution[row][column]);
                }
            }

            assertAll(
                    () -> assertEquals(9, blocks.get(new Position(0, 0)).size()),
                    () -> assertEquals(9, blocks.get(new Position(0, 1)).size()),
                    () -> assertEquals(9, blocks.get(new Position(0, 2)).size()),
                    () -> assertEquals(9, blocks.get(new Position(1, 0)).size()),
                    () -> assertEquals(9, blocks.get(new Position(1, 1)).size()),
                    () -> assertEquals(9, blocks.get(new Position(1, 2)).size()),
                    () -> assertEquals(9, blocks.get(new Position(2, 0)).size()),
                    () -> assertEquals(9, blocks.get(new Position(2, 1)).size()),
                    () -> assertEquals(9, blocks.get(new Position(2, 2)).size())
            );
        }
    }


    /**
     * Generating a solution can be used to generate a new Sudoku board from an empty board, but
     * only if the generated solution is random. If the solution is always the same only the same
     * board will be generated!
     *
     * This test is to make sure that two generated solutions are not the same, by using a Random
     * with different seed to generate two solutions and comparing them.
     */
    @Test
    @DisplayName("Numbers in generated solution are randomized")
    void solutionIsRandomized() {
        int[][] solution = solver.generate(empty);

        // generate another solution from a Random with a different seed
        Random random2 = new Random(1L);
        Solver solver2 = new SudokuSolver(random2);
        int[][] solution2 = solver2.generate(empty);

        assertNotEquals(solution2, solution);
    }

    @Test
    @DisplayName("Solve a 17 hint Sudoku puzzle")
    void solveDifficultSudoku() {
        int[][] puzzle = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 8, 1},
                {0, 3, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 7, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 5, 0, 6, 2, 0},
                {4, 0, 0, 0, 9, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 6, 0, 4, 0, 0, 3, 0, 0},
                {5, 0, 0, 0, 0, 8, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0}
        };
        int[][] solution = solver.generate(puzzle);

        for (int[] row : solution) {
            for (int column : row) {
                System.out.print(column + " ");
            }
            System.out.println();
        }

        assertNotEquals(puzzle, solution);
    }
}