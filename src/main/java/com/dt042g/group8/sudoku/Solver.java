package com.dt042g.group8.sudoku;

/**
 * Used to generate solutions to number puzzle boards, such as com.dt042g.group8.Sudoku. Can also be used to check if
 * a board has a single, unique solution, which is a condition for a com.dt042g.group8.Sudoku puzzle to be valid.
 */
public interface Solver {
    /**
     * Generates a solution for a given board. In other words, it fills every empty tile on
     * the board with a valid number, with the additional constraint that each number in
     * section on the board must be unique.
     *
     * @param board the board to solve
     * @return the solved board, i.e. a board with a correct number in every tile
     */
    int[][] generate(int[][] board);

    /**
     * Checks whether a board only has a single solution.
     *
     * @param board the board to check if it only has one solution
     * @return whether the given board only has one solution
     */
    boolean isUnique(int[][] board);
}
