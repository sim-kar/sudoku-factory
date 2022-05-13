public class SudokuTile implements Tile {
    boolean editable = true;
    int currentValue;
    int correctValue;

    public SudokuTile(int value) {
        this.correctValue = value;
        this.currentValue = value;
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
        this.curValue = 0;
    }

    @Override
    public boolean check() {
        return currentValue == correctValue;
    }
}
