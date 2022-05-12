import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PositionTest {
    @Test
    @DisplayName("Creating Position with x = 1, getX returns 1")
    public void createdPositionReturnsCorrectX() {
        Position position = new Position(1, 2);
        assertEquals(1, position.getX());
    }

    @Test
    @DisplayName("Creating Position with y = 2, getY returns 2")
    public void createdPositionReturnsCorrectY() {
        Position position = new Position(1, 2);
        assertEquals(2, position.getY());
    }

    @Test
    @DisplayName("Using equals with the same Position object returns true")
    public void usingEqualsWithSameObjectReturnsTrue() {
        Position position = new Position(1, 2);
        Position position2 = position;
        assertTrue(position.equals(position2));
    }

    @Test
    @DisplayName("Using equals with Positions with the same x and y returns true")
    public void usingEqualsWithSameXAndYReturnsTrue() {
        Position position = new Position(1, 2);
        Position position2 = new Position(1, 2);
        assertTrue(position.equals(position2));
    }

    @Test
    @DisplayName("Using equals with Positions with different x returns false")
    public void usingEqualsWithDifferentXReturnsFalse() {
        Position position = new Position(1, 2);
        Position position2 = new Position(2, 2);
        assertFalse(position.equals(position2));
    }

    @Test
    @DisplayName("Using equals with Positions with different y returns false")
    public void usingEqualsWithDifferentYReturnsFalse() {
        Position position = new Position(1, 2);
        Position position2 = new Position(1, 1);
        assertFalse(position.equals(position2));
    }

    @Test
    @DisplayName("Using equals with a null returns false")
    public void usingEqualsWithNullReturnsFalse() {
        Position position = new Position(1, 2);
        Position position2 = null;
        assertFalse(position.equals(position2));
    }

    @Test
    @DisplayName("Using equals with a different class returns false")
    public void usingEqualsWithDifferentClassReturnsFalse() {
        Position position = new Position(1, 2);
        Integer integer = 1;
        assertFalse(position.equals(integer));
    }

    @Test
    @DisplayName("Two Positions with the same x and y have the same hash code")
    public void positionsWithSameXAndYHaveSameHashCode() {
        Position position = new Position(1, 2);
        Position position2 = new Position(1, 2);
        assertEquals(position.hashCode(), position2.hashCode());
    }

    @Test
    @DisplayName("Two Positions with the different x have different hash codes")
    public void positionsWithDifferentXReturnNameHashCode() {
        Position position = new Position(1, 2);
        Position position2 = new Position(2, 2);
        assertNotEquals(position.hashCode(), position2.hashCode());
    }

    @Test
    @DisplayName("Two Positions with the different y have different hash codes")
    public void positionsWithDifferentYReturnNameHashCode() {
        Position position = new Position(1, 2);
        Position position2 = new Position(1, 1);
        assertNotEquals(position.hashCode(), position2.hashCode());
    }
}