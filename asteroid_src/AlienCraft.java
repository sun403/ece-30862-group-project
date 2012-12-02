import org.lwjgl.opengl.*;
import java.io.*;

public class AlienCraft extends Craft implements Serializable
{
    private static AlienCraft instance = null;
    private static UserCraft userCraftReference = null;

    private final Vector topLeft = new Vector(0.05 * Constants.WINDOW_WIDTH,
                                              0.05 * Constants.WINDOW_HEIGHT);
    private final Vector bottomMiddle = new Vector(Constants.WINDOW_WIDTH / 2, 
                                                   0.95 * Constants.WINDOW_HEIGHT);
    private final Vector topRight = new Vector(0.95 * Constants.WINDOW_WIDTH, 
                                               0.05 * Constants.WINDOW_HEIGHT);

    private AlienCraft() {
        super(Constants.ALIEN_CRAFT_RADIUS);
    }

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
            v.add(position);
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

    public void updatePosition()
    {
        super.updatePosition();
        Vector v;

        if(getPosition().distanceTo(topLeft) < 2 || 
           getPosition().distanceTo(new Vector(0, 0)) < 2)
        {
            v = new Vector(bottomMiddle.x - getPosition().x, 
                           bottomMiddle.y - getPosition().y);
            v.scalarMultiply(Constants.ALIEN_SPEED_CONSTANT);
            setVelocity(v);
        }
        else if(getPosition().distanceTo(bottomMiddle) < 2)
        {
            v = new Vector(topRight.x - getPosition().x, 
                           topRight.y - getPosition().y);
            v.scalarMultiply(Constants.ALIEN_SPEED_CONSTANT);
            setVelocity(v);
        }
        else if(getPosition().distanceTo(topRight) < 2)
        {
            v = new Vector(topLeft.x - getPosition().x, 
                           topLeft.y - getPosition().y);
            v.scalarMultiply(Constants.ALIEN_SPEED_CONSTANT);
            setVelocity(v);
        }
    }

	public void shoot()
	{
        //Vector pointing to the user craft
		//Vector rVector = userCraftReference.getPosition().difference(getPosition());
        Vector rVector = getPosition().difference(userCraftReference.getPosition());
        double missleTheta = rVector.angle();
        missleTheta = Constants.RANDOM_RANGE(missleTheta - Constants.ALIEN_DELTA_THETA, 
                                             missleTheta + Constants.ALIEN_DELTA_THETA);
        missleTheta = Math.toRadians(missleTheta);

		Vector misslePosition = new Vector(0, 0);
		misslePosition.rotate(missleTheta);;
		misslePosition.add(getPosition());

		Vector missleVelocity = new Vector(Constants.MISSLE_LAUNCH_VELOCITY * Math.cos(missleTheta),
										   Constants.MISSLE_LAUNCH_VELOCITY * Math.sin(missleTheta));
		missleVelocity.add(getVelocity());

		allSpaceObjects.add(new AlienMissle(missleVelocity, misslePosition));
	}
}
