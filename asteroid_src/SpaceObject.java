import java.util.ArrayList;

public abstract class SpaceObject
{
	public static ArrayList<SpaceObject> allSpaceObjects;
	public static ArrayList<SpaceObject> objectsToAdd;
	public static ArrayList<SpaceObject> objectsToRemove;

	protected double mass;
	protected double radius;
	protected Vector velocity;
	protected Vector position;

	//Constructors
	public SpaceObject() {}

	public SpaceObject(double newMass, double newRadius, Vector newVelocity, Vector newPosition)
	{
		mass = newMass;
		radius = newRadius;
		velocity = newVelocity;
		position = newPosition;
	}

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector newPosition) {
		position = newPosition;
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
