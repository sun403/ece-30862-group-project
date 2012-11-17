import org.lwjgl.opengl.*;

public class Line
{
	private Vector start;
	private Vector end;

	public Line(Vector startVector, Vector endVector) {
		start = startVector;
		end = endVector;
	}

	public void draw()
	{
		GL11.glBegin(GL11.GL_LINES);

		GL11.glVertex2d(start.x, start.y);
		GL11.glVertex2d(end.x, end.y);

		GL11.glEnd();
	}
}