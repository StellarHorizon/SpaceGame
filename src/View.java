/***************************
 * Purpose: View component of Model-View-Controller
 * paradigm, starting point for drawing to the screen
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;


@SuppressWarnings("serial")
class View extends JPanel
{
	Model model;
	BufferedImage Frame; //This implementation is drawing synchronized (must always display entire frame at a time)
	
	//Tracking variable to allow only one instance of painting next frame at a time
	private static boolean drawingFrame = false;
	
	View(Model m) throws IOException
	{
		this.model = m;
		
		this.Frame = new BufferedImage((int)ViewCamera.windowDim.x - 1, (int)ViewCamera.windowDim.y - 1, BufferedImage.TYPE_INT_ARGB);
	}
	
	/***************************
	 * Primary painting function
	 ***************************/
	public void paintComponent(Graphics g)
	{
		if (!View.drawingFrame)
		{
			View.drawingFrame = true;
			
			drawNextFrame();
			
			g.setColor(Color.darkGray);
			g.fillRect(0, 0, (int)ViewCamera.windowDim.x, (int)ViewCamera.windowDim.y);
			g.drawImage(this.Frame, (int)ViewCamera.scalingOffset.x, (int)ViewCamera.scalingOffset.y, null);
			
			View.drawingFrame = false;
		}
		
	}
	public void drawNextFrame()
	{
		double currentRenderScale = ViewCamera.renderScale;

		this.Frame = new BufferedImage((int)ViewCamera.windowDim.x - 1, (int)ViewCamera.windowDim.x - 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) Frame.getGraphics();
		
		g2.scale(currentRenderScale, currentRenderScale);
		
		g2.clipRect(0, 0, (int)ViewCamera.renderRes.x, (int)ViewCamera.renderRes.y);
		
		g2.setColor(Color.BLACK);
		g2.fillRect(0,  0,  (int)ViewCamera.renderRes.x, (int)ViewCamera.renderRes.y);
		
		synchronized(SpriteList.SpriteLock)
		{
			//ArrayList<Sprite> spriteList = SpriteList.getList();
			Sprite s;
			for (int i = 0; i < SpriteList.size(); i++)
			{
				s = SpriteList.get(i);
				s.draw(g2);
			}
		}
		
		g2.scale(1/currentRenderScale, 1/currentRenderScale);
	}
}
