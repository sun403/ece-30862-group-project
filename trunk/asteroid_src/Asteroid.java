import org.lwjgl.opengl.*;

public class Asteroid extends SpaceObject
{
	private int size;
    private double theta;

    public Asteroid(int asteroidSpeed)
    {
        this(null, null, Constants.LARGE_ASTEROID);

        velocity = randomVelocityWithMagnitude(asteroidSpeed);
        position = randomPosition();
    }

    private Vector randomPosition()
    {
        return new Vector(
                Constants.WINDOW_WIDTH * Math.random(),
                Constants.WINDOW_HEIGHT * Math.random());
    }

    private Vector randomVelocityWithMagnitude(int magnitude)
    {
        double randomTheta = Constants.RANDOM_RANGE(0, 2 * Math.PI);
        return new Vector(
                magnitude * Math.cos(randomTheta), 
                magnitude * Math.sin(randomTheta));
    }

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

        //Give each asteroid a random starting rotation
        theta = Constants.RANDOM_RANGE(0, 360);
	}

	public void draw()
    {
        super.draw();

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

        double newMass = 0;

        if(mass == Constants.LARGE_ASTEROID_MASS) {
            newMass = Constants.MEDIUM_ASTEROID_MASS;
        }
        else if(mass == Constants.MEDIUM_ASTEROID_MASS) {
            newMass = Constants.SMALL_ASTEROID_MASS;
        }

		if(this.size > 0)
        {
            Vector totalMomentum = getVelocity().scalarProduct(getMass());

            double originalTheta = totalMomentum.angle();

            Vector[] childVelocities = { new Vector(), new Vector(), new Vector() };
            double[] childSlopes = new double[3];
            double[] childThetas = { originalTheta + 45, 
                                     originalTheta, 
                                     originalTheta - 45 };


            for(int i = 0; i < childThetas.length; i++) {
                childSlopes[i] = Math.tan(Math.toRadians(childThetas[i]));
            }

            for(int i = 0; i < childVelocities.length; i++)
            {
                childVelocities[i].x = (getMass() * velocity.getMagnitude())/(3 * newMass * Math.sqrt(1 + Math.pow(childSlopes[i], 2)));

                childVelocities[i].y = childVelocities[i].x * childSlopes[i];

                if(childThetas[i] % 360 >= 0 && childThetas[i] % 360 < 90) 
                {
                    childVelocities[i].x = -1 * Math.abs(childVelocities[i].x);
                    childVelocities[i].y = -1 * Math.abs(childVelocities[i].y);
                }
                else if(childThetas[i] % 360 >= 90 && childThetas[i] % 360 < 180) 
                {
                    childVelocities[i].x = Math.abs(childVelocities[i].x);
                    childVelocities[i].y = -1 * Math.abs(childVelocities[i].y);
                }
                else if(childThetas[i] % 360 >= 180 && childThetas[i] % 360 < 270) 
                {
                    childVelocities[i].x = Math.abs(childVelocities[i].x);
                    childVelocities[i].y = Math.abs(childVelocities[i].y);
                }
                else if(childThetas[i] % 360 >= 270 && childThetas[i] % 360 < 360)
                {
                    childVelocities[i].x = -1 * Math.abs(childVelocities[i].x);
                    childVelocities[i].y = Math.abs(childVelocities[i].y);
                }

                SpaceObject.objectsToAdd.add(
                        new Asteroid(childVelocities[i], position.copy(), size - 1));
            }
		}
	}

    public String toString() 
    {
        return velocity.toString();
    }
}
