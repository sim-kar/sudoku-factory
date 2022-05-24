package gui;

import sudoku.Position;

enum Difficulty {
    VERY_EASY, EASY, MEDIUM, HARD, VERY_HARD
}

public interface Controller {
    void createPuzzle(Difficulty difficulty);
    void setValueAt(Position xy, int value);
}
