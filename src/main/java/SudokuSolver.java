import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class SudokuSolver implements Solver {
    private final Random random;
    private int[][] solution;

    public SudokuSolver(Random random) {
        this.random = random;
    }

    @Override
    public int[][] generate(int[][] board) {
        if (board == null) {
            throw new IllegalArgumentException("Cannot generate solution for a null board");
        }

        int[][] newBoard = Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);

        if (solve(newBoard)) {
            return this.solution;
        }

        throw new IllegalArgumentException("There is no solution for the given board");
    }

    private boolean solve(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (board[row][column] == 0) {
                    List<Integer> numbers = new ArrayList<>(
                            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)
                    );
                    while (numbers.size() > 0) {
                        int index = random.nextInt(numbers.size());
                        int number = numbers.get(index);
                        numbers.remove(index);
                        board[row][column] = number;

                        if (isValid(board, row, column) && solve(board)) {
                            this.solution = board;
                            return true;
                        }

                        board[row][column] = 0;
                    }

                    return false;
                }
            }
        }

        this.solution = board;
        return true;
    }

    private boolean isValid(int[][] board, int row, int column) {
        return (rowConstraint(board, row)
                && columnConstraint(board, column)
                && blockConstraint(board, row, column));
    }

    private boolean rowConstraint(int[][] board, int row) {
        boolean[] constraint = new boolean[9];
        return IntStream.range(0, 9)
                .allMatch(column -> checkConstraint(board, row, constraint, column));
    }

    private boolean columnConstraint(int[][] board, int column) {
        boolean[] constraint = new boolean[9];
        return IntStream.range(0, 9)
                .allMatch(row -> checkConstraint(board, row, constraint, column));
    }

    private boolean blockConstraint(int[][] board, int row, int column) {
        boolean[] constraint = new boolean[9];
        int blockRowStart = (row / 3) * 3;
        int blockRowEnd = blockRowStart + 3;

        int blockColumnStart = (column / 3) * 3;
        int blockColumnEnd = blockColumnStart + 3;

        for (int r = blockRowStart; r < blockRowEnd; r++) {
            for (int c = blockColumnStart; c < blockColumnEnd; c++) {
                if (!checkConstraint(board, r, constraint, c)) return false;
            }
        }
        return true;
    }

    boolean checkConstraint(
            int[][] board,
            int row,
            boolean[] constraint,
            int column
    ) {
        if ((board[row][column] > 9) || (board[row][column] < 0)) {
            throw new IllegalArgumentException("Boards can only contain numbers 1-9");
        }
        if (board[row][column] != 0) {
            if (!constraint[board[row][column] - 1]) {
                constraint[board[row][column] - 1] = true;
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
