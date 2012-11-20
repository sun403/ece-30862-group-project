import org.lwjgl.opengl.*;

public class Asteroid extends SpaceObject
{
	private int size;
    private double theta;

	public Asteroid(Vector initialVelocity, Vector initialPosition, int size)
	{
		super(0, 0, initialVelocity, initialPosition);

		this.size = size;
		switch(size)
        {
			case Constants.SMALL_ASTEROID:
				mass = Constants.SMALL_ASTEROID_MASS;
				radius = Constants.SMALL_ASTEROID_RADIUS;
				break;

			case Constants.MEDIUM_ASTEROID:
				mass = Constants.MEDIUM_ASTEROID_MASS;
				radius = Constants.MEDIUM_ASTEROID_RADIUS;
				break;

			case Constants.LARGE_ASTEROID:
				mass = Constants.LARGE_ASTEROID_MASS;
				radius = Constants.LARGE_ASTEROID_RADIUS;
				break;

			default:
				System.out.println("Invalid Asteroid size");
				System.exit(0);
				break;
		}

		radius *= Constants.SCALING_FACTOR;

        //Give each asteroid a random rotation
        theta = Constants.RANDOM_RANGE(0, 360);
	}

	public void draw()
    {
        Vector x0 = new Vector(0, 6);
        Vector x1 = new Vector(4, 8);
        Vector x2 = new Vector(7, 4);
        Vector x3 = new Vector(3, 3);
        Vector x4 = new Vector(8, -1);
        Vector x5 = new Vector(3, -7);
        Vector x6 = new Vector(-2, -5);
        Vector x7 = new Vector(-4, -7);
        Vector x8 = new Vector(-7, -3);
        Vector x9 = new Vector(-5, 1);
        Vector x10 = new Vector(-7, 4);
        Vector x11 = new Vector(-3, 8);

        Vector[] asteroidPoints = {x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11};

        for(Vector v : asteroidPoints)
        {
            switch(size)
            {
                case Constants.SMALL_ASTEROID:
                    v.scalarMultiply(Constants.SMALL_ASTEROID_SCALE);
                    break;

                case Constants.MEDIUM_ASTEROID:
                    v.scalarMultiply(Constants.MEDIUM_ASTEROID_SCALE);
                    break;

                case Constants.LARGE_ASTEROID:
                    v.scalarMultiply(Constants.LARGE_ASTEROID_SCALE);
                    break;

                default:
                    break;
            }

            v.scalarMultiply(size);
            v.flipY();
            v.rotate(theta);
            v.add(position);

        }

        for(int i = 0; i < asteroidPoints.length - 1; i++) {
            new Line(asteroidPoints[i], asteroidPoints[i + 1]).draw();
        }

        new Line(asteroidPoints[asteroidPoints.length - 1], asteroidPoints[0]).draw();
	}

	public void delete()
	{
		super.delete();

		if(this.size > 0)
        {
			Asteroid childOne = new Asteroid(velocity.scalarProduct(2), position.copy(), this.size - 1);
			Asteroid childTwo = new Asteroid(velocity.scalarProduct(2), position.copy(), this.size - 1);

			childOne.velocity.rotate(45);
			childTwo.velocity.rotate(-45);

			SpaceObject.objectsToAdd.add(childOne);
			SpaceObject.objectsToAdd.add(childTwo);
		}
	}
}
