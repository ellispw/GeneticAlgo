package me.privileged.genalgo.point;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * Class to represent a point (aka City)
 */

public final class Point implements Cloneable {
    // this is very ugly
    private static final int[] DISTANCE_DATA = {
            0, 94, 76, 141, 91, 60, 120, 145, 91, 74, 90, 55, 145, 108, 41, 49, 33, 151, 69, 111, 24,
            94, 0, 156, 231, 64, 93, 108, 68, 37, 150, 130, 57, 233, 26, 62, 140, 61, 229, 120, 57, 109,
            76, 156, 0, 80, 167, 133, 124, 216, 137, 114, 154, 100, 141, 161, 116, 37, 100, 169, 49, 185, 84,
            141, 231, 80, 0, 229, 185, 201, 286, 216, 139, 192, 178, 113, 239, 182, 92, 171, 155, 128, 251, 137,
            91, 64, 167, 229, 0, 49, 163, 65, 96, 114, 76, 93, 200, 91, 51, 139, 72, 185, 148, 26, 92,
            60, 93, 133, 185, 49, 0, 165, 115, 112, 65, 39, 91, 151, 117, 39, 99, 61, 139, 128, 75, 49,
            120, 108, 124, 201, 163, 165, 0, 173, 71, 194, 203, 74, 254, 90, 127, 136, 104, 269, 75, 163, 144,
            145, 68, 216, 286, 65, 115, 173, 0, 103, 179, 139, 123, 265, 83, 104, 194, 116, 250, 186, 39, 152,
            91, 37, 137, 216, 96, 112, 71, 103, 0, 160, 151, 39, 236, 25, 75, 130, 61, 239, 95, 93, 112,
            74, 150, 114, 139, 114, 65, 194, 179, 160, 0, 54, 127, 86, 171, 89, 77, 99, 80, 134, 140, 50,
            90, 130, 154, 192, 76, 39, 203, 139, 151, 54, 0, 129, 133, 155, 78, 117, 99, 111, 159, 101, 71,
            55, 57, 100, 178, 93, 91, 74, 123, 39, 127, 129, 0, 199, 61, 53, 91, 30, 206, 63, 101, 78,
            145, 233, 141, 113, 200, 151, 254, 265, 236, 86, 133, 199, 0, 251, 171, 118, 176, 46, 182, 226, 125,
            108, 26, 161, 239, 91, 117, 90, 83, 25, 171, 155, 61, 251, 0, 83, 151, 75, 251, 119, 81, 127,
            41, 62, 116, 182, 51, 39, 127, 104, 75, 89, 78, 53, 171, 83, 0, 90, 24, 168, 99, 69, 49,
            49, 140, 37, 92, 139, 99, 136, 194, 130, 77, 117, 91, 118, 151, 90, 0, 80, 139, 65, 159, 50,
            33, 61, 100, 171, 72, 61, 104, 116, 61, 99, 99, 30, 176, 75, 24, 80, 0, 179, 76, 86, 52,
            151, 229, 169, 155, 185, 139, 269, 250, 239, 80, 111, 206, 46, 251, 168, 139, 179, 0, 202, 211, 128,
            69, 120, 49, 128, 148, 128, 75, 186, 95, 134, 159, 63, 182, 119, 99, 65, 76, 202, 0, 161, 90,
            111, 57, 185, 251, 26, 75, 163, 39, 93, 140, 101, 101, 226, 81, 69, 159, 86, 211, 161, 0, 115,
            24, 109, 84, 137, 92, 49, 144, 152, 112, 50, 71, 78, 125, 127, 49, 50, 52, 128, 90, 115, 0
    };

    private static final int NUM_ROWS = 21, NUM_COLS = 21;

    private final char name;
    private List<PointPair> neighbours = new ArrayList<>();

    /*
     * Method generates every possible node and its neighbours from
     * the table provided by the exam board (DISTANCE_DATA)
     * Each node is given a character based on its row (starting from 'A')
     */
    public static List<Point> generatePossibleNodes() {
        // generate all possible points in table (aka number of rows/columns)
        final List<Point> points = IntStream.range(0, NUM_ROWS)
                .mapToObj(i -> new Point((char)('A' + i)))
                .collect(Collectors.toList());

        // set point neighbours (distances to other points)
        // this code is actually quite ugly but i couldn't
        // think of a nicer way of writing it (plus java is
        // already syntactically quite ugly, go C++)

        IntStream.range(0, NUM_ROWS).forEach(i -> points.get(i).setNeighbours(
                IntStream.range(0, NUM_COLS)
                        .filter(j -> i != j)
                        .mapToObj(j -> new PointPair(points.get(j), DISTANCE_DATA[j + (NUM_COLS * i)]))
                        .collect(Collectors.toList())
        ));

        return points;
    }

    public Point(final char name) {
        this.name = name;
    }

    public final char getName() {
        return this.name;
    }

    public final List<PointPair> getNeighbours() {
        return this.neighbours;
    }

    @Nullable
    public final PointPair getNeighbour(final Point neighbour) {
        return this.neighbours.stream()
                .filter(p -> p.getPoint() == neighbour)
                .findFirst()
                .orElse(null);
    }

    public final void setNeighbours(@NotNull final List<PointPair> neighbours) {
        this.neighbours = neighbours;
    }

    public final void addNeighbour(final PointPair neighbour) {
        this.neighbours.add(neighbour);
    }

    public Point clone() throws CloneNotSupportedException
    {
        return (Point) super.clone();
    }
}
