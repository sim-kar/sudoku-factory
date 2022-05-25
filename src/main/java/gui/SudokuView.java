package gui;

import javax.swing.JFrame;

public class SudokuView extends JFrame implements BoardChangeObserver, BoardSolvedObserver {
    private final Model model;
    private final Controller controller;

    public SudokuView(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
    }

    @Override
    public void updateBoard() {

    }

    @Override
    public void solved() {

    }
}
