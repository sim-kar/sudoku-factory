import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Used to generate solutions to Sudoku puzzles, which are 9x9 grids that also contains nine 3x3
 * sub-grids, or blocks. A solved puzzle has a number between 1 and 9 in each tile in the grid, and
 * has no duplicate numbers in any of the rows, columns, or blocks on the grid.
 * <br>
 * If there are several solutions to a puzzle, the generated solution is random, i.e. two solutions
 * to the same puzzle are unlikely to be identical. Therefore, this solver can also be used to
 * generate filled in, random sudoku boards from which new Sudoku puzzles can be created.
 * <br>
 * Can also be used to check if a Sudoku puzzle is unique, in other words if there is only one
 * possible solution. This is a requirement for a valid Sudoku puzzle.
 */
public class SudokuSolver implements Solver {
    private final static int BOARD_START_INDEX = 0;
    private final static int BOARD_SIZE = 9;
    private final static int BLOCK_SIZE = 3;
    private final static int MAX_VALUE = 9;
    private final static int MIN_VALUE = 1;
    private final static int EMPTY = 0;
    private final Random random;

    /**
     * Create a new Sudoku solver with the given {@link Random}.
     *
     * @param random a Random used to generate solutions
     */
    public SudokuSolver(Random random) {
        this.random = random;
    }

    /**
     * {@inheritDoc}
     * <br>
     * For a Sudoku board, empty tiles are represented by 0, and must be replaced with a number
     * between 1 and 9 in the solution. Additionally, the numbers filled in numbers (1-9) in each
     * row, column and block (one of nine 3x3 sub-grids) on the board must be unique.
     * If there is no solution that conforms to these constraints, or if the given board isn't
     * valid (contains numbers outside of the range 0-9; isn't a 9x9 grid; is null) an
     * IllegalArgumentException is thrown.
     *
     * @param board the 9x9 Sudoku board to solve
     * @return the solved Sudoku board with a number 1-9 in each tile that is unique for each row,
     *         column and block.
     * @throws IllegalArgumentException if board doesn't have a solution, or if it is null,
     *                                  contains numbers outside the range 1-9, or isn't a 9x9 grid
     *
     */
    @Override
    public int[][] generate(int[][] board) throws IllegalArgumentException {
        validateBoard(board);

        int[][] solution = copyBoard(board);
        // try numbers in random order so that generated solutions will be different
        // that way a solver it can be used to generate new sudoku boards
        List<Integer> numbers = random.ints(MIN_VALUE, MAX_VALUE + 1)
                .distinct()
                .limit(MAX_VALUE)
                .boxed()
                .collect(Collectors.toList());

        if (solve(solution, numbers)) return solution;

        throw new IllegalArgumentException("There is no solution for the given board");
    }

