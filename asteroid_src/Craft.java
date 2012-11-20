import org.lwjgl.opengl.*;

public class Craft extends SpaceObject
{
	//There will only ever be 1 Craft.
	private static Craft instance = null;

	//Controls how the craft is rotated.
	private double theta = 90;

	//When accelerating this is true
	private boolean drawThruster = false;

	private int lives = 3;

	private Craft() {
		super(Constants.CRAFT_MASS, 1.25 * Constants.DELTA, new Vector(0.0, 0.0), new Vector(0.0, 0.0));
	}

	public static Craft getInstance() {
		if(instance == null) {
			instance = new Craft();
		}

		return instance;
	}

	public void draw()
	{
		Vector v1 = new Vector(2 * Constants.DELTA, 0);
		Vector v2 = new Vector(-Constants.DELTA, -1.25 * Constants.DELTA);
		Vector v3 = new Vector(-Constants.DELTA, 1.25 * Constants.DELTA);
		Vector v4 = new Vector(0, .83 * Constants.DELTA);
		Vector v5 = new Vector(0, -.83 * Constants.DELTA);

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
			Vector v6 = new Vector(-Constants.DELTA, 0);
			Vector v7 = new Vector(0, .42 * Constants.DELTA);
			Vector v8 = new Vector(0, -.42 * Constants.DELTA);

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

	public void changeTheta(double DELTA_Theta) {
		theta += DELTA_Theta;
	}

	public void thrust()
	{
		double thetaRadians = Math.toRadians(theta);

		Vector force = new Vector(Constants.THRUSTER_FORCE * Math.cos(thetaRadians),
								  Constants.THRUSTER_FORCE * Math.sin(thetaRadians)).scalarProduct(Constants.DELTA_T / mass);
		velocity.add(force);
	}

	public void drawThruster() {
		drawThruster = true;
	}

	public void shoot()
	{
		double thetaRadians = Math.toRadians(theta);

		Vector misslePosition = new Vector(2 * Constants.DELTA, 0);
		misslePosition.rotate(theta);
		misslePosition.add(position);

		Vector missleVelocity = new Vector(Constants.MISSLE_LAUNCH_VELOCITY * Math.cos(thetaRadians),
										   Constants.MISSLE_LAUNCH_VELOCITY * Math.sin(thetaRadians));
		missleVelocity.add(velocity);

		allSpaceObjects.add(new Missle(missleVelocity, misslePosition));
	}

	public int getLives() {
		return lives;
	}

	public void subtractLife() {
		lives--;
	}
}
