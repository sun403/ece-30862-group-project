public class Vector
{
	public double x;
	public double y;

	//I'm forgoing accessors for (hopefully) performance increases.

	public Vector(Vector v) {
		x = v.x;
		y = v.y;
	}

	public Vector(double newX, double newY) {
		x = newX;
		y = newY;
	}

	public Vector scalarProduct(double scalar) {
		return new Vector(scalar * x, scalar * y);
	}

	public void add(Vector v) {
		x += v.x;
		y += v.y;
	}

	/* Theta = in degrees. */
	public void rotate(double theta)
	{
		double xOld = x;
		double yOld = y;

		theta = Math.toRadians(theta);

		x = (xOld * Math.cos(theta)) - (yOld * Math.sin(theta));
		y = (xOld * Math.sin(theta)) + (yOld * Math.cos(theta));
	}

	public double distanceTo(Vector v) {
		return Math.sqrt(Math.pow(x - v.x, 2) + Math.pow(y - v.y, 2));
	}

	public Vector copy() {
		return new Vector(this);
	}
}