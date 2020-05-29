package logic;

import commands.OutOfBoundsException;

import java.io.Serializable;
import java.util.Scanner;

/**
 * Class with coordinates which we using in collection like field.
 */
public class Coordinates implements Serializable {
    private Double x; //Максимальное значение поля: 736, Поле не может быть null
    private double y; //Значение поля должно быть больше -119
    private final Double X_MAXVALUE = 736d;
    private final double Y_MINVALUE = -119;

    /**
     * Constructor which throws exception, if we have out of bounds.
     * @param x - X coordinate.
     * @param y - Y coordinate.
     * @throws OutOfBoundsException - throws exception, if we out of X_MAXVALUE and Y_MINVALUE.
     */
    public Coordinates(Double x, double y) throws OutOfBoundsException {
        if (x > X_MAXVALUE || y < Y_MINVALUE) throw new OutOfBoundsException();

        this.x = x;
        this.y = y;
    }

    /**
     * This method returns X coordinate field.
     * @return - X coordinate.
     */
    public Double getX() {return  x;}

    /**
     * This method returns Y coordinate field.
     * @return - Y coordinate.
     */
    public double getY() {return  y;}

    /**
     * This method set X coordinate in instance.
     * @param x - coordinate which we want to set.
     * @throws OutOfBoundsException - throws exception, if we bound of X_MAXVALUE.
     */
    public void setX(Double x) throws OutOfBoundsException {
        if (x > X_MAXVALUE) throw new OutOfBoundsException();
        this.x = x;
    }

    /**
     * This method set Y coordinate in instance.
     * @param y - coordinate which we want to set.
     * @throws OutOfBoundsException - throws exception, if we bound of Y_MINVALUE.
     */
    public void setY(double y) throws OutOfBoundsException {
        if (y < Y_MINVALUE) throw new OutOfBoundsException();
        this.y = y;
    }

    /**
     * This method returns values of instance in string.
     * @return string with values of instance.
     */
    @Override
    public String toString() {
        return ("{x: " + x + ", y: " + y + "}");
    }

    public static Coordinates generateObjectUserInput() {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter coordinate x (Double): ");
        double x = Double.parseDouble(input.nextLine());
        System.out.println("Enter coordinate у (double): ");
        double y = Double.parseDouble(input.nextLine());

        try {
            return new Coordinates(x, y);
        } catch (OutOfBoundsException e) {
            System.out.println("Out of bound exception!");
        }
        return null;
    }
}