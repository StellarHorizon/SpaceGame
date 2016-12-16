/***************************
 * Purpose: GameFunction class containing common
 * static functions used throughout the program.
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
 
public final class GameFunction
{
	private GameFunction(){}
	
	public static void drawRect(Graphics g, Rectangle rect)
	{
		g.drawRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
	}
	
	public static File loadFile(String filename) throws IOException, URISyntaxException
	{
		URL url = GameFunction.class.getResource(filename);
		
		File file = new File(url.toURI());
		
		return file;
	}
}
