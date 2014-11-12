package net.zomis;

public class IntPoint {
    protected int x;
    protected int y;

    public IntPoint() {
        this(0, 0);
    }
    public IntPoint(IntPoint copy) {
        this(copy.x, copy.y);
    }
    public IntPoint(int coordx, int coordy) {
        this.set(coordx, coordy);
    }

    public void set(int coordx, int coordy) {
        x = coordx;
        y = coordy;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public IntPoint dxdy(int dx, int dy) {
        return new IntPoint(x + dx, y + dy);
    }
    public IntPoint dxdy(IntPoint delta) {
        return new IntPoint(x + delta.x, y + delta.y);
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
    public void set(IntPoint value) {
        this.set(value.x, value.y);
    }
    public int distanceSquared(IntPoint other) {
        int dx = other.x - this.x;
        int dy = other.y - this.y;
        return dx*dx + dy*dy;
    }
    public double distance(IntPoint other) {
        return Math.sqrt(distanceSquared(other));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof IntPoint))
            return false;
        IntPoint other = (IntPoint) obj;
        return x == other.getX() && y == other.getY();
    }


}