package logic;

import commands.OutOfBoundsException;

import java.io.Serializable;
import java.util.Scanner;

/**
 * The general class in our collection. Collection contains elements of routes.
 * Class route contains coordinate and location classes.
 */
public class Route implements Comparable<Route>, Serializable {
    private int id = 0; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private static int lastIdAdded = 1;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Location from; //Поле не может быть null
    private Location to; //Поле может быть null
    private float distance; //Значение поля должно быть больше 1

    /**
     * Default constructor for setters.
     */
    public Route() {}
    Route(String name, Coordinates coordinates, Location from, Location to, float distance)
            throws NullPointerException, OutOfBoundsException {
        if (distance < 1) throw new OutOfBoundsException();
        if (name == null || coordinates == null || from == null || to == null)
            throw new NullPointerException();

        this.id = lastIdAdded++;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = java.time.ZonedDateTime.now();
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    /**
     * This method sets name of instance.
     * @param name - new name of instance.
     */
    public void setName(String name) {
        if (name == null || name.trim().equals("")) throw new NullPointerException("Name cant be null");
        this.name = name;
    }

    /**
     * This method sets coordinates of instance.
     * @param coordinates - field with type Coordinates.
     */
    public void setCoordinates(Coordinates coordinates) {this.coordinates = coordinates;}

    /**
     * This method sets Location From field of instance.
     * @param location - field with type Location.
     */
    public void setFrom(Location location) {
        this.from = location;
    }

    /**
     * This method sets Location To field of instance.
     * @param location - field with type Location.
     */
    public void setTo(Location location) {
        this.to = location;
    }

    /**
     * This method sets distance field in instance.
     * @param distance - float field of instance.
     * @throws OutOfBoundsException - throws exception, if distance out of min_value = 1.
     */
    public void setDistance(float distance) throws OutOfBoundsException {
        if (distance < 1) throw new OutOfBoundsException();
        this.distance = distance;
    }

    /**
     * This method sets unique id for instance.
     */
    public void setId() {
        this.id = lastIdAdded++;
    }

    /**
     * This method generates creation date of instance.
     */
    public void setCreationDate() {this.creationDate = java.time.ZonedDateTime.now();}
    public void setCreationDate(boolean bool) {this.creationDate = null;}
    public void setCreationDateForParse (java.time.ZonedDateTime time) {
        this.creationDate = time;
    }

    /**
     * This method resets id for instance.
     */
    public static void resetId() {lastIdAdded = 1;}

    /**
     * This method returns id of instance.
     * @return - returns id.
     */
    public int getId() {return this.id;}

    /**
     * This method returns name of instance.
     * @return - returns name.
     */
    public String getName() {return this.name;}

    /**
     * This method returns coordination fields of instance.
     * @return - returns coordinates.
     */
    public Coordinates getCoordinates() {return coordinates;}

    /**
     * This method returns Location From of instance.
     * @return - returns location from.
     */
    public Location getFrom() {return from;}

    /**
     * This method returns Location To of instance.
     * @return - return location to.
     */
    public Location getTo() {return to;}

    /**
     * This method returns distance field of instance.
     * @return - returns distance.
     */
    public float getDistance() {return distance;}

    /**
     * This method returns creation date of instance.
     * @return - returns date.
     */
    public java.time.ZonedDateTime getCreationDate() {return creationDate;}

    /**
     * Overridden method compareTo to make ascending sort by distance as default.
     * @param route - instance of class.
     * @return - returns comparision of distance fields.
     */
    @Override
    public int compareTo(Route route) {
        if (distance > route.getDistance()) return 1;
        else if (distance == route.getDistance()) return 0;
        else return  -1;
    }

    /**
     * This method returns string with fields of instance.
     * @return - returns string.
     */
    @Override
    public String toString() {
        if (to == null) {
            return ("Route [id = " + id + ", name = " + name + ", coordinates = " + coordinates.toString() +
                    ", creation date = " + creationDate.getYear() + "-"  + creationDate.getMonthValue() + "-" +
                    creationDate.getDayOfMonth() + ", location from = " + from.toString() + ", location to = null"
                    + ", distance = " + distance + "]");
        }
        else {
            return ("Route [id = " + id + ", name = " + name + ", coordinates = " + coordinates.toString() +
                    ", creation date = " + creationDate.getYear() + "-"  + creationDate.getMonthValue() + "-" +
                    creationDate.getDayOfMonth() + ", location from = " + from.toString() + ", location to = " +
                    to.toString() + ", distance = " + distance + "]");
        }
    }

    public static Route generateObjectUserInput() {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter name: ");
        String name = input.nextLine();

        Location locationFrom = Location.generateObjectUserInput();
        Location locationTo = Location.generateObjectUserInput();
        //LOCATION TO CAN BE NULL!

        Coordinates coordinates = Coordinates.generateObjectUserInput();

        System.out.println("Enter distance (float): ");
        float distance = Float.parseFloat(input.nextLine());
        try {
            return new Route(name, coordinates, locationFrom, locationTo, distance);
        } catch (OutOfBoundsException e) {
            System.out.println("Out of bounds exception");
        }

        return null;
    }
}