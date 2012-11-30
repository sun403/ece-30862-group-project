import org.lwjgl.opengl.*;
import java.io.*;

public class Missle extends SpaceObject implements Serializable
{
	public Missle(Vector launchVelocity, Vector launchPosition) {
		super(Constants.MISSLE_MASS, Constants.MISSLE_RADIUS * Constants.SCALING_FACTOR, launchVelocity, launchPosition);
	}

	public void draw()
    {
        super.draw();
		Constants.drawCircle(position, radius, Constants.FULL_CIRCLE);
	}

	//We override updatePosition() because Missles do not rollover the screen.
	public void updatePosition()
	{
		position.add(velocity.scalarProduct(Constants.DELTA_T));

		if(position.x > Constants.MAX_POSITION.x || position.x < 0 ||position.y > Constants.MAX_POSITION.y || position.y < 0) {
			delete();
		}
	}
}
