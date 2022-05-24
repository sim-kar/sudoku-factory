package gui;

import sudoku.Position;

/**
 * Can be used to update the state of a {@link Model}. Provides the strategy of how to update the
 * model so that the caller doesn't have to concern itself with the details. Should be used with
 * a {@link SudokuModel}.
 */
public class SudokuController implements Controller {
    private final static int VERY_EASY_CLUES = 33;
    private final static int EASY_CLUES = 31;
    private final static int MEDIUM_CLUES = 29;
    private final static int HARD_CLUES = 27;
    private final static int VERY_HARD_CLUES = 25;
    private final Model model;

    /**
     * Create a new SudokuController for the given model.
     *
     * @param model the model that this controller will update
     */
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
