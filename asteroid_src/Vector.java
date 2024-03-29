import java.io.*;

public class Vector implements Serializable
{
    private double x;
    private double y;

    public Vector() {
        x = 0; y = 0;
    }

    public Vector(Vector v)
    {
        x = v.getX();
        y = v.getY();
    }

    public Vector(double newX, double newY)
    {
        x = newX;
        y = newY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double newX) {
        x = newX;
    }

    public void setY(double newY) {
        y = newY;
    }

    public Vector scalarProduct(double scalar) {
        return new Vector(scalar * x, scalar * y);
    }

    public void scalarMultiply(double scalar) {
        x *= scalar;
        y *= scalar;
    }

    public void add(Vector v) {
        x += v.getX();
        y += v.getY();
    }

    public void flipY() {
        y *= -1;
    }

    //takes a degrees measurement
    public void rotate(double theta)
    {
        double xOld = x;
        double yOld = y;

        theta = Math.toRadians(theta);

        x = (xOld * Math.cos(theta)) - (yOld * Math.sin(theta));
        y = (xOld * Math.sin(theta)) + (yOld * Math.cos(theta));
    }

    public double distanceTo(Vector v) {
        return Math.sqrt(Math.pow(x - v.x, 2) + Math.pow(y - v.y, 2));
    }

    public double getMagnitude() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    //return this - that
    public Vector difference(Vector v) {
        return new Vector(x - v.x, y - v.y);
    }

    public Vector unitVector() {
        return new Vector(x / getMagnitude(), y / getMagnitude());
    }

    public Vector copy() {
        return new Vector(this);
    }

    public String toString() {
        return String.format("<%f, %f>", x, y);
    }

    //returns in degrees
    public double angle()
    {
        //baseAngle is on [0, 2*PI)
        double baseAngle = Math.atan2(y, x) + Math.PI;
        return Math.toDegrees(baseAngle);
    }
}
