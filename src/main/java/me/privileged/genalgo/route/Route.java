package me.privileged.genalgo.route;

import com.sun.istack.internal.NotNull;
import me.privileged.genalgo.point.PointPair;
import me.privileged.genalgo.point.Point;

import java.util.*;
import java.util.stream.Collectors;

/*
 * Route represents a route from a given point to another point
 *
 * In a Genetic algorithm, this may be thought of as a collection of genes (points)
 */

public final class Route {
    private List<PointPair> route = new ArrayList<>();
    final Random random = new Random();

    /*
     * Method to actually generate a random route from start to end
     * This route can then be compared to other, possibly shorter routes
     * and 'Genes' can be inherited to produce shorter and shorter routes
     * over various populations
     *
     * In this problem we have to visit every Node
     */
    @NotNull
    public static Route generateRandomRoute(@NotNull final List<Point> possibleNodes, @NotNull final Point start) {
        final List<PointPair> pairs = new ArrayList<>(possibleNodes.size() + 2);
        final List<Point> pointsToVisit = new ArrayList<>(possibleNodes); // clone
        pointsToVisit.remove(start); // remove the start node as we're already there
        Point currentPoint = start; // create a temporary reference
        final Random random = new Random();

        // initialise 'pairs' with random points
        while (!pointsToVisit.isEmpty()) {
            // generate a random point
            final int randomIndex = random.nextInt(pointsToVisit.size());
            final Point randomPoint = pointsToVisit.get(randomIndex);
            final PointPair randomPointPair = currentPoint.getNeighbour(randomPoint);

            // ensure the point is connected to the current point
            if (randomPointPair == null)
                continue;

            // if it is, add it to the route and remove from
            // the points we need to visit
            pairs.add(randomPointPair);
            pointsToVisit.remove(randomIndex);
            currentPoint = randomPoint;
        }

        // add the start point as we need to return home
        pairs.add(pairs.get(pairs.size() - 1).getPoint().getNeighbour(start));
        return new Route(pairs);
    }

    public Route() {
    }

    public Route(final List<PointPair> route) {
        this.route = route;
    }

    /*
     * This distance can be equated to a traditional genetic algorithm
     * 'Fitness'. We will use it to compare how 'good' a route is
     */
    public final int calculateDistance() {
        return this.route.stream()
                .mapToInt(PointPair::getDistance)
                .sum();
    }

    /*
     * Returns true if the route contains the point given
     * Useful for generating a random gene
     */
    public final boolean containsPoint(final Point point) {
        return this.route.stream()
                .anyMatch(p -> p.getPoint() == point);
    }

    /*
     * Generate random point that is not in the route
     */
    public final PointPair generateRandomPoint(final PointPair start) {
        final List<PointPair> filtered = start.getPoint().getNeighbours().stream()
                .filter(p -> !this.containsPoint(p.getPoint()))
                .collect(Collectors.toList());

        if (filtered.isEmpty())
            return start;

        return filtered.get(this.random.nextInt(filtered.size()));
    }

    /*
     * This method merges and mutates the current route points with a given other
     * point at a 50/50 chance each point
     *
     * Ideally will lead to an evolving population over iterations
     */
    public final void updateRoute(final Route otherRoute, final PointPair start) {
        if (this == otherRoute)
            return; // no inbreeding

        // so ugly
        this.route.clear();
        otherRoute.route.forEach(p -> this.route.add(this.random.nextDouble() >= 0.9D && !this.containsPoint(p.getPoint()) ? p : this.generateRandomPoint(start)));
    }

    public final void printRoute() {
        this.route.forEach(p -> System.out.printf("%c ", p.getPoint().getName()));
        System.out.print('\n');
    }
}
