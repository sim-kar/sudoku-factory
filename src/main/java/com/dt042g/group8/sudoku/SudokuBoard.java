package com.dt042g.group8.sudoku;

import java.util.*;

public class SudokuBoard implements Board{
    Map<Position, Section> rows = new HashMap<>();
    Map<Position, Section> columns = new HashMap<>();
    Map<Position, Section> blocks = new HashMap<>();

    /**
     * The SudokuBoard contains 9x9 Tiles and 27 Sections (one for each row, column, and 9 3x3 blocks
     *
     * @param rows The rows as an array of Sections
     * @param columns The columns as an array of Sections
     * @param blocks The blocks as an array of Sections
     */
    public SudokuBoard(Section[] rows, Section[] columns, Section[] blocks) {

        // Populate the rows Map
        for (int i = 0; i < rows.length; i++) {
            Set<Tile> tiles = rows[i].getTiles();
            int finalI = i;
            tiles.forEach(tile -> {
                Position position = tile.getPosition();
                this.rows.put(position, rows[finalI]);
            });
        }

        // Populate the columns Map
        for (int i = 0; i < columns.length; i++) {
            Set<Tile> tiles = columns[i].getTiles();
            int finalI = i;
            tiles.forEach(tile -> {
                Position position = tile.getPosition();
                this.columns.put(position, columns[finalI]);
            });
        }

        // Populate the blocks Map
        for (int i = 0; i < blocks.length; i++) {
            Set<Tile> tiles = blocks[i].getTiles();
            int finalI = i;
            tiles.forEach(tile -> {
                Position position = tile.getPosition();
                this.blocks.put(position, blocks[finalI]);
            });
        }

    }

    @Override
    public Section getRow(int y) {
        return this.rows.get(new Position(0, y));
    }

    @Override
    public Section getRow(Position xy) {
        return this.rows.get(xy);
    }

    @Override
    public Section getColumn(int x) {
        return this.columns.get(new Position(x, 0));
    }

    @Override
    public Section getColumn(Position xy) {
        return this.columns.get(xy);
    }

    @Override
    public Section getBlock(Position xy) {
        return this.blocks.get(xy);
    }


    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the given Position isn't on the board
     */
    @Override
    public Tile getTile(Position xy) throws IllegalArgumentException {
        if (!rows.containsKey(xy)) {
            throw new IllegalArgumentException("There is no tile at the given Position");
        }

        Section section = rows.get(xy);
        return section.getTile(xy);
    }

    @Override
    public void setTile(Position xy, int value) {
        // will throw null pointer exception if xy is not in map otherwise
        if (!rows.containsKey(xy)) return;

        Section section = rows.get(xy);
        Tile tile = section.getTile(xy);
        tile.setCurrentValue(value);
    }

    @Override
    public boolean isCorrect() {

        for (Section section : rows.values()) {
            for (Tile tile : section.getTiles()) {
                if (!tile.check()) return false;
            }
        }

        return true;
    }

    @Override
    public List<Section> getIncorrectSections() {
        List<Section> incorrectSections = new LinkedList<>();

        for (Section section : rows.values()) {
            if (!section.isCorrect())
                if (!incorrectSections.contains(section))
                    incorrectSections.add(section);
        }

        for (Section section : columns.values()) {
            if (!section.isCorrect())
                if (!incorrectSections.contains(section))
                    incorrectSections.add(section);
        }

        for (Section section : blocks.values()) {
            if (!section.isCorrect())
                if (!incorrectSections.contains(section))
                    incorrectSections.add(section);
        }

        return incorrectSections;
    }

    @Override
    public void clear() {
        rows.forEach((position, section) -> {
            section.getTile(position).clear();
        });
    }
}
