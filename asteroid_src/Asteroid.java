import org.lwjgl.opengl.*;

public class Asteroid extends SpaceObject
{
	private int size;

	public Asteroid(Vector initialVelocity, Vector initialPosition, int size)
	{
		super(0, 0, initialVelocity, initialPosition);

		this.size = size;
		switch(size) {
			case 0:
				mass = Constants.SMALL_ASTEROID_MASS;
				radius = Constants.SMALL_ASTEROID_RADIUS;
				break;

			case 1:
				mass = Constants.MEDIUM_ASTEROID_MASS;
				radius = Constants.MEDIUM_ASTEROID_RADIUS;
				break;

			case 2:
				mass = Constants.LARGE_ASTEROID_MASS;
				radius = Constants.LARGE_ASTEROID_RADIUS;
				break;

			default:
				System.out.println("Invalid Asteroid size");
				System.exit(0);
				break;
		}

		radius *= Constants.SCALING_FACTOR;
	}

	public void draw() {
		Constants.drawCircle(position, radius);
	}

	public void delete()
	{
		super.delete();

		if(this.size > 0) {

			Asteroid childOne = new Asteroid(velocity.scalarProduct(2), position.copy(), this.size - 1);
			Asteroid childTwo = new Asteroid(velocity.scalarProduct(2), position.copy(), this.size - 1);

			childOne.velocity.rotate(45);
			childTwo.velocity.rotate(-45);

			SpaceObject.objectsToAdd.add(childOne);
			SpaceObject.objectsToAdd.add(childTwo);
		}
	}
}
