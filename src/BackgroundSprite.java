/***************************
 * Purpose: BackgroundSprite drawn behind
 * all regular sprites on a GameMap, does
 * not interact with anything.
 *
 * Contributors:
 * - Zachary Johnson
 ***************************/

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class BackgroundSprite extends Sprite
{
	String imgPath;
	
	public BackgroundSprite(Vector2D pos, String imagePath)
	{
		super(pos);
		this.imgPath = imagePath;
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		//synchronized(this.imageLock)
		//{

			this.currentImage = new BufferedImage((int)ViewCamera.renderRes.x - 1, (int)ViewCamera.renderRes.y - 1, 
					BufferedImage.TYPE_INT_ARGB);
			
			BufferedImage sourceImage = ResourceLoader.getBufferedImage(imgPath);			
			Graphics2D c2 = this.currentImage.createGraphics();
			
			//Draw source image scaled to size of background sprite			
			c2.drawImage(sourceImage, 0, 0, this.currentImage.getWidth(), this.currentImage.getHeight(), null);


			//draw parallax layer (closer to ship, uses ViewCamera position)
			int xOffset = (int)(-ViewCamera.pos.x*0.04), yOffset = (int)(-ViewCamera.pos.y*0.04);
			c2.drawImage(sourceImage, xOffset, yOffset, (int)(xOffset+(this.currentImage.getWidth()*3.0)), (int)(yOffset+(this.currentImage.getHeight()*3.0)), null);
			
			//draw another parallax layer
			xOffset *= 2; yOffset *= 2;
			c2.drawImage(sourceImage, xOffset, yOffset, (int)(xOffset+(this.currentImage.getWidth()*3.5)), (int)(yOffset+(this.currentImage.getHeight()*3.5)), null);
			
			this.needsRedraw = false;
			
			super.drawStatic(g2);
		//}
	}
}
