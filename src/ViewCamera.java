/***************************
 * Purpose: ViewCamera class used for
 * determining how the game is drawn to
 * the current window.
 *
 * Contributors:
 * - Derek Paschal
 ***************************/
public class ViewCamera 
{
	public static Vector2D pos = new Vector2D();
	public static Vector2D windowDim = new Vector2D();
	public static final Vector2D renderRes = new Vector2D(900,500);
	public static double renderScale = 1.0;
	public static Vector2D scalingOffset = new Vector2D(0,0);
}
