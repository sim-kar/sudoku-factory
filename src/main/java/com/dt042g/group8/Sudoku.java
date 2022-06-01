package com.dt042g.group8;

import com.dt042g.group8.gui.Controller;
import com.dt042g.group8.gui.Model;
import com.dt042g.group8.gui.SudokuController;
import com.dt042g.group8.gui.SudokuModel;
import com.dt042g.group8.gui.SudokuView;
import com.dt042g.group8.sudoku.Factory;
import com.dt042g.group8.sudoku.SudokuFactory;
import com.dt042g.group8.sudoku.Solver;
import com.dt042g.group8.sudoku.SudokuSolver;
import java.awt.EventQueue;
import java.util.Random;

/**
 * A com.dt042g.group8.Sudoku puzzle game application. Lets you generate new interactive com.dt042g.group8.Sudoku puzzles of varying
 * difficulty, which are displayed along with controls.
 */
public class Sudoku {
    /**
     * Run the game in a new window.
     *
     * @param args not used
     */
    public static void main(String... args) {
        EventQueue.invokeLater(() -> {
            Random random = new Random();
            Solver solver = new SudokuSolver(random);
            Factory factory = new SudokuFactory(solver);
            Model model = new SudokuModel(factory);
            Controller controller = new SudokuController(model);
            SudokuView view = new SudokuView(model, controller);

            view.createView();
        });
    }
}
