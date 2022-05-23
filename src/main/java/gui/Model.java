package gui;
import sudoku.Position;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public interface Model {
    void registerObserver(BoardChangeObserver observer);
    void removeObserver(BoardChangeObserver observer);
    void registerObserver(BoardSolvedObserver observer);
    void removeObserver(BoardSolvedObserver observer);
    void createPuzzle(int clues);
    void createPuzzle(int clues, CountDownLatch latch);
    void setValueAt(Position xy, int value);
    int getValueAt(Position xy);
    boolean isEditable(Position xy);
    Set<Position> getSectionsWithMistakes();
    Set<Position> getSectionsWithMistakes(boolean ignoreEmptyTiles);
    Set<Position> getDuplicates();
    Set<Position> getDuplicates(boolean editableOnly);
}
