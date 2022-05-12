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
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;
        if (getClass() != otherObject.getClass()) return false;

        Position other = (Position) otherObject;
        return (this.x == other.x) && (this.y == other.y);
    }
}