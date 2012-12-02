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

    public static GravitationalObject getInstance()
    {
        if(instance == null) {
            instance = new GravitationalObject();
        }

        return instance;
    }

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
            Constants.drawCircle(getPosition(), radius, Constants.FULL_CIRCLE);
        }
    }
}

