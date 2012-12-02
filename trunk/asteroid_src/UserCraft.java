import org.lwjgl.opengl.*;
import java.io.*;

public class UserCraft extends Craft implements Serializable
{
    private static UserCraft instance = null;

	//When accelerating this is true
	private boolean drawThruster = false;
    private static GravitationalObject gravityObject = null;

    private UserCraft() {
        super(Constants.USER_CRAFT_RADIUS);
    }

	public static UserCraft getInstance()
    {
        if(instance == null) {
            instance = new UserCraft();
        }

        return instance;
	}

	public void draw()
	{
        super.draw();

		Vector v1 = new Vector(2 * 15, 0);
		Vector v2 = new Vector(-15, -1.25 * 15);
		Vector v3 = new Vector(-15, 1.25 * 15);
		Vector v4 = new Vector(0, .83 * 15);
		Vector v5 = new Vector(0, -.83 * 15);

		v1.rotate(theta);
		v2.rotate(theta);
		v3.rotate(theta);
		v4.rotate(theta);
		v5.rotate(theta);

		v1.add(position);
		v2.add(position);
		v3.add(position);
		v4.add(position);
		v5.add(position);

		Line lineOne = new Line(v3, v1);
		Line lineTwo = new Line(v2, v1);
		Line lineThree = new Line(v4, v5);

		lineOne.draw();
		lineTwo.draw();
		lineThree.draw();

		if(drawThruster)
        {
			/* Thruster */
			Vector v6 = new Vector(-15, 0);
			Vector v7 = new Vector(0, .42 * 15);
			Vector v8 = new Vector(0, -.42 * 15);

			v6.rotate(theta);
			v7.rotate(theta);
			v8.rotate(theta);

			v6.add(position);
			v7.add(position);
			v8.add(position);

			Line lineFour = new Line(v6, v7);
			Line lineFive = new Line(v6, v8);

			lineFour.draw();
			lineFive.draw();

			drawThruster = false;
		}
	}

    //Recall:
    //F=ma
    //v = v0 + a*deltaT
    //Fgrav = Gm1m2/r^2
	public void thrust(boolean thrusterOn)
	{
		double thetaRadians = Math.toRadians(theta);
        double gravityObjectForceMagnitude;

        Vector totalForce = new Vector();
        Vector gravityObjectForce = new Vector();
        Vector thrusterForce = new Vector();


        if(gravityObject == null)
        {
            for(SpaceObject s : SpaceObject.allSpaceObjects)
            {
                if(s instanceof GravitationalObject) {
                    gravityObject = (GravitationalObject)s;
                }
            }
        }

        Vector rVector = new Vector(this.getPosition()).difference(
                gravityObject.getPosition());
        Vector rHat = rVector.unitVector();

        
        gravityObjectForceMagnitude = (-1 * Constants.GRAVITATIONAL_CONSTANT * getMass() * gravityObject.getMass()) / Math.pow(rHat.getMagnitude(), 2);

        gravityObjectForce = rHat.scalarProduct(gravityObjectForceMagnitude);

        if(thrusterOn)
        {
            double forceX = Constants.THRUSTER_FORCE * Math.cos(thetaRadians);
            double forceY = Constants.THRUSTER_FORCE * Math.sin(thetaRadians);

            thrusterForce = new Vector(forceX, forceY);
        }

        totalForce.add(gravityObjectForce);
        totalForce.add(thrusterForce);

        Vector acceleration = totalForce.scalarProduct(1.0 / mass);
		velocity.add(acceleration.scalarProduct(Constants.DELTA_T));
	}

	public void drawThruster() {
		drawThruster = true;
	}

	public void shoot()
	{
		double thetaRadians = Math.toRadians(theta);

		Vector misslePosition = new Vector(2 * 15, 0);
		misslePosition.rotate(theta);
		misslePosition.add(position);

		Vector missleVelocity = new Vector(Constants.MISSLE_LAUNCH_VELOCITY * Math.cos(thetaRadians),
										   Constants.MISSLE_LAUNCH_VELOCITY * Math.sin(thetaRadians));
		missleVelocity.add(velocity);

		allSpaceObjects.add(new UserMissle(missleVelocity, misslePosition));
	}

    public void resetPosition() {
        setPosition(new Vector(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT - 50));
    }
}
