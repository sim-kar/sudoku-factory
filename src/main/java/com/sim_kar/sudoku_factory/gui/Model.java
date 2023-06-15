package com.sim_kar.sudoku_factory.gui;
import com.sim_kar.sudoku_factory.sudoku.Position;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * Contains the parts and functionality needed to play a number puzzle game. Can create new
 * boards and provides the means to interact with it. Changes to the board should be "pulled"
 * from the model by registering as an observer. A {@link BoardChangeObserver} will be notified
 * whenever the state of the board has changed with {@link BoardChangeObserver#updateBoard()}, which
 * it can use pull the changes by calling the methods in this model. A {@link BoardSolvedObserver}
 * will be notified when the board is solved with {@link BoardSolvedObserver#solved()}.
 */
public interface Model {
    /**
     * Register an observer that will be notified when the board changes.
     *
     * @param observer the observer that will be notified of changes
     */
    void registerObserver(BoardChangeObserver observer);

    /**
     * Remove an observer so that it is no longer notified when the board changes.
     *
     * @param observer the observer to remove from being notified of changes
     */
    void removeObserver(BoardChangeObserver observer);

    /**
     * Register an observer that will be notified when the board is solved.
     *
     * @param observer the observer that will be notified when the board is solved
     */
    void registerObserver(BoardSolvedObserver observer);

    /**
     * Remove an observer so that it is no longer notified when the board is solved.
     *
     * @param observer the observer to remove from being notified when the board is solved
     */
    void removeObserver(BoardSolvedObserver observer);

    /**
     * Create a new number puzzle board that will be used by this model. Clues are the correct
     * numbers that are visible in tiles on the board, in contrast to the empty tiles that a user
     * is supposed to fill in.
     *
     * @param clues the amount of correct tiles to show on the board
     */
    void createPuzzle(int clues);

    /**
     * Create a new number puzzle board that will be used by this model. Clues are the correct
     * numbers that are visible in tiles on the board, in contrast to the empty tiles that a user
     * is supposed to fill in.
     * The latch can be used to wait for the board to be created and set in the model before
     * continuing execution in the calling thread, otherwise the board may not be set when using
     * methods that rely on the board. The latch only needs to be counted down once (1).
     * The preferable way is to use {@link BoardChangeObserver} to wait for the board to be
     * updated. Then there is no need to use a latch.
     *
     * @param clues the amount of correct tiles to show on the board
     * @param latch a latch that can be used to wait for the method to complete. Should be used
     *              with a count of 1.
     */
    void createPuzzle(int clues, CountDownLatch latch);

    /**
     * Set the value of the tile at the given position.
     *
     * @param xy the column x and row y of the tile
     * @param value the value to set
     */
    void setValueAt(Position xy, int value);

    /**
     * Get the value of the tile at the given position. The value is the current value of the tile,
     * which is not necessarily the correct value.
     *
     * @param xy the column x and row y of the tile
     * @return the value of the tile
     */
    int getValueAt(Position xy);

    /**
     * Check whether a tile is editable, i.e. its value can be changed.
     *
     * @param xy the column x and row y of the tile
     * @return whether the tile is editable
     */
    boolean isEditable(Position xy);

    /**
     * Get all tiles in sections with mistakes made by a user. In other words, only tiles with
     * incorrect values set by a user are counted; empty tiles are ignored. Returns the positions
     * of all tiles in sections with user mistakes, not just the positions of incorrect tiles.
     *
     * @return the positions of all tiles in sections containing user mistakes
     */
    Set<Position> getSectionsWithMistakes();

    /**
     * Get all tiles in sections with mistakes. The given parameter decides whether to only count
     * tiles with incorrect values set by a user, or to count empty tiles as well. Returns the
     * positions of all tiles in sections with mistakes, not just the positions of incorrect tiles.
     *
     * @param ignoreEmptyTiles whether to include sections with empty tiles, even if there are no
     *                         user made mistakes in them
     * @return the positions of all tiles in sections containing mistakes
     */
    Set<Position> getSectionsWithMistakes(boolean ignoreEmptyTiles);

    /**
     * Get the position of all tiles with duplicate values. Duplicate values are values in sections
     * that are repeated more than once. Only duplicate values that have been set by a user will
     * be returned; clues (correct values on the board) and empty tiles will be ignored.
     *
     * @return the positions of duplicate values set by a user
     */
    Set<Position> getDuplicates();

    /**
     * Get the position of all tiles with duplicate values. Duplicate values are values in sections
     * that are repeated more than once, excluding empty tiles. The given parameter decides whether
     * all duplicate values (including clues) are returned, or whether only duplicates set by a
     * user (excluding clues) will be returned.
     *
     * @param editableOnly whether to include clues, or to only include values set by a user
     * @return the positions of duplicate values
     */
    Set<Position> getDuplicates(boolean editableOnly);
}
