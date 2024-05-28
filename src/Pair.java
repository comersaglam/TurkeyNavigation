/**
 * Represents a pair consisting of a distance value and a list of city names forming an optimal route.
 * This class is used to store the result of route calculations, encapsulating the total distance of the
 * route and the sequential list of cities that form the path of the route.
 * <p>
 * The distance represents the total length of the route between the start and destination cities,
 * while the path is a list of city names in the order they are visited.
 * </p>
 * 
 * @author Celaleddin Ömer Sağlam
 * @since 04.04.2024
 */

import java.util.List;
public class Pair {
    // The total distance of the path between two cities
    double distance;

    // A list representing the optimal route between two cities, as a sequence of city names.
    List<String> path;

    /**
     * Constructs a Pair with a specified distance and path.
     * 
     * @param distance The total distance of the route.
     * @param path A list of city names representing the route.
     */
    public Pair(double distance, List<String> path) {
        this.distance = distance;
        this.path = path;
    }

    /**
     * Returns the total distance of the route.
     * 
     * @return The total distance of the route.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Returns the list of city names that form the route.
     * 
     * @return A list of city names representing the route.
     */
    public List<String> getPath() {
        return path;
    }

    /**
     * Returns a string representation of the Pair, including the total distance
     * and the sequential list of cities in the route.
     * 
     * @return A string representation of the Pair, formatted with the total distance and path.
     */
    @Override
    public String toString() {
        return  "Total Distance: " + String.format("%.2f", distance) +
                ". Path: " + String.join(" -> ", path);
    }
    
}