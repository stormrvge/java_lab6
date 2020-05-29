package logic;

import java.io.Serializable;
import java.util.Scanner;

/**
 * Class which we using like a field in collection. Contains 3 coordinates.
 */
public class Location implements Serializable {
    private Float x; //Поле не может быть null
    private Integer y; //Поле не может быть null
    private int z;

    /**
     * Constructor which sets coordinates. Throws exception if X or Y coordinates equal null.
     * @param x - X coordinate.
     * @param y - Y coordinate.
     * @param z - Z coordinate.
     * @throws NullPointerException - throws exception, if X or Y coordinates equal null.
     */
    public Location(Float x, Integer y, int z) throws NullPointerException {
        if (x == null || y == null) throw new NullPointerException();

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location() {}
    /**
     * This method returns X coordinate.
     * @return - returns X coordinate.
     */
    public Float getX() {return this.x;}

    /**
     * This method returns Y coordinate.
     * @return - returns Y coordinate.
     */
    public Integer getY() {return this.y;}

    /**
     * This method returns Z coordinate.
     * @return - returns Z coordinate.
     */
    public int getZ() {return this.z;}

    /**
     * This method sets X coordinate.
     * @param x - coordinate which we want to set.
     * @throws NullPointerException - throws exception, if X coordinate equals null.
     */
    public void setX(Float x) throws NullPointerException {
        if (x == null) throw new NullPointerException();
        this.x = x;
    }

    /**
     * This method sets Y coordinate.
     * @param y - coordinate which we want to set.
     * @throws NullPointerException = throws exception, if Y coordinate equals null.
     */
    public void setY(Integer y) throws NullPointerException {
        if (y == null) throw new NullPointerException();
        this.y = y;
    }

    /**
     * This method sets Z coordinate.
     * @param z - coordinate which we want to set.
     */
    public void setZ(int z) {this.z = z;}

    @Override
    public String toString() {
        return ("{x: " + x + ", y: " + y + ", z: " + z + "}");
    }

    public static Location generateObjectUserInput() {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter coordinate x (Float, Location from): ");
        Float x_From = Float.parseFloat(input.nextLine());
        System.out.println("Enter coordinate y (Integer, Location from): ");
        Integer y_From = Integer.parseInt(input.nextLine());
        System.out.println("Enter coordinate z (int, Location from): ");
        int z_From = Integer.parseInt(input.nextLine());

        try {
            return new Location(x_From, y_From, z_From);
        } catch (NullPointerException e) {
            System.out.println("Argument cant be null!");
        }
        return null;
    }
}