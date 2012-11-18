import org.lwjgl.opengl.*;

import java.util.ArrayList;

public final class Constants
{
	public static final double MISSLE_MASS = 1.0;
	public static final double CRAFT_MASS = 100.0;

	public static final double SMALL_ASTEROID_RADIUS = 20;
	public static final double MEDIUM_ASTEROID_RADIUS = 30;
	public static final double LARGE_ASTEROID_RADIUS = 50;

	public static final double SMALL_ASTEROID_MASS = 100.0;
	public static final double MEDIUM_ASTEROID_MASS = 200.0;
	public static final double LARGE_ASTEROID_MASS = 300.0;

	public static final double MISSLE_RADIUS = 4;

	public static final double THRUSTER_FORCE = 500.0;
	public static final double MISSLE_LAUNCH_VELOCITY = 120.0;

    //can't be final
	public static double DELTA = 15;

	public static double SCALING_FACTOR;

	public static double DELTA_T;
	public static Vector MAX_POSITION;

	public static final double ROTATION_SPEED = 3.0;    

	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;

    public static final int CONTINUE_GAME = 0;
    public static final int CONTINUE_GAME_WITH_NEW_OPTIONS = 1;
    public static final int SAVE_GAME = 2;
    public static final int QUIT_GAME = 3;

	public static final void drawCircle(Vector center, double radius)
	{
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);

		GL11.glVertex2d(center.x, center.y);

		for(double angle = 0; angle <= 2 * Math.PI; angle += Math.PI / 36) {
			GL11.glVertex2d(center.x + Math.sin(angle) * radius, center.y + Math.cos(angle) * radius);
		}

		GL11.glEnd();
	}
}
