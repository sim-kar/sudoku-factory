package com.sim_kar.sudoku_factory.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Used to create valid Sudoku puzzle boards.
 */
public class SudokuFactory implements Factory {
    // 6,670,903,752,021,072,936,960 possible sudoku boards and so far around 49,000 puzzles with
    // the lowest possible amount of clues (17) have been found. We need to set our lower limit
    // slightly higher than this...
    private static final int MIN_CLUES = 25;
    private static final int MAX_CLUES = 81;
    private static final int EMPTY = 0;
    private final Solver solver;

    /**
     * Create a new Sudoku factory.
     *
     * @param solver a solver used to generate new boards
     */
    public SudokuFactory(Solver solver) {
        this.solver = solver;
    }

    /**
     * Create a valid Sudoku puzzle board. A Sudoku board is 9x9 tiles, where each row, column
     * and nine 3x3 blocks can only contain the numbers 1 to 9 without any duplicates. There are
     * also empty tiles to be filled in. For a board to be considered valid there can only be a
     * single solution.
     *
     * A board consists of empty tiles to be filled in, and clues which are tiles containing the
     * correct number. A Sudoku board can have at most 81 clues, since there are 81 tiles. The
     * minimum amount of clues required by this method is 25.
     *
     * @param clues the number of correct tiles to show on the board
     * @return a Sudoku puzzle board with the amount of supplied clues
     */
    @Override
    public Board create(int clues) {
        if (clues < MIN_CLUES) {
            throw new IllegalArgumentException("There must be at least 25 clues");
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

            // shuffle the order of positions, or tiles will be removed
            // in the same position and order every time
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

    /**
     * Get a list of positions for all x and y positions on a 9x9 board, ordered in ascending order.
     */
    private List<Position> getPositions() {
        List<Position> positions = new ArrayList<>();

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                positions.add(new Position(x, y));
            }
        }

        return positions;
    }

    /**
     * Get a copy of the given 2D integer array.
     */
    private int[][] copy2DArray(int[][] board) {
        return Arrays.stream(board)
                .map(int[]::clone)
                .toArray(int[][]::new);
    }

    /**
     * Get a Sudoku puzzle from a 9x9 2D array containing all the correct values on the board,
     * and a 9x9 2D array containing the clues and empty tiles. The created board has the correct
     * value for each tile, and the empty tiles are editable and have their current value cleared.
     *
     * @param solution a 9x9 2D array with all correct values
     * @param puzzle a 9x9 2D array with all clues and empty tiles
     * @return a 9x9 sudoku board with the tiles initialized according to the given parameters
     */
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

        for (Position position : getPositions()) {
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

    /**
     * Get the index of a block (0-8) in linear order.
     */
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
