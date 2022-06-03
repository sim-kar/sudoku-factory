package com.dt042g.group8.sudoku;

/**
 * Used to create new number puzzle boards.
 */
public interface Factory {

    /**
     * Create a new number puzzle board. Clues are the correct numbers that are visible in tiles
     * on the board, in contrast to the empty tiles that a user is supposed to fill in.
     *
     * @param clues the amount of correct tiles to show on the board
     * @return a number puzzle board with the amount of supplied clues
     */
    Board create(int clues);
}
