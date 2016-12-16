/***************************
 * Purpose: GameBar class for representing
 * a value graphically with the GUI. Used
 * as part of the GameGUI.
 *
 * Contributors:
 * - Zachary Johnson
 ***************************/

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class GUIBar extends Sprite
{
	private int currentValue, maxValue, length, height;
	private int frameWidth = 2;
	Color frameColor, barColor;
	
	public GUIBar(int value, int max, int length, int height, Color c)
	{
		this.needsRedraw = true;
		
		this.currentValue = value;
		this.maxValue = max;
		this.length = length;
		this.height = height;
		
		//Hardcoded
		frameColor = Color.LIGHT_GRAY;
		barColor = new Color((float)c.getRed()/(float)255, (float)c.getGreen()/(float)255, (float)c.getBlue()/(float)255, (float)0.75);
		
	}
	
	public void update()
	{
		this.needsRedraw = true;
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		synchronized (this.imageLock)
		{
			if (this.needsRedraw || this.currentImage == null)
			{
				//this.currentImage = new BufferedImage(this.length - 1, this.height - 1, BufferedImage.TYPE_INT_ARGB);
				this.currentImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
				Graphics2D c2 = this.currentImage.createGraphics();
				
				//Draw the frame
				c2.setColor(frameColor);
				c2.fillRect(0, 0, this.frameWidth, this.height+(2*this.frameWidth));
				c2.fillRect(0, 0, this.length+(2*this.frameWidth), this.frameWidth);
				c2.fillRect(this.length+this.frameWidth, 0, this.frameWidth, this.height+this.frameWidth);
				c2.fillRect(0, this.height+this.frameWidth, this.length+(2*this.frameWidth), this.frameWidth);
				
				//Draw the bar
				c2.setColor(this.barColor);
				c2.fillRect(this.frameWidth, this.frameWidth, (int)((float)this.currentValue/(float)this.maxValue*((float)length)), this.height);
				
//				this.needsRedraw = false;
				
			}
			
			super.drawStatic(g2);
		}
	}
}






















