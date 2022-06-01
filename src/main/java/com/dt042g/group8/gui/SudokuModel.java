package com.dt042g.group8.gui;

import org.jetbrains.annotations.Nullable;
import com.dt042g.group8.sudoku.Board;
import com.dt042g.group8.sudoku.Factory;
import com.dt042g.group8.sudoku.Position;
import com.dt042g.group8.sudoku.Section;
import com.dt042g.group8.sudoku.Tile;
import javax.swing.SwingWorker;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * For playing com.dt042g.group8.Sudoku.
 * {@inheritDoc}
 */
public class SudokuModel implements Model {
    private final static int EMPTY = 0;
    private final Factory factory;
    private final List<BoardChangeObserver> changeObservers;
    private final List<BoardSolvedObserver> solvedObservers;
    @Nullable private Board board;

    /**
     * Create a new com.dt042g.group8.Sudoku Model that uses the given factory to create com.dt042g.group8.Sudoku boards.
     *
     * @param factory the factory to use to create com.dt042g.group8.Sudoku boards.
     */
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

    /**
     * Notifies all registered board change observers in no particular order.
     */
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

    /**
     * Notifies all registered board solved observers in no particular order.
     */
    private void notifySolvedObservers() {
        for (BoardSolvedObserver observer : solvedObservers) {
            observer.solved();
        }
    }

    @Override
    public void createPuzzle(int clues) {
        createPuzzle(clues, null);
    }

    @Override
    public void createPuzzle(int clues, @Nullable CountDownLatch latch) {
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
    public void setValueAt(Position xy, int value) throws IllegalStateException {
        if (board == null) throw new IllegalStateException("No puzzle has been created");

        Tile tile = board.getTile(xy);
        if (tile.isEditable()) {
            board.getTile(xy).setCurrentValue(value);
            notifyChangeObservers();

            if (board.isCorrect()) notifySolvedObservers();
        }
    }

    @Override
    public int getValueAt(Position xy) throws IllegalStateException {
        if (board == null) throw new IllegalStateException("No puzzle has been created");

        return board.getTile(xy).getCurrentValue();
    }

    @Override
    public boolean isEditable(Position xy) throws IllegalStateException {
        if (board == null) throw new IllegalStateException("No puzzle has been created");

        return board.getTile(xy).isEditable();
    }

    @Override
    public Set<Position> getSectionsWithMistakes() {
        return getSectionsWithMistakes(true);
    }

    @Override
    public Set<Position> getSectionsWithMistakes(boolean ignoreEmptyTiles)
            throws IllegalStateException {
        if (board == null) throw new IllegalStateException("No puzzle has been created");

        Set<Position> sectionsWithMistakes = new HashSet<>();

        for (Section section : board.getIncorrectSections()) {
            Set<Position> positions = new HashSet<>();
            boolean hasUserMistake = false;

            for (Tile tile : section.getTiles()) {
                positions.add(tile.getPosition());

                if (tile.isEditable() && (tile.getCurrentValue() != EMPTY)) {
                    hasUserMistake = true;
                }
            }

            if (!ignoreEmptyTiles || hasUserMistake) sectionsWithMistakes.addAll(positions);
        }

        return sectionsWithMistakes;
    }

    @Override
    public Set<Position> getDuplicates() {
        return getDuplicates(true);
    }

    @Override
    public Set<Position> getDuplicates(boolean editableOnly) throws IllegalStateException {
            if (board == null) throw new IllegalStateException("No puzzle has been created");

            Set<Position> duplicates = new HashSet<>();

            for (Section section : board.getIncorrectSections()) {
                Map<Integer, List<Tile>> tilesGroupedByValue = section.getTiles()
                        .stream()
                        .collect(Collectors.groupingBy(Tile::getCurrentValue));

                duplicates.addAll(
                        tilesGroupedByValue.values()
                                .stream()
                                .filter(list -> list.size() > 1)
                                .flatMap(List::stream)
                                .filter(tile -> tile.getCurrentValue() != EMPTY)
                                .filter(tile -> {
                                    if (editableOnly) return tile.isEditable();
                                    return true;
                                })
                                .map(Tile::getPosition)
                                .collect(Collectors.toSet())
                );
            }

            return duplicates;
    }
}
