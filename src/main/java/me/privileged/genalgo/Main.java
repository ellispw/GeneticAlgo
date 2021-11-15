package me.privileged.genalgo;

import me.privileged.genalgo.point.Point;
import me.privileged.genalgo.route.Route;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * This program requires >= java 8 for stream api because its amazing
 * if you're not already on at least java 8, just update anyways
 */

public final class Main {
    private static final int NUM_POPULATIONS = 100, NUM_ITERATIONS = 1000;

    public static void main(final String[] args) {
        System.out.println("'A' is the start node, so we will return home at the end");

        final List<Point> possibleNodes = Point.generatePossibleNodes();
        final List<Route> routes = generateInitialPopulation(possibleNodes);

        Route fastestRoute = null; // store the fastest Route

        // main generational loop
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            // find the shortest route
            final Route minRoute = routes.stream()
                    .min(Comparator.comparing(Route::calculateDistance))
                    .orElse(null);

            if (minRoute == null)
                break; // you done goofed

            // update the fastest route accordingly
            if (fastestRoute == null
                    || minRoute.calculateDistance() < fastestRoute.calculateDistance())
                fastestRoute = minRoute;

            // update the route via breeding with most
            // successful parent in order to hopefully
            // create a successful child
            routes.forEach(r -> r.updateRoute(minRoute, possibleNodes.get(1).getNeighbour(possibleNodes.get(0))));

            if (i % 10 == 0)
                System.out.printf("%d%n", fastestRoute.calculateDistance());
        }

        if (fastestRoute == null) {
            System.out.println("Failed to find a route");
            return;
        }

        System.out.printf("Fastest route: %dkm%n", fastestRoute.calculateDistance());
        fastestRoute.printRoute();
    }

    private static List<Route> generateInitialPopulation(final List<Point> possibleNodes) {
        // create initial population of routes
        return IntStream.range(0, NUM_POPULATIONS)
                .mapToObj(i -> Route.generateRandomRoute(possibleNodes, possibleNodes.get(0)))
                .collect(Collectors.toList());
    }
}