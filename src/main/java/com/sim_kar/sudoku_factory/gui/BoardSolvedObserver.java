package com.sim_kar.sudoku_factory.gui;

/**
 * Observes when a puzzle game board is solved.
 */
public interface BoardSolvedObserver {
    /**
     * Can be used by the observed component to notify the observer that the board has been  solved.
     */
    void solved();
}