    /**
     * Tries to generate a valid solution for the given Sudoku board. A valid solution is a solution
     * where every tile contains a number between 1 and 9, and there are no duplicate numbers in
     * any row, column, or 3x3 block.
     * If there isn't a valid solution for the board, it returns false.
     * The solution is generated by choosing numbers in the order of the given iterable. If there
     * are multiple solutions for the given board, different solutions can be found by changing the
     * order that numbers are tested.
     *
     * @param board the Sudoku board to try to solve
     * @param numbers an iterable containing the numbers 1-9. If there is more than one possible
     *                solution, the order of the numbers will influence the generated solution.
     * @return whether the board was solved or not
     */
    private boolean solve(int[][] board, Iterable<Integer> numbers) {
        for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
                if (board[row][column] == EMPTY) {

                    for (int number : numbers) {
                        board[row][column] = number;

                        if (isValid(board, row, column) && solve(board, numbers)) return true;

                        board[row][column] = EMPTY;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the tile at the specified row and column is valid, i.e. is a unique number between
     * 1-9 that is not in already in the tile's row, column or 3x3 block (or is empty).
     * Since empty tiles aren't unique they also count as valid.
     *
     * @param board the Sudoku board to use
     * @param row the row of the tile
     * @param column the of the tile
     * @return whether the number in the tile has no duplicates in its row, column or block
     */
    private boolean isValid(int[][] board, int row, int column) {
        return (isRowValid(board, row)
                && isColumnValid(board, column)
                && isBlockValid(board, row, column));
    }

    /**
     * Checks if a row is valid, i.e. it doesn't contain any duplicates.
     *
     * @param board the Sudoku board to use
     * @param row the row to check
     * @return whether the row contains duplicate numbers
     */
    private boolean isRowValid(int[][] board, int row) {
        boolean[] takenNumbers = new boolean[BOARD_SIZE];

        for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
            if (!isTileValid(board, row, column, takenNumbers)) return false;
        }
        return true;
    }

    /**
     * Checks if a column is valid, i.e. it doesn't contain any duplicates.
     *
     * @param board the Sudoku board to use
     * @param column the column to check
     * @return whether the column contains duplicate numbers
     */
    private boolean isColumnValid(int[][] board, int column) {
        boolean[] takenNumbers = new boolean[BOARD_SIZE];

        for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {

            if (!isTileValid(board, row, column, takenNumbers)) return false;
        }
        return true;
    }

    /**
     * Checks if a 3x3 block is valid, i.e. it doesn't contain any duplicates.
     *
     * @param board the Sudoku board to use
     * @param row the row of a tile in the block
     * @param column the column of a tile in the block
     * @return whether the block contains duplicate numbers
     */
    private boolean isBlockValid(int[][] board, int row, int column) {
        boolean[] takenNumbers = new boolean[BOARD_SIZE];
        /*
        dividing by the block size (floor division) gives the index of a block
        multiplying by the block size gives the real index the block starts at
        e.g.:
            indexes 0, 1, 2 divided by 3 is 0 -> 0 times 3 gives the starting index 0
            indexes 3, 4, 5 divided by 3 is 1 -> 1 times 3 gives the starting index 3
            etc.
         */
        int blockRowStart = (row / BLOCK_SIZE) * BLOCK_SIZE;
        int blockRowEnd = blockRowStart + BLOCK_SIZE;
        int blockColumnStart = (column / BLOCK_SIZE) * BLOCK_SIZE;
        int blockColumnEnd = blockColumnStart + BLOCK_SIZE;

        for (int blockRow = blockRowStart; blockRow < blockRowEnd; blockRow++) {
            for (int blockColumn = blockColumnStart; blockColumn < blockColumnEnd; blockColumn++) {
                if (!isTileValid(board, blockRow, blockColumn, takenNumbers)) return false;
            }
        }
        return true;
    }

    /**
     * Checks if the number in the tile at a specified row and column is valid, i.e. is a
     * unique number between 1-9 that is not already in another tile in the section
     * (row, column, or block) that is being checked, or is empty.
     * Since empty tiles aren't unique they also count as valid.
     * If there is an illegal number in the tile (<0 or >9) an exception will be thrown.
     *
     * @param board the Sudoku board to use
     * @param row the row of the tile
     * @param column the column of the tile
     * @param takenNumbers an array of booleans representing the taken numbers in a section
     *                     (row, column, or block) of the Sudoku board
     * @return whether the number in the tile is already taken
     * @throws IllegalArgumentException if the number in the tile at row and column is <0 or >9
     */
    private boolean isTileValid(
            int[][] board,
            int row,
            int column,
            boolean[] takenNumbers
    ) throws IllegalArgumentException {
        if ((board[row][column] > MAX_VALUE) || (board[row][column] < EMPTY)) {
            throw new IllegalArgumentException("Boards can only contain numbers 0-9");
        }

        if (board[row][column] != EMPTY) {
            // the number acts as index to takenNumbers, but needs to be converted from 1-9 to 0-8
            int index = board[row][column] - 1;

            if (!takenNumbers[index]) {
                takenNumbers[index] = true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * <br>
     * The board must be a valid 9x9 Sudoku board, with tiles that are either empty (0), or has
     * a number between 1 and 9.
     *
     * @param board the Sudoku board to check if it only has one solution
     * @return
     * @throws IllegalArgumentException if the board is null, the wrong size, or contains a number
     *                                  outside the range 0-9
     */
    @Override
    public boolean isUnique(int[][] board) throws IllegalArgumentException {
        validateBoard(board);

        int[][] ascendingSolution = copyBoard(board);
        int[][] descendingSolution = copyBoard(board);
        List<Integer> ascendingNumbers = IntStream.range(MIN_VALUE, MAX_VALUE + 1)
                .boxed()
                .collect(Collectors.toList());
        List<Integer> descendingNumbers = new ArrayList<>(ascendingNumbers);
        Collections.reverse(descendingNumbers);

        if (solve(ascendingSolution, ascendingNumbers)
                && solve(descendingSolution, descendingNumbers)) {
            return Arrays.deepEquals(ascendingSolution, descendingSolution);
        }

        throw new IllegalArgumentException("There is no solution for the given board");
    }

    /**
     * Throws an exception if the board is the wrong size, or if it is null.
     *
     * @param board the Sudoku board to validate
     * @throws IllegalArgumentException if the board is null, or has the wrong number of rows or
     *                                  columns
     */
    private void validateBoard(int[][] board) throws IllegalArgumentException {
        if (board == null) {
            throw new IllegalArgumentException("Cannot generate solution for a null board");
        }

        if (board.length != BOARD_SIZE) {
            throw new IllegalArgumentException("Board must have 9 rows");
        }

        for (int[] row : board) {
            if (row.length != BOARD_SIZE) {
                throw new IllegalArgumentException("Board must have 9 columns");
            }
        }
    }

    /**
     * Creates a new copy of a two-dimensional array of ints.
     *
     * @param board the array to copy
     * @return a copy of the array
     */
    private int[][] copyBoard(int[][] board) {
        return Arrays.stream(board)
                .map(int[]::clone)
                .toArray(int[][]::new);
    }
}
