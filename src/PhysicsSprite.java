/***************************
 * Purpose: PhysicsSprite class extending Sprite.
 * Contains essential variables and behaviors for
 * sprites that respond to physics.
 *
 * Contributors:
 * - Derek Paschal
 * - Zachary Johnson
 ***************************/

import java.util.ArrayList;
import java.util.ListIterator;

abstract class PhysicsSprite extends Sprite
{
	public Vector2D vel;
	public Vector2D acc;
	public double rot_vel;
	public double rot_acc;
	public double size;
	public double mass;
	public double restitution;
	
	public double health;
	public Object healthLock = new Object();
	
	public PhysicsSprite(Vector2D position, Rotation rotation, double size, double restitution, double health) 
	{
		super(position);
		this.size = size;
		this.mass = size;
		this.vel = new Vector2D(0.0,0.0);
		this.acc = new Vector2D(0.0,0.0);
		this.rotation = rotation;
		this.rot_vel = 0.0;
		this.rot_acc = 0.0;
		this.restitution = restitution;
		this.health = health;
	}
	
	public abstract void updateAcc(int index);
	
	public abstract void collisionAlert(PhysicsSprite impactor, double impact);
	
	public void CollisionDetect(int index)
	{
		//Physics Collision Detection
		double distance;
		double overlap;
		Vector2D UnitVector;
		double VelocityOnNormal;
		double restitution;
	
		
		Sprite s;
		PhysicsSprite p;
		int listSize = SpriteList.size();
		for (int i = index+1; i < listSize; i++)
		{
			s = SpriteList.get(i);
			if(s instanceof PhysicsSprite)
				p = (PhysicsSprite)s;
			else
				continue;
			
			
			distance = this.pos.distance(p.pos); //Calculate Distance between Sprites
			overlap = (this.size + ((PhysicsSprite)p).size) - distance; //overlap of the Sprites
			if (overlap > 0 && distance > 0.0) //If the Sprites are Colliding
			{
				restitution = 1.0; //Reset local Restitution variable to default
				UnitVector  = this.pos.subtract(p.pos).divide(distance); //Find Unit Vector between Sprites
				VelocityOnNormal = ((PhysicsSprite)p).vel.subtract(this.vel).dot_product(UnitVector); //Portion of velocity on the Unit Vector
				
				if (VelocityOnNormal < 0) //If Velocity on the Normal is Negative (Sprites are moving away from each other)
				{
					restitution = Math.min(this.restitution, ((PhysicsSprite)p).restitution); //Modify Restitution to simulate inelastic collisions
				}
				
				Vector2D impact = UnitVector.multiply( (3 * restitution) * (Math.min( overlap , Math.min(this.size, p.size))));
				double impactValue = 0;
				//Add to acceleration based on collision depth and restitution and size of current sprite
				synchronized (this.acc)
				{
					if (this.mass > 0.0 && p.mass > 0.0)
					{
						this.acc = this.acc.add(impact.divide(this.mass));
						impactValue = impact.length()/this.mass;
					}
					synchronized (this.healthLock)
					{
						this.collisionAlert(p,impactValue);
					}
				}
				
				synchronized (p.acc)
				{
					if (p.mass > 0.0 && this.mass > 0.0)
					{
						p.acc = p.acc.add(impact.divide(-p.mass));
						impactValue = impact.length()/p.mass;
					}
					else
					{
						impactValue = 0;
					}
					synchronized (p.healthLock)
					{
						p.collisionAlert(this, impactValue);
					}
				}
				
			}
		}
	}
	
	public void updateVelPos()
	{
		//Integrate Acceleration
		this.vel = this.vel.add(this.acc);
		
		//Integrate Rotational Acceleration
		this.rot_vel += this.rot_acc;
			
		//Integrate Velocity
		this.pos = this.pos.add(this.vel);
		
		//Integrate Rotational Velocity
		this.rotation.addAmount(this.rot_vel);
	}
}
