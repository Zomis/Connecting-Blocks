package net.zomis;

public enum Direction4 {
    LEFT(-1, 0), RIGHT(1, 0), UP(0, -1), DOWN(0, 1);

    private int	dx;
    private int	dy;

    private Direction4(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDeltaX() {
        return dx;
    }
    public int getDeltaY() {
        return dy;
    }

    /**
     * Up is down, down is up. Logic went on ski vacation with it's buddy reason.
     * @return
     */
    public Direction4 getOpposite() {
        return direction(0, 0, -dx, -dy);
    }
    /**
     * Left --> Up --> Right --> Down --> Left
     * @return A direction that has been rotated 90 degrees clockwise.
     */
    public Direction4 getRotation90() {
        return direction(0, 0, -dy, dx);
    }

    public int getDegreeRotation() {
        if (this == LEFT)
            return 180;
        else if (this == UP)
            return 90;
        else if (this == RIGHT)
            return 0;
        else if (this == DOWN)
            return 270;
        throw new UnsupportedOperationException("Since when did 4 directions become more than 4 ?");
    }

    public static Direction4 direction(int oldX, int oldY, int newX, int newY) {
        if (oldX > newX) return LEFT;
        else if (oldX < newX) return RIGHT;
        else if (oldY > newY) return UP;
        else if (oldY < newY) return DOWN;
        else return null;
    }

    public void addToIntPoint(IntPoint ip) {
        ip.set(ip.getX() + this.dx, ip.getY() + this.dy);
    }

    public char toChar() {
        return this.toString().charAt(0);
    }
}
