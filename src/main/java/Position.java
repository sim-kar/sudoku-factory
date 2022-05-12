import java.util.Objects;

public class Position {
    private final int x;
    private final int y;
    private final int hashCode;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.hashCode = Objects.hash(x, y);
    }

    public int getX() {
        return -1;
    }

    public int getY() {
        return -1;
    }

    @Override
    public int hashCode() {
        return -1;
    }

    @Override
    public boolean equals(Object otherObject) {
        return false;
    }
}