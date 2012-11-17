import org.lwjgl.opengl.*;

public class Missle extends SpaceObject
{
	public Missle(Vector LAUNCH_VELOCITY, Vector launchPosition) {
		super(Constants.MISSLE_MASS, Constants.MISSLE_RADIUS * Constants.SCALING_FACTOR, LAUNCH_VELOCITY, launchPosition);
	}

	public void draw() {
		Constants.drawCircle(position, radius);
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