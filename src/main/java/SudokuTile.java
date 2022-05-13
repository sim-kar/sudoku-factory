public class SudokuTile implements Tile{
    boolean editable = true;
    int curValue = 0;
    int corValue = 0;

    public SudokuTile(int corValue) {
        this.corValue = corValue;
    }

    @Override
    public int getCorrectValue() {
        return corValue;
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
        return curValue;
    }

    @Override
    public void setCurrentValue(int value) {
        if (this.editable) this.curValue = value;
    }

    @Override
    public void clear() {
        this.curValue = 0;
    }

    @Override
    public boolean check() {
        return curValue == corValue;
    }
}
