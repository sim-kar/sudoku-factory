package com.sim_kar.sudoku_factory.sudoku;

import org.jetbrains.annotations.Nullable;
import java.util.Objects;

/**
 * An x, y position, such as a position on a game board. X is the column, y is the row.
 */
public class Position {
    private final int x;
    private final int y;
    private final int hashCode;

    /**
     * Create a new position at the column x and row y.
     *
     * @param x the column.
     * @param y the row.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.hashCode = Objects.hash(x, y);
    }

    /**
     * Get the x value (column) of this position.
     *
     * @return the position's x value (column).
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get the y value (row) of this position.
     *
     * @return the position's y value (row).
     */
    public int getY() {
        return this.y;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(@Nullable Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;
        if (getClass() != otherObject.getClass()) return false;

        Position other = (Position) otherObject;
        return (this.x == other.x) && (this.y == other.y);
    }
}