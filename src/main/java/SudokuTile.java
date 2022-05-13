public class SudokuTile implements Tile{
    boolean editable = true;
    int curVal = 0;
    int corVal = 0;

    public SudokuTile(int corVal) {
        this.corVal = corVal;
    }

    @Override
    public int getCorrectVal() {
        return corVal;
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
    public void setCurrentVal(int value) {
        if (this.editable) this.curVal = value;
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
