import org.lwjgl.opengl.*;
import java.io.*;

public class Missle extends SpaceObject implements Serializable
{
	public Missle(Vector launchVelocity, Vector launchPosition)
    {
		super(Constants.MISSLE_MASS, Constants.MISSLE_RADIUS * Constants.SCALING_FACTOR, 
              launchVelocity, launchPosition);
	}

	public void draw()
    {
        super.draw();
		Constants.drawCircle(getPosition(), getRadius(), Constants.FULL_CIRCLE);
	}

	//We override updatePosition() because Missles do not rollover the screen.
	public void updatePosition()
	{
		getPosition().add(getVelocity().scalarProduct(Constants.DELTA_T));

		if(getPosition().getX() > Constants.MAX_POSITION.getX() || 
           getPosition().getX() < 0 ||
           getPosition().getY() > Constants.MAX_POSITION.getY() || 
           getPosition().getY() < 0)
        {
            this.delete();
		}
	}
}
