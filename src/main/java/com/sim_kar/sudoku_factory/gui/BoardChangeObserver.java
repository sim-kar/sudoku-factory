package com.sim_kar.sudoku_factory.gui;

/**
 * Observes changes to a puzzle game board.
 */
public interface BoardChangeObserver {
    /**
     * Can be used by the observed component to notify the observer that the board has been updated.
     */
    void updateBoard();
}
