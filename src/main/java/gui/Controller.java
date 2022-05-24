package gui;

import sudoku.Position;

public interface Controller {
    void createPuzzle();
    void setValueAt(Position xy, int value);
}
