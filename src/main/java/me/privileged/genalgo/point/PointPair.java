package me.privileged.genalgo.point;

/*
 * Simple pair type to hold a Point and a distance
 */

public final class PointPair implements Cloneable {
    private final Point point;
    private final int distance;

    public PointPair(final Point point, final int distance) {
        this.point = point;
        this.distance = distance;
    }

    public final Point getPoint() {
        return this.point;
    }

    public final int getDistance() {
        return this.distance;
    }

    public PointPair clone() throws CloneNotSupportedException
    {
        return (PointPair) super.clone();
    }
}
