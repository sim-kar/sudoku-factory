package gui;

import sudoku.Position;

public class SudokuController implements Controller {
    private final Model model;

    public SudokuController(Model model) {
        this.model = model;
    }

    @Override
    public void createPuzzle(Difficulty difficulty) {

    }

    @Override
    public void setValueAt(Position xy, int value) {
        model.setValueAt(xy, value);
    }
}
