package gui;

import sudoku.Position;

public class SudokuController implements Controller {
    private final static int VERY_EASY_CLUES = 33;
    private final static int EASY_CLUES = 31;
    private final static int MEDIUM_CLUES = 29;
    private final static int HARD_CLUES = 27;
    private final static int VERY_HARD_CLUES = 25;
    private final Model model;

    public SudokuController(Model model) {
        this.model = model;
    }

    @Override
    public void createPuzzle(Difficulty difficulty) {
        switch (difficulty) {
            case VERY_EASY -> model.createPuzzle(VERY_EASY_CLUES);
            case EASY -> model.createPuzzle(EASY_CLUES);
            case MEDIUM -> model.createPuzzle(MEDIUM_CLUES);
            case HARD -> model.createPuzzle(HARD_CLUES);
            case VERY_HARD -> model.createPuzzle(VERY_HARD_CLUES);
        }
    }

    @Override
    public void setValueAt(Position xy, int value) {
        model.setValueAt(xy, value);
    }
}
