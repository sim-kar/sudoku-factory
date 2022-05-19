import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SudokuFactory implements Factory {
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

        int[][] empty = new int[9][9];

        for (int[] row : empty) {
            Arrays.fill(row, 0);
        }

        List<Position> positions = new ArrayList<>();

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                positions.add(new Position(x, y));
            }
        }

        Collections.shuffle(positions);

        int[][] solution = new int[][]{};
        int[][] puzzle = new int[][]{};
        int currentClues = MAX_CLUES;

        if (clues == MAX_CLUES) {
            solution = solver.generate(empty);
            puzzle = Arrays.stream(solution)
                    .map(int[]::clone)
                    .toArray(int[][]::new);
        }

        // it's possible to generate a random solution that doesn't have a unique puzzle
        // with the given amount of clues. If that is the case we try again
        while (currentClues > clues) {
            solution = solver.generate(empty);
            puzzle = Arrays.stream(solution)
                    .map(int[]::clone)
                    .toArray(int[][]::new);

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
        }

        List<Set<Tile>> tilesInRows = Stream.generate(HashSet<Tile>::new)
                .limit(9)
                .collect(Collectors.toCollection(ArrayList::new));
        List<Set<Tile>> tilesInColumns = Stream.generate(HashSet<Tile>::new)
                .limit(9)
                .collect(Collectors.toCollection(ArrayList::new));
        List<Set<Tile>> tilesInBlocks = Stream.generate(HashSet<Tile>::new)
                .limit(9)
                .collect(Collectors.toCollection(ArrayList::new));

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                int value = solution[y][x];
                Tile tile = new SudokuTile(value, new Position(x, y));
                if (puzzle[y][x] == EMPTY) {
                    tile.setEditable(true);
                    tile.clear();
                }

                tilesInRows.get(y).add(tile);
                tilesInColumns.get(x).add(tile);

                int blockRowStartingIndex = (y / 3) * 3;
                int blockColumnOffset = x / 3;
                int blockIndex = blockRowStartingIndex + blockColumnOffset;

                tilesInBlocks.get(blockIndex).add(tile);
            }
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
}
