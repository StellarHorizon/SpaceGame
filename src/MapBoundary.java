/***************************
 * Purpose: MapBoundary object representing a
 * rectangular area on the GameMap used to contain
 * sprites within the given area. PhysicsSprites
 * in the MapBoundary's internal list of sprites
 * are affected by the MapBoundary in the following
 * ways:
 * 	1) 	Sprites inside the MapBoundary experience
 * 		no additional forces
 *  2)	Sprites outside the MapBoundary experience
 *  	a force linearly proportional to the distance
 *  	the sprite is from the MapBoundary, and in
 *  	the direction of the MapBoundary.
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MapBoundary extends Sprite
{
	private Rectangle mapBounds;
	public Color boundaryColor;
	public int boundaryWidth;
	private double repulsiveForce;
	public ArrayList<PhysicsSprite> sprites;
	
	public MapBoundary(Rectangle size)
	{
		this.sprites = new ArrayList<PhysicsSprite>();
		this.mapBounds = new Rectangle(size);
		this.boundaryColor = Color.RED;
		this.boundaryWidth = 0;
		this.repulsiveForce = 0.1;
	}
	
	public MapBoundary(Rectangle size, ArrayList<PhysicsSprite> spriteList)
	{
		this.sprites = spriteList;
		this.mapBounds = new Rectangle(size);
		this.boundaryColor = Color.RED;
		this.boundaryWidth = 10;
		this.repulsiveForce = 0.1;
	}
	
	/*
	 * Add the given PhysicsSprite to the list of sprites
	 * affected by the MapBoundary.
	 */
	public void addSprite(PhysicsSprite s)
	{
		synchronized (this.sprites)
		{
			this.sprites.add(s);
		}
	}
	
	/*
	 * Remove the given PhysicsSprite from the list of
	 * sprites affected by the MapBoundary
	 */
	public boolean removeSprite(PhysicsSprite s)
	{
		synchronized (this.sprites)
		{
			return this.sprites.remove(s);
		}
	}
	
	/*
	 * Change the size of the MapBoundary (Thread-safe)
	 */
	public void setSize(Rectangle size)
	{
		synchronized (this.mapBounds)
		{
			this.mapBounds = size;
		}
		
		this.needsRedraw = true;
	}
	
	public int getUpperBound()
	{
		synchronized (this.mapBounds)
		{
			return this.mapBounds.y;
		}
	}
	public int getLowerBound()
	{
		synchronized (this.mapBounds)
		{
			return this.mapBounds.y + (int) this.mapBounds.getHeight();
		}
	}
	public int getLeftBound()
	{
		synchronized (this.mapBounds)
		{
			return this.mapBounds.x;
		}
	}
	public int getRightBound()
	{
		synchronized (this.mapBounds)
		{
			return this.mapBounds.x + (int) this.mapBounds.getWidth();
		}
	}
	public int getWidth()
	{
		synchronized(this.mapBounds)
		{
			return this.mapBounds.width;
		}
	}
	public int getHeight()
	{
		synchronized(this.mapBounds)
		{
			return this.mapBounds.height;
		}
	}
	
	/*
	 * Set the force coefficient PhysicsObjects
	 * outside the MapBoundary will experience.
	 */
	public void setForce(double force)
	{
		this.repulsiveForce = force;
	}
	/*
	 * Get the force coefficient PhysicsObjects
	 * outside the MapBoundary will experience.
	 */
	public double getForce()
	{
		return this.repulsiveForce;
	}
	
	/*
	 * Iterate through the entire list of PhysicsSprites in
	 * the master sprite list to check for collisions
	 */
	public void checkCollision()
	{
		int i = 0;
		for(PhysicsSprite pSprite : this.sprites)
		{
			synchronized (pSprite.acc)
			{
				if (pSprite.pos.x - pSprite.size < this.getLeftBound())
					pSprite.acc.x += this.repulsiveForce * Math.abs(pSprite.pos.x - pSprite.size - this.getLeftBound());
				
				else if (pSprite.pos.x+pSprite.size > this.getRightBound())
					pSprite.acc.x += -this.repulsiveForce * Math.abs(pSprite.pos.x + pSprite.size - this.getRightBound());
				
				if (pSprite.pos.y-pSprite.size < this.getUpperBound())
					pSprite.acc.y += this.repulsiveForce * Math.abs(pSprite.pos.y - pSprite.size - this.getUpperBound());
				
				else if (pSprite.pos.y+pSprite.size > this.getLowerBound())
					pSprite.acc.y += -this.repulsiveForce * Math.abs(pSprite.pos.y + pSprite.size - this.getLowerBound());
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		if (this.boundaryWidth < 1)
			return;
		
		synchronized(this.imageLock)
		{
			if (this.needsRedraw || this.currentImage == null)
			{
				this.currentImage = new BufferedImage((int)(this.mapBounds.width + (2*this.boundaryWidth)), (int)(this.mapBounds.height + (2*this.boundaryWidth)), BufferedImage.TYPE_INT_ARGB); 

				this.pos = new Vector2D(this.mapBounds.x + this.getWidth()/2, this.mapBounds.y + this.getHeight()/2);
				Graphics2D c2 = this.currentImage.createGraphics();
				
				c2.setColor(boundaryColor);
				c2.fillRect(0, 0, this.boundaryWidth, this.mapBounds.height+(2*this.boundaryWidth));
				c2.fillRect(0, 0, this.mapBounds.width+(2*this.boundaryWidth), this.boundaryWidth);
				c2.fillRect(this.mapBounds.width+this.boundaryWidth, 0, this.boundaryWidth, this.mapBounds.height+this.boundaryWidth);
				c2.fillRect(0, this.mapBounds.height+this.boundaryWidth, this.mapBounds.width+(2*this.boundaryWidth), this.boundaryWidth);
				
				c2.dispose();
				
				this.needsRedraw = false;
			}
			
			super.draw(g2);
			
		}
		
	}
	
	/*public String getInfo()
	{
		String s = "Boundary Color: " + this.boundaryColor.toString() + "\n" +
				"Coordinates: (" + this.mapBounds.x + ", " + this.mapBounds.y + ") to (" + (this.mapBounds.x + this.mapBounds.width) + ", " + (this.mapBounds.y + this.mapBounds.height) + ")" +
				"Affected Sprites: " + this.sprites.size() + "\n";
		
		return s;
	}*/
}
