import java.io.*;
import org.lwjgl.opengl.*;

public class Asteroid extends SpaceObject implements Serializable
{
    private int size;
    private double theta;

    public Asteroid(int asteroidSpeed)
    {
        this(null, null, Constants.LARGE_ASTEROID);

        //To make the game differnet every time, the asteroids
        //start out with random velocities and positions
        setVelocity(Constants.RANDOM_VELOCITY_WITH_MAGNITUDE(asteroidSpeed));
        setPosition(Constants.RANDOM_POSITION());

    }

    public Asteroid(Vector initialVelocity, Vector initialPosition, int size)
    {
        super(0, 0, initialVelocity, initialPosition);

        this.size = size;
        switch(size)
        {
            case Constants.SMALL_ASTEROID:
                setMass(Constants.SMALL_ASTEROID_MASS);
                setRadius(Constants.SMALL_ASTEROID_RADIUS);
                break;

            case Constants.MEDIUM_ASTEROID:
                setMass(Constants.MEDIUM_ASTEROID_MASS);
                setRadius(Constants.MEDIUM_ASTEROID_RADIUS);
                break;

            case Constants.LARGE_ASTEROID:
                setMass(Constants.LARGE_ASTEROID_MASS);
                setRadius(Constants.LARGE_ASTEROID_RADIUS);
                break;

                //Should never happen
            default:
                setMass(Constants.SMALL_ASTEROID_MASS);
                setRadius(Constants.SMALL_ASTEROID_RADIUS);
                break;
        }

        //Give each asteroid a random starting rotation
        theta = Constants.RANDOM_RANGE(0, 360);
    }

    public void draw()
    {
        super.draw();

        //The points that make up the Asteroid for drawing.
        //Can't put these in Constants, because they get altered below.
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
            v.add(getPosition());
        }

        //Playing connect the dots...
        for(int i = 0; i < asteroidPoints.length - 1; i++) {
            new Line(asteroidPoints[i], asteroidPoints[i + 1]).draw();
        }

        //Drawing a line from the last point to the first point
        new Line(asteroidPoints[asteroidPoints.length - 1], asteroidPoints[0]).draw();
    }

    public void delete()
    {
        super.delete();

        double newMass = 0;

        if(getMass() == Constants.LARGE_ASTEROID_MASS) {
            newMass = Constants.MEDIUM_ASTEROID_MASS;
        }
        else if(getMass() == Constants.MEDIUM_ASTEROID_MASS) {
            newMass = Constants.SMALL_ASTEROID_MASS;
        }

        //We delete this asteroid, but spawn 3 smaller asteroids.
        //One of which is heading in the same direction as this one,
        //the other 2 are heading 45 degrees and -45 degrees offset
        //from the current direction.
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
                //These equations conserve momentum. I figured them out one day,
                //with a pencil, and a lot of paper. Do not try to understand them.
                childVelocities[i].setX((getMass() * getVelocity().getMagnitude()) / 
                        (3 * newMass * Math.sqrt(1 + Math.pow(childSlopes[i], 2))));

                childVelocities[i].setY(childVelocities[i].getX() * childSlopes[i]);

                if(childThetas[i] % 360 >= 0 && childThetas[i] % 360 < 90) 
                {
                    childVelocities[i].setX(-1 * Math.abs(childVelocities[i].getX()));
                    childVelocities[i].setY(-1 * Math.abs(childVelocities[i].getY()));
                }
                else if(childThetas[i] % 360 >= 90 && childThetas[i] % 360 < 180) 
                {
                    childVelocities[i].setX(Math.abs(childVelocities[i].getX()));
                    childVelocities[i].setY(-1 * Math.abs(childVelocities[i].getY()));
                }
                else if(childThetas[i] % 360 >= 180 && childThetas[i] % 360 < 270) 
                {
                    childVelocities[i].setX(Math.abs(childVelocities[i].getX()));
                    childVelocities[i].setY(Math.abs(childVelocities[i].getY()));
                }
                else if(childThetas[i] % 360 >= 270 && childThetas[i] % 360 < 360)
                {
                    childVelocities[i].setX(-1 * Math.abs(childVelocities[i].getX()));
                    childVelocities[i].setY(Math.abs(childVelocities[i].getY()));
                }

                SpaceObject.objectsToAdd.add(
                        new Asteroid(childVelocities[i], getPosition().copy(), size - 1));
            }
        }
    }

    public String toString() {
        return getVelocity().toString();
    }
}
