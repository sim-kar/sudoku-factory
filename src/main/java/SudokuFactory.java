public class SudokuFactory implements Factory {
    private final Solver solver;

    public SudokuFactory(Solver solver) {
        this.solver = solver;
    }

    @Override
    public Board create(int clues) {
        return null;
    }
}
