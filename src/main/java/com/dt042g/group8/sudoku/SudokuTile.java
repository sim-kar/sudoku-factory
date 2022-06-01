package com.dt042g.group8.sudoku;

public class SudokuTile implements Tile {
    Position position;
    boolean editable = false;
    int currentValue;
    int correctValue;

    /**
     * Creates a com.dt042g.group8.sudoku.SudokuTile with a certain value at a certain position
     *
     * @param value the value of the com.dt042g.group8.sudoku.SudokuTile
     * @param position the position of the com.dt042g.group8.sudoku.SudokuTile
     */
    public SudokuTile(int value, Position position) {
        this.correctValue = value;
        this.currentValue = value;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public int getCorrectValue() {
        return correctValue;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public int getCurrentValue() {
        return currentValue;
    }

    @Override
    public void setCurrentValue(int value) {
        if (isEditable()) this.currentValue = value;
    }

    @Override
    public void clear() {
        if (isEditable()) this.currentValue = 0;
    }

    @Override
    public boolean check() {
        return currentValue == correctValue;
    }
}
