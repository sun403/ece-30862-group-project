import java.io.*;

public class GravitationalObject extends SpaceObject implements Serializable
{
    private static GravitationalObject instance;
    private boolean toDraw = false;

    //Gets initialized in the "off" state
    private GravitationalObject()
    {
        super(
                0,
                Constants.GRAVITATIONAL_OBJECT_RADIUS,
                new Vector(),
                new Vector(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2));
    }

    //Made this a singleton because there should and will
    //only ever be one GravitationalObject
    public static GravitationalObject getInstance()
    {
        if(instance == null) {
            instance = new GravitationalObject();
        }

        return instance;
    }

    //The GravitationalObject is always present. However when it's turned off,
    //the UserCraft does not feel it's effects, and it's positioned off screen somewhere.
    //
    //The following 2 methods turn it's effects on and off

    public void turnOn()
    {
        setMass(Constants.GRAVITATIONAL_OBJECT_MASS);
        setPosition(new Vector(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2));
        toDraw = true;
    }

    public void turnOff()
    {
        setMass(0);
        setPosition(new Vector(-1, -1));
        toDraw = false;
    }

    public void draw()
    {
        if(toDraw) {
            Constants.drawCircle(getPosition(), getRadius(), Constants.FULL_CIRCLE);
        }
    }
}

