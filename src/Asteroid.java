/***************************
 * Purpose: Asteroid class containing the
 * functionality for an asteroid object on the
 * GameMap with physics interactions.
 *
 * Contributors:
 * - Derek Paschal
 * - Zachary Johnson
 ***************************/

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Asteroid extends PhysicsSprite
{
	public Color color;
	int type;
	
	public Asteroid(Vector2D position, Rotation rotation, double size)
	{
		super(position, rotation, Math.round(size),0.99, size*size);
		this.vel = new Vector2D();
		int color_value = (int)(128+(Math.random()*64));
		this.color = new Color(color_value,color_value,color_value);
		type = (int)Math.floor(Math.random()*2);
	}
	
	public void setColor(Color c)
	{
		this.color = c;
	}
	
	@Override
	public void updateAcc(int index)
	{				
		super.CollisionDetect(index);
	}
	
	@Override
	public void updateVelPos()
	{
		super.updateVelPos();
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		synchronized (this.imageLock)
		{
			if (this.needsRedraw)
			{
				switch(type)
				{
					case 0:
						//tempImage = ResourceLoader.getBufferedImage("asteroids/asteroid1.png"); //Load Asteroid Image
						this.currentImage = ResourceLoader.getBufferedImage("asteroids/asteroid1.png");
						break;
					case 1:
						this.currentImage = ResourceLoader.getBufferedImage("asteroids/asteroid2.png");
						//tempImage = ResourceLoader.getBufferedImage("asteroids/asteroid2.png"); //Load Asteroid Image
						break;
					default:
						//tempImage = ResourceLoader.getBufferedImage("asteroids/asteroid1.png"); //Load Asteroid Image
						break;
				}
				
				this.scale =  (this.size*2) / Math.max(this.currentImage.getWidth(), this.currentImage.getHeight());
			}
			
			super.draw(g2);
		}
	}

	@Override
	public void collisionAlert(PhysicsSprite impactor, double impact) 
	{
		this.rot_vel = Math.random()*5-2.5;
		
		if (impactor instanceof SpaceShip)
			this.health -= impact;
		if (this.health <= 0)
			this.remove = true;
	}
}
