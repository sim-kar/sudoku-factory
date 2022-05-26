import gui.Controller;
import gui.Model;
import gui.SudokuController;
import gui.SudokuModel;
import gui.SudokuView;
import sudoku.Factory;
import sudoku.SudokuFactory;
import sudoku.Solver;
import sudoku.SudokuSolver;
import java.awt.EventQueue;
import java.util.Random;

/**
 * A Sudoku puzzle game application. Lets you generate new interactive Sudoku puzzles of varying
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
