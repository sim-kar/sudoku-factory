package gui;

import sudoku.Factory;
import sudoku.Position;
import java.util.Set;

public class SudokuModel implements Model {
    public SudokuModel(Factory factory) {

    }

    @Override
    public void registerObserver(BoardChangeObserver observer) {

    }

    @Override
    public void removeObserver(BoardChangeObserver observer) {

    }

    @Override
    public void registerObserver(BoardSolvedObserver observer) {

    }

    @Override
    public void removeObserver(BoardSolvedObserver observer) {

    }

    @Override
    public void createPuzzle(int clues) {

    }

    @Override
    public void setValueAt(Position xy, int value) {

    }

    @Override
    public int getValueAt(Position xy) {
        return 0;
    }

    @Override
    public boolean isEditable(Position xy) {
        return false;
    }

    @Override
    public Set<Position> getSectionsWithMistakes() {
        return null;
    }

    @Override
    public Set<Position> getSectionsWithMistakes(boolean ignoreEmptyTiles) {
        return null;
    }

    @Override
    public Set<Position> getDuplicates() {
        return null;
    }

    @Override
    public Set<Position> getDuplicates(boolean editableOnly) {
        return null;
    }
}
