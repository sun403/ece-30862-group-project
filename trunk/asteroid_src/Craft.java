import java.io.*;
import org.lwjgl.opengl.*;

public class Craft extends SpaceObject implements Serializable
{
    //Controls how the craft is rotated.
    //-90 points north
    private double theta = -90;
    private int lives = 3;

    public Craft(double craftRadius)
    {
        super(Constants.CRAFT_MASS, craftRadius,
                new Vector(0.0, 0.0), new Vector(0.0, 0.0));
    }

    public void changeTheta(double dTheta) {
        theta += dTheta;
    }

    public void setTheta(double newTheta) {
        theta = newTheta;
    }

    public void resetTheta() {
        theta = -90;
    }

    public void resetPosition() {
        setPosition(new Vector());
    }

    public void resetVelocity() {
        setVelocity(new Vector());
    }

    public int getLives() {
        return lives;
    }

    public void subtractLife() {
        lives--;
    }

    public void resetLives() {
        lives = 3;
    }

    public double getTheta() {
        return theta;
    }
}
