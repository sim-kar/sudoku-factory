package gui;

import org.jetbrains.annotations.Nullable;
import sudoku.Board;
import sudoku.Factory;
import sudoku.Position;
import javax.swing.SwingWorker;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class SudokuModel implements Model {
    private final Factory factory;
    private final List<BoardChangeObserver> changeObservers;
    private final List<BoardSolvedObserver> solvedObservers;
    @Nullable private Board board;

    public SudokuModel(Factory factory) {
        this.factory = factory;
        this.changeObservers = new ArrayList<>();
        this.solvedObservers = new ArrayList<>();
    }

    @Override
    public void registerObserver(BoardChangeObserver observer) {
        this.changeObservers.add(observer);
    }

    @Override
    public void removeObserver(BoardChangeObserver observer) {
        this.changeObservers.remove(observer);
    }

    private void notifyChangeObservers() {
        for (BoardChangeObserver observer : changeObservers) {
            observer.updateBoard();
        }
    }

    @Override
    public void registerObserver(BoardSolvedObserver observer) {
        this.solvedObservers.add(observer);
    }

    @Override
    public void removeObserver(BoardSolvedObserver observer) {
        this.solvedObservers.remove(observer);
    }

    @Override
    public void createPuzzle(int clues) {
        createPuzzleInBackground(clues, null);
    }

    @Override
    public void createPuzzle(int clues, CountDownLatch latch) {
        createPuzzleInBackground(clues, latch);
    }

    private void createPuzzleInBackground(int clues, @Nullable CountDownLatch latch) {
        SwingWorker<Board, Void> worker = new SwingWorker<>() {
            @Override
            protected Board doInBackground() {
                return factory.create(clues);
            }

            @Override
            public void done() {
                try {
                    board = get();
                    notifyChangeObservers();
                    if (latch != null) latch.countDown();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    @Override
    public void setValueAt(Position xy, int value) {

    }

    @Override
    public int getValueAt(Position xy) {
        return -1;
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
