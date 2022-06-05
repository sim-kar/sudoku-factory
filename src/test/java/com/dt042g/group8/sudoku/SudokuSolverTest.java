package com.dt042g.group8.sudoku;

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
    @Nested
    @DisplayName("Generating a solution")
    class GeneratingSolutionTest {
        Solver solver;
        int[][] empty;

        /**
         * Creates an empty 9x9 board.
         */
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
        @DisplayName("Generating a solution from board with wrong column size throws error")
        void tooSmallInputToGenerateSolutionThrowsError() {
            empty = new int[9][8];
            assertThrows(IllegalArgumentException.class, () -> solver.generate(empty));
        }

        @Test
        @DisplayName("Generating a solution from board with wrong row size throws error")
        void tooLargeInputToGenerateSolutionThrowsError() {
            empty = new int[10][9];
            assertThrows(IllegalArgumentException.class, () -> solver.generate(empty));
        }

        /**
         * Generating a solution can be used to generate a new Sudoku board from an empty board, but
         * only if the generated solution is random. If the solution is always the same only the
         * same board can be generated!
         * <br>
         * This test is to make sure that two generated solutions are not the same, by using a
         * Random with different seed to generate two solutions and comparing them.
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

        @Nested
        @DisplayName("Conforms to Sudoku rules")
        class SudokuRulesTest {
            Solver solver;
            int[][] empty;
            int[][] solution;

            /**
             * Creates a 9x9 Sudoku board to test that it conforms to Sudoku rules.
             */
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


            /**
             * Creates a set from the numbers in each row. Since there are 9 numbers in each
             * row, if any set doesn't have exactly 9 numbers then there must be a duplicate,
             */
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

            /**
             * Creates a set from the numbers in each column. Since there are 9 numbers in each
             * column, if any set doesn't have exactly 9 numbers then there must be a duplicate.
             */
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

            /**
             * Creates a set from the numbers in each block. Since there are 9 numbers in each
             * block, if any set doesn't have exactly 9 numbers then there must be a duplicate.
             *
             * To get the blocks, a hashmap with a position for each block is used:
             * <br>
             * (0, 0) (1, 0) (2, 0) <br>
             * (0, 1) (1, 1) (2, 1) <br>
             * (0, 2) (1, 2) (2, 2) <br>
             *
             * All numbers in a block are added to the set at the corresponding position in the map.
             */
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
    }

    @Nested
    @DisplayName("Checking if a Sudoku board is unique")
    class BoardIsUniqueTest {
        Solver solver;
        int[][] board;

        /**
         * Sets up an empty 9x9 board.
         */
        @BeforeEach
        void setup() {
            Random random = new Random(0L);
            solver = new SudokuSolver(random);
            board = new int[9][9];
            for (int[] row : board) {
                Arrays.fill(row, 0);
            }
        }

        @Test
        @DisplayName("Checking if a board is unique with null input throws an error")
        void nullBoardThrowsError() {
            assertThrows(IllegalArgumentException.class, () -> solver.isUnique(null));
        }

        @Test
        @DisplayName("Checking if a board with numbers >9 is unique throws error")
        void boardWithNumbersGreaterThanNineThrowsError() {
            board[0][0] = 10;
            assertThrows(IllegalArgumentException.class, () -> solver.isUnique(board));
        }

        @Test
        @DisplayName("Checking if a board with numbers <0 is unique throws error")
        void boardWithNumbersLessThanZeroThrowsError() {
            board[0][0] = -1;
            assertThrows(IllegalArgumentException.class, () -> solver.isUnique(board));
        }

        @Test
        @DisplayName("Checking if a board with duplicates in row is unique throws error")
        void boardWithDuplicatesInRowThrowsError() {
            board[0][0] = 1;
            board[0][1] = 1;
            assertThrows(IllegalArgumentException.class, () -> solver.isUnique(board));
        }

        @Test
        @DisplayName("Checking if a board with duplicates in column is unique throws error")
        void boardWithDuplicatesInColumnThrowsError() {
            board[0][0] = 1;
            board[1][0] = 1;
            assertThrows(IllegalArgumentException.class, () -> solver.isUnique(board));
        }


        @Test
        @DisplayName("Checking if a board with duplicates in block is unique throws error")
        void boardWithDuplicatesInBlockThrowsError() {
            board[0][0] = 1;
            board[1][1] = 1;
            assertThrows(IllegalArgumentException.class, () -> solver.isUnique(board));
        }


        @Test
        @DisplayName("Checking if a board with wrong column size is unique throws error")
        void boardWithWrongColumnSizeThrowsError() {
            board = new int[9][8];
            assertThrows(IllegalArgumentException.class, () -> solver.isUnique(board));
        }

        @Test
        @DisplayName("Checking if a board with wrong row size is unique throws error")
        void boardWithWrongRowSizeThrowsError() {
            board = new int[10][9];
            assertThrows(IllegalArgumentException.class, () -> solver.isUnique(board));
        }

        /**
         * Uses this unsolvable board:
         * <br>
         * 5, 1, 6, 8, 4, 9, 7, 3, 2 <br>
         * 3, 0, 7, 6, 0, 5, 0, 0, 0 <br>
         * 8, 0, 9, 7, 0, 0, 0, 6, 5 <br>
         * 1, 3, 5, 0, 6, 0, 9, 0, 7 <br>
         * 4, 7, 2, 5, 9, 1, 0, 0, 6 <br>
         * 9, 6, 8, 3, 7, 0, 0, 5, 0 <br>
         * 2, 5, 3, 1, 8, 6, 0, 7, 4 <br>
         * 6, 8, 4, 2, 0, 7, 5, 0, 0 <br>
         * 7, 9, 1, 0, 5, 0, 6, 0, 8 <br>
         * <br>
         * Taken from <a href="https://sudokudragon.com/unsolvable.htm">here</a>.
         */
        @Test
        @DisplayName("Checking if a board with no possible solution is unique throws error")
        void boardWithNoSolutionReturnsFalse() {
            board = new int[][]{
                    {5, 1, 6, 8, 4, 9, 7, 3, 2},
                    {3, 0, 7, 6, 0, 5, 0, 0, 0},
                    {8, 0, 9, 7, 0, 0, 0, 6, 5},
                    {1, 3, 5, 0, 6, 0, 9, 0, 7},
                    {4, 7, 2, 5, 9, 1, 0, 0, 6},
                    {9, 6, 8, 3, 7, 0, 0, 5, 0},
                    {2, 5, 3, 1, 8, 6, 0, 7, 4},
                    {6, 8, 4, 2, 0, 7, 5, 0, 0},
                    {7, 9, 1, 0, 5, 0, 6, 0, 8}
            };

            assertThrows(IllegalArgumentException.class, () -> solver.isUnique(board));
        }

        /**
         * Uses this board with a single solution:
         * <br>
         * 5, 3, 0, 0, 7, 0, 0, 0, 0 <br>
         * 6, 0, 0, 1, 9, 5, 0, 0, 0 <br>
         * 0, 9, 8, 0, 0, 0, 0, 6, 0 <br>
         * 8, 0, 0, 0, 6, 0, 0, 0, 3 <br>
         * 4, 0, 0, 8, 0, 3, 0, 0, 1 <br>
         * 7, 0, 0, 0, 2, 0, 0, 0, 6 <br>
         * 0, 6, 0, 0, 0, 0, 2, 8, 0 <br>
         * 0, 0, 0, 4, 1, 9, 0, 0, 5 <br>
         * 0, 0, 0, 0, 8, 0, 0, 7, 9 <br>
         * <br>
         * Taken from taken from <a href="https://en.wikipedia.org/wiki/Sudoku">here</a>.
         */
        @Test
        @DisplayName("Checking if a board with one solution is unique returns true")
        void boardWithOneSolutionReturnsTrue() {
            board = new int[][]{
                    {5, 3, 0, 0, 7, 0, 0, 0, 0},
                    {6, 0, 0, 1, 9, 5, 0, 0, 0},
                    {0, 9, 8, 0, 0, 0, 0, 6, 0},
                    {8, 0, 0, 0, 6, 0, 0, 0, 3},
                    {4, 0, 0, 8, 0, 3, 0, 0, 1},
                    {7, 0, 0, 0, 2, 0, 0, 0, 6},
                    {0, 6, 0, 0, 0, 0, 2, 8, 0},
                    {0, 0, 0, 4, 1, 9, 0, 0, 5},
                    {0, 0, 0, 0, 8, 0, 0, 7, 9}
            };

            assertTrue(solver.isUnique(board));
        }

        /**
         * Uses this board with multiple solutions:
         * <br>
         * 0, 8, 0, 0, 0, 9, 7, 4, 3 <br>
         * 0, 5, 0, 0, 0, 8, 0, 1, 0 <br>
         * 0, 1, 0, 0, 0, 0, 0, 0, 0 <br>
         * 8, 0, 0, 0, 0, 5, 0, 0, 0 <br>
         * 0, 0, 0, 8, 0, 4, 0, 0, 0 <br>
         * 0, 0, 0, 3, 0, 0, 0, 0, 6 <br>
         * 0, 0, 0, 0, 0, 0, 0, 7, 0 <br>
         * 0, 3, 0, 5, 0, 0, 0, 8, 0 <br>
         * 9, 7, 2, 4, 0, 0, 0, 5, 0 <br>
         * <br>
         * Taken from <a href="https://sudokudragon.com/unsolvable.htm">here</a>.
         */
        @Test
        @DisplayName("Checking if a board with multiple solution is unique returns false")
        void boardWithMultipleSolutionReturnsFalse() {
            board = new int[][]{
                    {0, 8, 0, 0, 0, 9, 7, 4, 3},
                    {0, 5, 0, 0, 0, 8, 0, 1, 0},
                    {0, 1, 0, 0, 0, 0, 0, 0, 0},
                    {8, 0, 0, 0, 0, 5, 0, 0, 0},
                    {0, 0, 0, 8, 0, 4, 0, 0, 0},
                    {0, 0, 0, 3, 0, 0, 0, 0, 6},
                    {0, 0, 0, 0, 0, 0, 0, 7, 0},
                    {0, 3, 0, 5, 0, 0, 0, 8, 0},
                    {9, 7, 2, 4, 0, 0, 0, 5, 0},
            };

            assertFalse(solver.isUnique(board));
        }
    }
}