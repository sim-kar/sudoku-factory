package gui;

import sudoku.Position;

/**
 * A difficulty level, with 5 steps ranging from very easy to very hard.
 */
enum Difficulty {
    VERY_EASY, EASY, MEDIUM, HARD, VERY_HARD
}

public interface Controller {
    void createPuzzle(Difficulty difficulty);
    void setValueAt(Position xy, int value);
}
