/***************************
 * Purpose: Sprite class to handle essential
 * attributes of most ingame sprite objects
 *
 * Contributors:
 * - Derek Paschal
 * - Zachary Johnson
 ***************************/

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

abstract class Sprite
{
	public Vector2D pos;
	public BufferedImage currentImage;
	public Object imageLock = new Object();
	public Rotation rotation;
	public boolean visible;
	public boolean needsRedraw = true;
	public boolean remove = false;
	private AffineTransform at = new AffineTransform();
	public double scale = 1.0;
	
	public Sprite(Vector2D position)
	{
		this.pos = new Vector2D(position);
		this.rotation = new Rotation(0);
		this.visible = true;
		this.remove = false;
	}
	
	public Sprite()
	{
		this(new Vector2D(0,0));
	}
	
	public void setPos(ReferencePositions refPoint, int x, int y)
	{
		setPos(refPoint, new Vector2D(x, y));
	}
	public void setPos(ReferencePositions refPoint, Vector2D newPos)
	{
		if (this.currentImage == null)
		{
			this.pos = new Vector2D(newPos);
			return;
		}
		
		switch (refPoint)
		{
			case TOP_LEFT:
				this.pos = new Vector2D(newPos);
				break;
			case TOP_CENTER:
				this.pos = new Vector2D((int) (newPos.x - this.currentImage.getWidth() / 2), (int) newPos.y);
				break;
			case TOP_RIGHT:
				this.pos = new Vector2D((int) (newPos.x - this.currentImage.getWidth()), (int) newPos.y);
				break;
			case CENTER_LEFT:
				this.pos = new Vector2D((int) newPos.x, (int) (newPos.y - this.currentImage.getHeight() / 2));
				break;
			case CENTER:
				this.pos = new Vector2D((int) (newPos.x - this.currentImage.getWidth() / 2), (int) (newPos.y - this.currentImage.getHeight() / 2));
				break;
			case CENTER_RIGHT:
				this.pos = new Vector2D((int) (newPos.x - this.currentImage.getWidth()), (int) (newPos.y - this.currentImage.getHeight() / 2));
				break;
			case BOTTOM_LEFT:
				this.pos = new Vector2D((int) newPos.x, (int) (newPos.y - this.currentImage.getHeight()));
				break;
			case BOTTOM_CENTER:
				this.pos = new Vector2D((int) (newPos.x - this.currentImage.getWidth() / 2), (int) (newPos.y - this.currentImage.getHeight()));
				break;
			case BOTTOM_RIGHT:
				this.pos = new Vector2D((int) (newPos.x - this.currentImage.getWidth()), (int) (newPos.y - this.currentImage.getHeight()));
				break;
		}
		
		this.needsRedraw = true;
	}
	public Vector2D getPos()
	{
		return this.pos;
	}
	public Vector2D getPos(ReferencePositions refPoint)
	{
		switch (refPoint)
		{
			case TOP_LEFT:
				return this.pos;
			case TOP_CENTER:
				return new Vector2D((int) (this.pos.x + this.currentImage.getWidth() / 2), (int) this.pos.y);
			case TOP_RIGHT:
				return new Vector2D((int) (this.pos.x + this.currentImage.getWidth()), (int) this.pos.y);
			case CENTER_LEFT:
				return new Vector2D((int) this.pos.x, (int) (this.pos.y + this.currentImage.getHeight() / 2));
			case CENTER:
				return new Vector2D((int) (this.pos.x + this.currentImage.getWidth() / 2), (int) (this.pos.y + this.currentImage.getHeight() / 2));
			case CENTER_RIGHT:
				return new Vector2D((int) (this.pos.x + this.currentImage.getWidth()), (int) (this.pos.y + this.currentImage.getHeight() / 2));
			case BOTTOM_LEFT:
				return new Vector2D((int) this.pos.x, (int) (this.pos.y + this.currentImage.getHeight()));
			case BOTTOM_CENTER:
				return new Vector2D((int) (this.pos.x + this.currentImage.getWidth() / 2), (int) (this.pos.y + this.currentImage.getHeight()));
			case BOTTOM_RIGHT:
				return new Vector2D((int) (this.pos.x + this.currentImage.getWidth()), (int) (this.pos.y + this.currentImage.getHeight()));
			default:
				return this.pos;
		}
	}
	
	//Draw the sprite with the position relative to the camera object
	public void draw(Graphics2D g2)
	{
		if (!this.visible)
			return;
		
		if (this.currentImage == null)
			return;
		
		this.at.setToIdentity();
		
		double transX = this.pos.x - ((this.currentImage.getWidth()*this.scale)/2) - ViewCamera.pos.x + (ViewCamera.renderRes.x*0.5);
		double transY = this.pos.y - ((this.currentImage.getHeight()*this.scale)/2) - ViewCamera.pos.y + (ViewCamera.renderRes.y*0.5);
		
		if (!(transX <= ViewCamera.renderRes.x && (transX+(currentImage.getWidth()*this.scale))  >= 0 && 
				transY <= ViewCamera.renderRes.y && (transY+(currentImage.getHeight()*this.scale)) >= 0))
		{
			return;
		}
		
		at.translate(transX, transY);
		
		double rotateX = (currentImage.getWidth()*this.scale) / 2;
		double rotateY = (currentImage.getHeight()*this.scale) / 2;
		at.rotate(this.rotation.getRadians(), rotateX, rotateY);
		
		this.at.scale(scale, scale);
		
		g2.drawImage(currentImage, at, null);
	}
	
	//Draw the sprite at a static location on the screen 
	//The position of the camera does not affect where the sprite appears on the screen
	//Primarily used for GUI elements
	public void drawStatic(Graphics2D g2, AffineTransform at)
	{
		if (!this.visible)
			return;
		
		if (this.currentImage == null)
			return;
		if (at == null)
		{
			at = new AffineTransform();
			double transX = this.pos.x;
			double transY = this.pos.y;
			
			if (!(transX <= ViewCamera.renderRes.x && transX+currentImage.getWidth()  >= 0 && 
					transY <= ViewCamera.renderRes.y && transY+currentImage.getHeight() >= 0))
			{
				return;
			}
			
			at.translate(transX, transY);
			
			double rotateX = currentImage.getWidth() / 2;
			double rotateY = currentImage.getHeight() / 2;
			at.rotate(this.rotation.getRadians(), rotateX, rotateY);
		}
		
		g2.drawImage(currentImage, at, null);
	}
	
	//Draw static sprite with translation
		public void drawStatic(Graphics2D g2, double x, double y)
		{
			AffineTransform at = new AffineTransform();
			at.translate(x, y);
			drawStatic(g2, at);
		}
	
	//Draw static sprite with no transformation
	public void drawStatic(Graphics2D g2)
	{
		drawStatic(g2, null);
	}
}
