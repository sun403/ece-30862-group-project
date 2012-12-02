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

        //The points that make up the UserCraft for drawing.
        //Can't put these in Constants, because they get altered below.
        //
        //I remember picking these magic numbers long ago, and have
        //forgotten how I came about them.
		Vector v1 = new Vector(30, 0);
		Vector v2 = new Vector(-15, -1.25 * 15);
		Vector v3 = new Vector(-15, 1.25 * 15);
		Vector v4 = new Vector(0, .83 * 15);
		Vector v5 = new Vector(0, -.83 * 15);

		v1.rotate(getTheta());
		v2.rotate(getTheta());
		v3.rotate(getTheta());
		v4.rotate(getTheta());
		v5.rotate(getTheta());

		v1.add(getPosition());
		v2.add(getPosition());
		v3.add(getPosition());
		v4.add(getPosition());
		v5.add(getPosition());

		Line lineOne = new Line(v3, v1);
		Line lineTwo = new Line(v2, v1);
		Line lineThree = new Line(v4, v5);

		lineOne.draw();
		lineTwo.draw();
		lineThree.draw();

		if(drawThruster)
        {
			Vector v6 = new Vector(-15, 0);
			Vector v7 = new Vector(0, .42 * 15);
			Vector v8 = new Vector(0, -.42 * 15);

			v6.rotate(getTheta());
			v7.rotate(getTheta());
			v8.rotate(getTheta());

			v6.add(getPosition());
			v7.add(getPosition());
			v8.add(getPosition());

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
		double thetaRadians = Math.toRadians(getTheta());
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

        Vector rVector = new Vector(this.getPosition()).difference(gravityObject.getPosition());
        Vector rHat = rVector.unitVector();

        
        //Doing some fancy physics, just nod your head and move on...
        gravityObjectForceMagnitude = (
                -1 * Constants.GRAVITATIONAL_CONSTANT * getMass() * gravityObject.getMass()) / 
                Math.pow(rHat.getMagnitude(), 2);
        
        gravityObjectForce = rHat.scalarProduct(gravityObjectForceMagnitude);

        if(thrusterOn)
        {
            double forceX = Constants.THRUSTER_FORCE * Math.cos(thetaRadians);
            double forceY = Constants.THRUSTER_FORCE * Math.sin(thetaRadians);

            thrusterForce = new Vector(forceX, forceY);
            totalForce.add(thrusterForce);
        }

        totalForce.add(gravityObjectForce);

        Vector acceleration = totalForce.scalarProduct(1.0 / getMass());
		getVelocity().add(acceleration.scalarProduct(Constants.DELTA_T));
	}

	public void drawThruster() {
		drawThruster = true;
	}

	public void shoot()
	{
		double thetaRadians = Math.toRadians(getTheta());

        //Starting the missle 30 "units" away from the UserCraft
        //so it doesn't seem like it's shooting out from the middle,
        //and instead coming out the tip of the craft.
		Vector misslePosition = new Vector(30, 0);
		misslePosition.rotate(getTheta());
		misslePosition.add(getPosition());

		Vector missleVelocity = new Vector(Constants.MISSLE_LAUNCH_VELOCITY * Math.cos(thetaRadians),
										   Constants.MISSLE_LAUNCH_VELOCITY * Math.sin(thetaRadians));
		missleVelocity.add(getVelocity());

		allSpaceObjects.add(new UserMissle(missleVelocity, misslePosition));
	}

    //Resets to the middle of the screen, and a little bit off of the bottom.
    public void resetPosition() {
        setPosition(new Vector(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT - 50));
    }
}
