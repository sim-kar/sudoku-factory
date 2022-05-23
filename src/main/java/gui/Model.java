package gui;
import sudoku.Position;
import java.util.List;

public interface Model {
    void registerObserver(BoardChangeObserver observer);
    void removeObserver(BoardChangeObserver observer);
    void registerObserver(BoardSolvedObserver observer);
    void removeObserver(BoardSolvedObserver observer);
    void createPuzzle(int clues);
    void setValueAt(Position xy, int value);
    int getValueAt(Position xy);
    boolean isEditable(Position xy);
    List<Position> getSectionsWithMistakes();
    List<Position> getSectionsWithMistakes(boolean ignoreEmptyTiles);
    List<Position> getDuplicates();
    List<Position> getDuplicates(boolean editableOnly);
}
