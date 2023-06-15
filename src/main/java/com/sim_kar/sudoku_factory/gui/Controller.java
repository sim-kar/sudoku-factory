package com.sim_kar.sudoku_factory.gui;

import com.sim_kar.sudoku_factory.sudoku.Position;

/**
 * A difficulty level, with 5 steps ranging from very easy to very hard.
 */
enum Difficulty {
    VERY_EASY, EASY, MEDIUM, HARD, VERY_HARD
}

/**
 * Can be used to update the state of a {@link Model}.
 */
public interface Controller {
    /**
     * Create a new puzzle in the {@link Model}. The given {@link Difficulty} will determine how
     * many clues (correct values) are on the puzzle board. Higher difficulty means fewer clues.
     *
     * @param difficulty how difficult to make the puzzle, determined by the number of clues on the
     *                   board
     */
    void createPuzzle(Difficulty difficulty);

    /**
     * Set the value of the tile at position x, y on the puzzle board in the {@link Model}.
     *
     * @param xy the position of the tile to set the value of
     * @param value the value to set
     */
    void setValueAt(Position xy, int value);
}
