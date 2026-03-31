package org.algandsd;


import java.util.Objects;

public class Point {
    private int x;
    private int y;
    private Point parent = null;

    public Point (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX () {
        return x;
    }

    public int getY () {
        return y;
    }

    public void setParent (Point parent) {
        this.parent = parent;
    }

    public Point getParent () {
        return parent;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point other = (Point) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}