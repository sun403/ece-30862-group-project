import org.lwjgl.opengl.*;

import java.util.ArrayList;

public final class Constants
{
    //Draws a white circle around a space object to denote
    //what gets used in determining impacts with other space objects
    public static final boolean DRAW_DEBUG_CIRCLE = true;

	public static final double MISSLE_MASS = 1.0;
	public static final double CRAFT_MASS = 100.0;

    public static final int SMALL_ASTEROID = 0;
    public static final int MEDIUM_ASTEROID = 1;
    public static final int LARGE_ASTEROID = 2;

    //unitless maximum asteroid radius
    private static final double ASTEROID_RADIUS = 8.94427191;
    //let's ~15% of the asteroid be hittable without dying
    private static final double ERROR_RANGE = 0.15;

	public static final double SMALL_ASTEROID_SCALE = 1.5;
	public static final double MEDIUM_ASTEROID_SCALE = 4;
	public static final double LARGE_ASTEROID_SCALE = 8;

	public static final double SMALL_ASTEROID_RADIUS = SMALL_ASTEROID_SCALE * ASTEROID_RADIUS * (1 - ERROR_RANGE);
	public static final double MEDIUM_ASTEROID_RADIUS = MEDIUM_ASTEROID_SCALE * ASTEROID_RADIUS * (1 - ERROR_RANGE);
	public static final double LARGE_ASTEROID_RADIUS = LARGE_ASTEROID_SCALE * ASTEROID_RADIUS * (1 - ERROR_RANGE);

	public static final double SMALL_ASTEROID_MASS = 100.0;
	public static final double MEDIUM_ASTEROID_MASS = 200.0;
	public static final double LARGE_ASTEROID_MASS = 300.0;

	public static final double MISSLE_RADIUS = 2;

	public static final double THRUSTER_FORCE = 500.0;
	public static final double MISSLE_LAUNCH_VELOCITY = 120.0;

    //can't be final
	public static double DELTA = 15;

	public static final double DELTA_T = 0.1;
	public static Vector MAX_POSITION;

	public static final double ROTATION_SPEED = 3.5;    
    public static final double SCALING_FACTOR = 1.0;

	public static int WINDOW_WIDTH;
	public static int WINDOW_HEIGHT;

    public static final int CONTINUE_GAME = 0;
    public static final int CONTINUE_GAME_WITH_NEW_OPTIONS = 1;
    public static final int SAVE_GAME = 2;
    public static final int QUIT_GAME = 3;

    public static final int FULL_CIRCLE = GL11.GL_TRIANGLE_FAN;
    public static final int EMPTY_CIRCLE = GL11.GL_LINE_LOOP;

    //Retuns a double in [x, y)
    public static final double RANDOM_RANGE(double x, double y) {
        return (Math.random() * (y - x)) + x;
    }

	public static final void drawCircle(Vector center, double radius, int mode)
	{
        int numberOfCircleIncrements = 72;
        double twoPI = 2 * Math.PI;
        
		//GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glBegin(mode);
		GL11.glVertex2d(center.x, center.y);

        //Draws a circle by drawing a bunch of rectthetas. 
        //TODO is this wrong? x + sin?
		for(double theta = 0; theta <= twoPI; theta += twoPI / numberOfCircleIncrements) {
			GL11.glVertex2d(center.x + (Math.sin(theta) * radius), center.y + (Math.cos(theta) * radius));
		}

		GL11.glEnd();
	}
}
