import java.util.ArrayList;
import java.io.*;

public class SpaceObject implements Serializable
{
    public static ArrayList<SpaceObject> allSpaceObjects;
    public static ArrayList<SpaceObject> objectsToAdd;
    public static ArrayList<SpaceObject> objectsToRemove;

    private double mass;
    private double radius;
    private Vector velocity;
    private Vector position;

    public SpaceObject() {
        this(0, 0, new Vector(), new Vector());
    }

    public SpaceObject(double newMass, double newRadius, 
            Vector newVelocity, Vector newPosition)
    {
        setMass(newMass);
        setRadius(newRadius);
        setVelocity(newVelocity);
        setPosition(newPosition);
    }

    public void setMass(double newMass) {
        mass = newMass;
    }

    public void setRadius(double newRadius) {
        radius = newRadius;
    }

    public void setVelocity(Vector newVelocity) {
        velocity = newVelocity;
    }

    public void setPosition(Vector newPosition) {
        position = newPosition;
    }

    public Vector getPosition() {
        return position;
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void updatePosition()
    {
        getPosition().add(velocity.scalarProduct(Constants.DELTA_T));

        //Rollover from right side of screen to left
        if(getPosition().getX() > Constants.MAX_POSITION.getX()) {
            getPosition().setX(0);
        }

        //Rollover from left side of screen to right
        else if(getPosition().getX() < 0) {
            getPosition().setX(Constants.MAX_POSITION.getX());
        }

        //Rollover from top of screen to bottom
        if(getPosition().getY() > Constants.MAX_POSITION.getY()) {
            getPosition().setY(0);
        }

        //Rollover from bottom of screen to top
        else if(getPosition().getY() < 0) {
            getPosition().setY(Constants.MAX_POSITION.getY());
        }
    }

    public void delete() {
        SpaceObject.objectsToRemove.add(this);
    }

    public void draw()
    {
        if(Constants.DRAW_DEBUG_CIRCLE) {
            Constants.drawCircle(position, radius, Constants.EMPTY_CIRCLE);
        }
    }
}
