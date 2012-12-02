import org.lwjgl.opengl.*;
import java.io.*;

public class AlienCraft extends Craft implements Serializable
{
    private static AlienCraft instance = null;

    //AlienCraft needs a reference to the userCraft so that it's
    //able to track it's position, and shoot at it.
    private static UserCraft userCraftReference = null;

    //These mark the the top left, bottom middle, and the top right of the 
    //game window. I don't use the *absolute* corners, because otherwise the 
    //AlienCraft goes half off screen when flying, and that doesn't look good.
    //Using 95% of the values keeps the AlienCraft in the field of view at 
    //all times.
    private final Vector topLeft = new Vector(0.05 * Constants.WINDOW_WIDTH,
                                              0.05 * Constants.WINDOW_HEIGHT);
    private final Vector bottomMiddle = new Vector(Constants.WINDOW_WIDTH / 2, 
                                                   0.95 * Constants.WINDOW_HEIGHT);
    private final Vector topRight = new Vector(0.95 * Constants.WINDOW_WIDTH, 
                                               0.05 * Constants.WINDOW_HEIGHT);

    private AlienCraft() {
        super(Constants.ALIEN_CRAFT_RADIUS);
    }

    //Decided to make this a singleton since there 
    //should and will only ever be one alien 
    //on the field at once.
    public static AlienCraft getInstance()
    {
        if(instance == null) {
            instance = new AlienCraft();
        }
        
        return instance;
    }

    public void setUserCraftReference(UserCraft reference) {
        userCraftReference = reference;
    }

	public void draw()
	{
        super.draw();

        //The points that make up the AlienCraft for drawing.
        //Can't put these in Constants, because they get altered below.
		Vector x1 = new Vector(0, 2);
		Vector x2 = new Vector(1, 1);
		Vector x3 = new Vector(2, 1);
		Vector x4 = new Vector(3, 0);
		Vector x5 = new Vector(2, -1);
        Vector x6 = new Vector(-2, -1);
        Vector x7 = new Vector(-3, 0);
        Vector x8 = new Vector(-2, 1);
        Vector x9 = new Vector(-1, 1);

        Vector[] points = {x1, x2, x3, x4, x5, x6, x7, x8, x9};

        for(Vector v : points)
        {
            v.scalarMultiply(10.0);
            v.flipY();
            v.add(getPosition());
        }

        //Playing connect the dots...
        for(int i = 0; i < points.length - 1; i++) {
            new Line(points[i], points[i + 1]).draw();
        }
        new Line(points[0], points[points.length - 1]).draw();


        //Draws the long horizontal line that "cuts" the ship in two
        new Line(x4, x7).draw();

        //Draws the line under the "cockpit"
        new Line(x2, x8).draw();
	}

    //Recalculates the AlienCraft's velocity to make sure it's
    //heading for the corner it's supposed to be heading for.
    //Without this, after a full "circle" around the "map",
    //the craft would not end up EXACTLY where it started.
    //Thus leading to an unprofessional looking "drift" in it's
    //path.
    private void recalculateVelocity()
    {
        Vector v;

        if(getPosition().distanceTo(topLeft) < 2 || 
           getPosition().distanceTo(new Vector(0, 0)) < 2)
        {
            v = new Vector(bottomMiddle.getX() - getPosition().getX(), 
                           bottomMiddle.getY() - getPosition().getY());
            v.scalarMultiply(Constants.ALIEN_SPEED_CONSTANT);
            setVelocity(v);
        }
        else if(getPosition().distanceTo(bottomMiddle) < 2)
        {
            v = new Vector(topRight.getX() - getPosition().getX(),
                           topRight.getY() - getPosition().getY());
            v.scalarMultiply(Constants.ALIEN_SPEED_CONSTANT);
            setVelocity(v);
        }
        else if(getPosition().distanceTo(topRight) < 2)
        {
            v = new Vector(topLeft.getX() - getPosition().getX(),
                           topLeft.getY() - getPosition().getY());
            v.scalarMultiply(Constants.ALIEN_SPEED_CONSTANT);
            setVelocity(v);
        }
    }

    public void updatePosition()
    {
        super.updatePosition();
        recalculateVelocity();
    }

	public void shoot()
	{
        //Vector pointing to the user craft
        Vector rVector = getPosition().difference(userCraftReference.getPosition());
        double missleTheta = rVector.angle();

        //We shoot the missle at some range of thetas, so we're close to hitting the 
        //UserCraft, but we don't always hit it.
        missleTheta = Constants.RANDOM_RANGE(missleTheta - Constants.ALIEN_DELTA_THETA, 
                                             missleTheta + Constants.ALIEN_DELTA_THETA);
        missleTheta = Math.toRadians(missleTheta);

		Vector misslePosition = new Vector(0, 0);
		misslePosition.rotate(missleTheta);
		misslePosition.add(getPosition());

		Vector missleVelocity = new Vector(Constants.MISSLE_LAUNCH_VELOCITY * Math.cos(missleTheta),
										   Constants.MISSLE_LAUNCH_VELOCITY * Math.sin(missleTheta));
		missleVelocity.add(getVelocity());

		allSpaceObjects.add(new AlienMissle(missleVelocity, misslePosition));
	}
}
