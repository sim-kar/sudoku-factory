import java.util.Arrays;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;

public class SudokuSolver implements Solver {
    private final static int BOARD_START_INDEX = 0;
    private final static int BOARD_SIZE = 9;
    private final static int BLOCK_SIZE = 3;
    private final static int MAX_VALUE = 9;
    private final static int MIN_VALUE = 1;
    private final static int EMPTY = 0;
    private final Random random;

    public SudokuSolver(Random random) {
        this.random = random;
    }

    @Override
    public int[][] generate(int[][] board) {
        if (board == null) {
            throw new IllegalArgumentException("Cannot generate solution for a null board");
        }

        int[][] solution = Arrays.stream(board)
                .map(int[]::clone)
                .toArray(int[][]::new);

        if (solve(solution)) return solution;

        throw new IllegalArgumentException("There is no solution for the given board");
    }

    private boolean solve(int[][] board) {
        for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
                if (board[row][column] == EMPTY) {
                    // try numbers in random order so that generated solutions will be different
                    // that way a solver it can be used to generate new sudoku boards
                    Stack<Integer> nums = random.ints(MIN_VALUE, MAX_VALUE + 1)
                            .distinct()
                            .limit(MAX_VALUE)
                            .boxed()
                            .collect(Collectors.toCollection(Stack::new));

                    while (nums.size() > 0) {
                        board[row][column] = nums.pop();

                        if (isValid(board, row, column) && solve(board)) return true;

                        board[row][column] = EMPTY;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int column) {
        return (isRowValid(board, row)
                && isColumnValid(board, column)
                && isBlockValid(board, row, column));
    }

    private boolean isRowValid(int[][] board, int row) {
        boolean[] takenNumbers = new boolean[BOARD_SIZE];

        for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
            if (!isTileValid(board, row, column, takenNumbers)) return false;
        }
        return true;
    }

    private boolean isColumnValid(int[][] board, int column) {
        boolean[] takenNumbers = new boolean[BOARD_SIZE];

        for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {

            if (!isTileValid(board, row, column, takenNumbers)) return false;
        }
        return true;
    }

    private boolean isBlockValid(int[][] board, int row, int column) {
        boolean[] takenNumbers = new boolean[BOARD_SIZE];
        /*
        dividing by the block size (floor division) gives the index of a block
        multiplying by the block size gives the real index the block starts at
        e.g.:
            0, 1, 2 divided by 3 is 0 -> 0 times 3 gives the starting index 0
            3, 4, 5 divided by 3 is 1 -> 1 times 3 gives the starting index 3
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

    @Override
    public boolean isUnique(int[][] board) {
        return false;
    }
}
