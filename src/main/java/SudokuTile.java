public class SudokuTile implements Tile{
    boolean editable = true;
    int curVal = 0;
    int corVal = 0;

    @Override
    public int getCorrectVal() {
        return 0;
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
    public int getCurrentVal() {
        return curVal;
    }

    @Override
    public void SetCurrentVal(int value) {
        this.curVal = value;
    }

    @Override
    public void clear() {
        this.curVal = 0;
    }

    @Override
    public boolean check() {
        return curVal == corVal;
    }
}
