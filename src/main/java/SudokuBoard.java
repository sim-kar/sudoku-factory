import java.util.List;

public class SudokuBoard implements Board{
    @Override
    public Section getRow(int y) {
        return null;
    }

    @Override
    public Section getRow(Position xy) {
        return null;
    }

    @Override
    public Section getColumn(int x) {
        return null;
    }

    @Override
    public Section getColumn(Position xy) {
        return null;
    }

    @Override
    public Section getBlock(Position xy) {
        return null;
    }

    @Override
    public Tile getTile(Position xy) {
        return null;
    }

    @Override
    public void setTile(Position xy, int value) {

    }

    @Override
    public boolean isCorrect() {
        return false;
    }

    @Override
    public List<Section> getIncorrectSections() {
        return null;
    }

    @Override
    public void clear() {

    }
}
