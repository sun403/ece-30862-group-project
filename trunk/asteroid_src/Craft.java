import org.lwjgl.opengl.*;

public class Craft extends SpaceObject
{
	//Controls how the craft is rotated.
    //-90 points north
	protected double theta = -90;
	private int lives = 3;

	public Craft(double craftRadius)
    {
		super(Constants.CRAFT_MASS, craftRadius,
              new Vector(0.0, 0.0), new Vector(0.0, 0.0));
	}

	public void changeTheta(double dTheta) {
		theta += dTheta;
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

    public void resetTheta() {
        theta = -90;
    }
}
