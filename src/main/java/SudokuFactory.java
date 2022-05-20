import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SudokuFactory implements Factory {
    // FIXME: does minimum clues have to be increased?
    //  Some testing suggests that anything less than 23 takes too long to be practical
    //  Using 25 or more reduces the variance in time taken to create a puzzle even more
    private static final int MIN_CLUES = 17;
    private static final int MAX_CLUES = 81;
    private static final int EMPTY = 0;
    private final Solver solver;

    public SudokuFactory(Solver solver) {
        this.solver = solver;
    }

    @Override
    public Board create(int clues) {
        if (clues < MIN_CLUES) {
            throw new IllegalArgumentException("There must be at least 17 clues");
        }

        if (clues > MAX_CLUES) {
            throw new IllegalArgumentException("There cannot be more than 81 clues");
        }

        List<Position> positions = getPositions();
        int[][] empty = new int[9][9];
        int[][] solution;
        int[][] puzzle;
        int currentClues;

        // it's possible to generate a random solution that doesn't have a unique puzzle
        // with the given amount of clues. If that is the case we try again with a new solution
        do {
            currentClues = MAX_CLUES;
            solution = solver.generate(empty);
            puzzle = copy2DArray(solution);

            // shuffle the order that positions are removed each time, or it will always remove
            // the tiles in the same position, and in the same order, every time
            Collections.shuffle(positions);

            for (Position position : positions) {
                if (currentClues == clues) break;

                int x = position.getX();
                int y = position.getY();
                int current = puzzle[y][x];
                puzzle[y][x] = EMPTY;

                if (solver.isUnique(puzzle)) {
                    currentClues--;
                } else {
                    puzzle[y][x] = current;
                }
            }
        } while (currentClues > clues);

        return getBoard(solution, puzzle);
    }

    // get a list of all x, y positions on a 9x9 board
    private List<Position> getPositions() {
        List<Position> positions = new ArrayList<>();

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                positions.add(new Position(x, y));
            }
        }

        return positions;
    }

    // get a copy of a 2d array
    private int[][] copy2DArray(int[][] board) {
        return Arrays.stream(board)
                .map(int[]::clone)
                .toArray(int[][]::new);
    }

    // get a Sudoku board from a solution containing all the correct values, and a puzzle with
    // containing empty tiles
    private Board getBoard(int[][] solution, int[][] puzzle) {
        List<Set<Tile>> tilesInRows = Stream.generate(HashSet<Tile>::new)
                .limit(9)
                .collect(Collectors.toCollection(ArrayList::new));
        List<Set<Tile>> tilesInColumns = Stream.generate(HashSet<Tile>::new)
                .limit(9)
                .collect(Collectors.toCollection(ArrayList::new));
        List<Set<Tile>> tilesInBlocks = Stream.generate(HashSet<Tile>::new)
                .limit(9)
                .collect(Collectors.toCollection(ArrayList::new));

        List<Position> positions = getPositions();
        for (Position position : positions) {
            int x = position.getX();
            int y = position.getY();
            int blockIndex = getBlockIndex(x, y);
            int value = solution[y][x];

            Tile tile = new SudokuTile(value, position);
            if (puzzle[y][x] == EMPTY) {
                tile.setEditable(true);
                tile.clear();
            }

            tilesInRows.get(y).add(tile);
            tilesInColumns.get(x).add(tile);
            tilesInBlocks.get(blockIndex).add(tile);
        }

        Section[] rows = new Section[9];
        Section[] columns = new Section[9];
        Section[] blocks = new Section[9];

        for (int i = 0; i < 9; i++) {
            rows[i] = new SudokuSection(tilesInRows.get(i));
            columns[i] = new SudokuSection(tilesInColumns.get(i));
            blocks[i] = new SudokuSection(tilesInBlocks.get(i));
        }

        return new SudokuBoard(rows, columns, blocks);
    }

    // get the index of a block 0-8 in linear order
    private int getBlockIndex(int x, int y) {
        /*
        Indexes of all blocks:
        | 0 | 1 | 2 |
        | 3 | 4 | 5 |
        | 6 | 7 | 8 |

        The y-value divided by 3 (floor division) gives us a starting index
        Adding the x-value divided by 3 (floor division) gives us the block index

        Ex: the tiles in block 4 have the positions {3, 3} to {5, 5}
            Position {3, 3}:
            row starting index: (3 // 3) * 3 = 3
            column offset:      3 // 3       = 1
            block index:        3 + 1        = 4

            Position {5, 5}:
            row starting index: (5 // 3) * 3 = 3
            column offset:      5 // 3       = 1
            block index:        3 + 1        = 4
         */
        int blockRowStartingIndex = (y / 3) * 3;
        int blockColumnOffset = x / 3;

        return blockRowStartingIndex + blockColumnOffset;
    }
}
