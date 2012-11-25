import java.util.ArrayList;

public class SpaceObject
{
	public static ArrayList<SpaceObject> allSpaceObjects;
	public static ArrayList<SpaceObject> objectsToAdd;
	public static ArrayList<SpaceObject> objectsToRemove;

	protected double mass;
	protected double radius;
	protected Vector velocity;
	protected Vector position;

	public SpaceObject() {
        this(0, 0, new Vector(), new Vector());
    }

	public SpaceObject(double newMass, double newRadius, 
                       Vector newVelocity, Vector newPosition)
	{
        setMass(newMass);
        setRadius(newRadius);
        setVelocity(newVelocity);
        setPosition(newPosition);
	}

    public void setMass(double newMass) {
        mass = newMass;
    }

    public void setRadius(double newRadius) {
        radius = newRadius;
    }

    public void setVelocity(Vector newVelocity) {
        velocity = newVelocity;
    }

    public void setPosition(Vector newPosition) {
        position = newPosition;
    }

	public Vector getPosition() {
		return position;
	}

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public Vector getVelocity() {
        return velocity;
    }

	public void updatePosition()
	{
		position.add(velocity.scalarProduct(Constants.DELTA_T));

		//Rollover from right side of screen to left
		if(position.x > Constants.MAX_POSITION.x) {
			position.x = 0;
		}

		//Rollover from left side of screen to right
		else if(position.x < 0) {
			position.x = Constants.MAX_POSITION.x;
		}

		//Rollover from top of screen to bottom
		if(position.y > Constants.MAX_POSITION.y) {
			position.y = 0;
		}

		//Rollover from bottom of screen to top
		else if(position.y < 0) {
			position.y = Constants.MAX_POSITION.y;
		}
	}

	public void delete() {
		SpaceObject.objectsToRemove.add(this);
	}

    public void draw()
    {
        if(Constants.DRAW_DEBUG_CIRCLE) {
            Constants.drawCircle(position, radius, Constants.EMPTY_CIRCLE);
        }
    }
}
