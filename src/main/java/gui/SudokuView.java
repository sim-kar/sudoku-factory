package gui;

import javax.swing.JFrame;

public class SudokuView extends JFrame implements BoardChangeObserver, BoardSolvedObserver {
    private final Model model;
    private final Controller controller;

    public SudokuView(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
        this.model.registerObserver((BoardChangeObserver) this);
        this.model.registerObserver((BoardSolvedObserver) this);
    }

    @Override
    public void updateBoard() {

    }

    @Override
    public void solved() {

    }
}
