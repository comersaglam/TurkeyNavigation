/**
 * This class is to store City values. It has 3 properties, Name, xPos, yPos.
 * @author Celaleddin Ömer Sağlam, Student ID:2023400348
 * @since Date: 04.04.2024
 */


public class City {
    // Fields representing the city's name and coordinates.
    private String name;
    private double xPos;
    private double yPos;

    /**
     * Constructs a City instance with specified name, x position, and y position.
     * 
     * @param name The name of the city.
     * @param xPos The x coordinate of the city.
     * @param yPos The y coordinate of the city.
     */
    public City(String name, double xPos, double yPos) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * Sets the x position of the city. The position must be a non-negative value.
     * 
     * @param xPos The x coordinate to be set.
     */
    public void setXPos(double xPos) {
        if (xPos >= 0) {
            this.xPos = xPos;
        } else {
            System.out.println("x should be positive");
        }
    }

    /**
     * Returns the x position of the city.
     * 
     * @return The x coordinate of the city.
     */
    public double getXPos() {
        return xPos;
    }

    /**
     * Sets the y position of the city. The position must be a non-negative value.
     * 
     * @param yPos The y coordinate to be set.
     */
    public void setYPos(double yPos) {
        if (yPos >= 0) {
            this.yPos = yPos;
        } else {
            System.out.println("y should be positive");
        }
    }

    /**
     * Returns the y position of the city.
     * 
     * @return The y coordinate of the city.
     */
    public double getYPos() {
        return yPos;
    }

    /**
     * Sets the name of the city.
     * 
     * @param name The new name of the city.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the city.
     * 
     * @return The name of the city.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the city, including its name, x position, and y position.
     * 
     * @return A string representation of the city.
     */
    @Override
    public String toString() {
        return "City{" +
                "name='" + name +
                ", x=" + xPos +
                ", y=" + yPos +
                '}';
    }

    /**
     * Calculates the Euclidean distance between this city and another city.
     * 
     * @param other The other city to which the distance is calculated.
     * @return The distance between this city and the specified city.
     */
    public double distanceTo(City other){
        double dx= this.xPos-other.xPos;
        double dy= this.yPos-other.yPos;
        return Math.sqrt(dx*dx+dy*dy);
    }
}