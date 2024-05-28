/**
 * TRNavigation is a program that calculates and displays the optimal route between two cities in Turkey.
 * It reads city coordinates and connections from files, allows the user to select a start and destination city,
 * and then calculates the shortest path using Dijkstra's algorithm. The program also visually displays the map,
 * cities, connections, and the calculated shortest path.
 *
 * @author Celaleddin Ömer Sağlam, Student ID:2023400348
 * @since Date: 04.04.2024
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class TRNavigation {
    // Screen dimensions for the map display
    public static int canvas_width = 2377; //screen width
    public static int canvas_height = 1055; // screen height

    // Lists and maps for storing city information and adjacencies
    public static List<City> cities = new ArrayList<>();
    public static Map<String, City> citiesMap;
    public static Map<String, String[]> citiesAdj;
    public static Map<String, Map<String, Double>> adjacencyList;

    // Starting and ending city names
    public static String start = "";
    public static String end = "";

     /**
     * The main method initiates the program: reads city data, processes user input, 
     * calculates the shortest path, and then draws the result on a graphical interface.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Initialize variables
        toCities("city_coordinates.txt");
        toMap(cities);
        toAdjList("city_connections.txt");

        // Input taking and Shortest Path Calculation
        takeInput();
        Pair result = Dijkstra(start, end);
        if (result == null) {
            System.out.println("No path could be found");
            return;
        }
        System.out.println(result.toString());

        // Draw Canvas and Map
        StdDraw.setCanvasSize(canvas_width, canvas_height);
        StdDraw.setXscale(0, canvas_width);
        StdDraw.setYscale(0, canvas_height);
        StdDraw.picture(canvas_width / 2.0, canvas_height / 2.0, "map.png");

        StdDraw.enableDoubleBuffering();
        drawCities();
        drawWays("city_connections.txt");
        drawShortestPath(result);
        StdDraw.show();
    }

    /**
     * Prompts the user for starting and destination cities, ensuring valid input.
     */
    public static void takeInput() {
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("Enter starting city:");
            start = sc.nextLine().toLowerCase();
            if (citiesMap.containsKey(start)){
                break;
            }
            System.out.println("City named " + start + " not found. Please enter a valid city name.");
        }
        while (true){
            System.out.println("Enter destination city:");
            end = sc.nextLine().toLowerCase();
            if (citiesMap.containsKey(end)){
                break;
            }
            System.out.println("City named " + end + " not found. Please enter a valid city name.");
        }
        
    }

    /**
     * Prints the list of cities to the console.
     * @param cities The list of City objects to be printed.
     */
    public static void printCities(List<City> cities) {
        for (City city : cities) {
            System.out.println(city.toString());
        }
    }

    /**
     * Reads city coordinates from a file and adds them to the cities list.
     * @param txtname The name of the file containing city coordinates.
     */
    public static void toCities(String txtname) {
        try (BufferedReader br = new BufferedReader(new FileReader(txtname))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split(", ");
                cities.add(new City(lineSplit[0].toLowerCase(), Double.parseDouble(lineSplit[1]), Double.parseDouble(lineSplit[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates the citiesMap with city names and City objects for quick lookup.
     * @param cities The list of City objects used to populate the map.
     */
    public static void toMap(List<City> cities) {
        citiesMap = new HashMap<>();
        for (City city : cities) {
            citiesMap.put(city.getName(), city);
        }
    }

    /**
     * Reads city connections from a file and constructs the adjacency list representing the graph of cities.
     * @param txtname The name of the file containing city connections.
     */
    public static void toAdjList(String txtname) {
        adjacencyList = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(txtname))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split(",");
                String city1 = lineSplit[0].toLowerCase();
                String city2 = lineSplit[1].toLowerCase();
                double distance = citiesMap.get(city1).distanceTo(citiesMap.get(city2));
    
                adjacencyList.putIfAbsent(city1, new HashMap<>());
                adjacencyList.putIfAbsent(city2, new HashMap<>());
    
                adjacencyList.get(city1).put(city2, distance);
                adjacencyList.get(city2).put(city1, distance);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws a connection between two cities on the map.
     * @param city1 The first city in the connection.
     * @param city2 The second city in the connection.
     */
    public static void drawSingleConnection(City city1, City city2) {
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.line(city1.getXPos(), city1.getYPos(), city2.getXPos(), city2.getYPos());
    }

     /**
     * Draws the shortest path between two cities on the map.
     * @param city1 The starting city of the path.
     * @param city2 The ending city of the path.
     */
    public static void drawSinglePath(City city1, City city2) {
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.01);
        StdDraw.line(city1.getXPos(), city1.getYPos(), city2.getXPos(), city2.getYPos());
        StdDraw.setPenRadius();
    }

    /**
     * Draws all cities on the map, marking them with their names.
     */
    public static void drawCities() {
        StdDraw.setPenColor(StdDraw.GRAY);
        for (City city : cities){
            String cityName = city.getName();
            double estimatedWidth = cityName.length() * 5;
            StdDraw.text(city.getXPos() - estimatedWidth / 2, city.getYPos() + 10, cityName);
        }
    }

    /**
     * Draws all connections between cities as specified in the given text file.
     * @param txtname The name of the file containing city connections.
     */
    public static void drawWays(String txtname) {
        try (BufferedReader br = new BufferedReader(new FileReader(txtname))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split(",");
                String city1 = lineSplit[0].toLowerCase();
                String city2 = lineSplit[1].toLowerCase();

                drawSingleConnection(citiesMap.get(city1), citiesMap.get(city2));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Calculates the shortest path between two cities using Dijkstra's algorithm.
     * @param city1 The name of the starting city.
     * @param city2 The name of the destination city.
     * @return A Pair object containing the total distance and the list of cities forming the shortest path.
     */
    public static Pair Dijkstra(String city1, String city2) {
        Map<String, Double> shortestDistances = new HashMap<>();
        Map<String, String> previousCities = new HashMap<>();
        PriorityQueue<Pair> queue = new PriorityQueue<>(Comparator.comparing(Pair::getDistance));

        for (String city : adjacencyList.keySet()) {
            shortestDistances.put(city, Double.MAX_VALUE);
        }
        shortestDistances.put(city1, 0.0);

        queue.add(new Pair(0.0, Arrays.asList(city1)));

        while (!queue.isEmpty()) {
            Pair currentPair = queue.poll();
            String currentCity = currentPair.getPath().get(currentPair.getPath().size() - 1);

            if (currentCity.equals(city2)) {
                return currentPair;
            }

            for (Map.Entry<String, Double> entry : adjacencyList.get(currentCity).entrySet()) {
                String adjacentCity = entry.getKey();
                double distanceToAdjacent = entry.getValue();

                double newDistance = shortestDistances.get(currentCity) + distanceToAdjacent;

                if (newDistance < shortestDistances.get(adjacentCity)) {
                    shortestDistances.put(adjacentCity, newDistance);
                    List<String> newPath = new ArrayList<>(currentPair.getPath());
                    newPath.add(adjacentCity);
                    queue.add(new Pair(newDistance, newPath));
                    previousCities.put(adjacentCity, currentCity);
                }
            }
        }

        return null;
    }
    
    /**
     * Draws the shortest path on the map using a different color to distinguish it.
     * @param result The Pair object containing the path information.
     */
    public static void drawShortestPath(Pair result) {
        List path = result.getPath();
        int n = path.size();
        for (int i = 0; i < n-1; i ++) {
            City city1 = citiesMap.get(path.get(i));
            City city2 = citiesMap.get(path.get(i+1));
            drawSinglePath(city1, city2);
        }
    }
}
