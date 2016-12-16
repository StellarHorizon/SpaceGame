/***************************
 * Purpose: SpaceShip class containing common
 * functionality for spaceship behavior.
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

abstract class SpaceShip extends PhysicsSprite
{
	public double healthMax = 0, shield = 0, shieldMax = 0, shieldRegen = 0, energy = 0, bulletSpread = 1;
	public boolean left, right, forward, backward, firing;
	public double turnRate = 2, thrustPower = 0.2;
	
	public double bulletSize = 4, bulletVel = 15.0, bulletDamage = 100;
	public int bulletCooldown = 10, timeSinceFiring = 0;
	
	public int currentLevel = 1;
	
	public Color shieldColor;
	public BufferedImage shipImage;
	
	public SpaceShip(Vector2D position, Rotation rotation, double size, double health, double shield, double shieldRegen)
	{
		super(position,rotation,size, 1.0, health);
		
		this.healthMax = health;
		this.shield = shield;
		this.shieldMax = shield;
		this.shieldRegen = shieldRegen;
		this.energy = 1000;

		
		left = false;
		right = false;
		forward = false;
		backward = false;
		firing = false;
	}
	
	@Override
	public void updateAcc(int index)
	{		
		super.CollisionDetect(index);
		
		synchronized (this.acc)
		{
			//Add Acceleration from Thrusters			
			if (forward && !backward && this.energy > this.thrustPower)
			{
				this.acc = this.acc.add(new Vector2D(Math.cos(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower, Math.sin(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower));
				this.energy -= this.thrustPower;
			}
			if (backward && !forward && this.energy > this.thrustPower)
			{
				this.acc = this.acc.add(new Vector2D(-Math.cos(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower,-Math.sin(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower));
				this.energy -= this.thrustPower;
			}
			
			//Apply Friction Force (In Space?)
			this.acc = this.acc.subtract(this.vel.multiply(new Vector2D(0.02,0.02)));
			
			//Add Rotation
			if (left  && !right)
			{
				this.rot_acc =+ -this.turnRate;
			}
			if (right && !left)
			{
				this.rot_acc =+ this.turnRate;
			}
			
			//Apply Rotation Friction (In Space?)
			this.rot_acc = this.rot_acc - (this.rot_vel * 0.2);
		}
	}
	
	@Override
	public void collisionAlert(PhysicsSprite impactor, double impact)
	{
		if (impactor instanceof Asteroid)
		{
			this.shield -= impact;
			
			if (this.shield < 0)
			{
				this.health += this.shield;
				this.shield = 0.0;
			}
			
			if (this.health < 0)
			{
				this.remove = true;
			}
		}
	}
	
	@Override
	public void updateVelPos()
	{
		super.updateVelPos();
		
		synchronized(this.healthLock)
		{		
			if (this.energy > 0.5)
			{
				double shieldAdd = Math.min(this.shieldRegen, this.shieldMax - this.shield);
				double shieldCost = shieldAdd * 10;
				
				if (this.energy > shieldCost)
				{
					this.energy -= shieldCost;
					this.shield += shieldAdd;
				}
			}
			else
			{						
				this.health = Math.max(this.health-0.1, 0);
			}
			
			
		}
		
		this.timeSinceFiring++;
	}

	public Bullet fireBullet()
	{
		this.timeSinceFiring = 0;
		Vector2D bulletPos = new Vector2D(this.pos.x + (Math.cos(this.rotation.getRadians() - Math.PI / 2.0) * 20), this.pos.y + (Math.sin(this.rotation.getRadians() - Math.PI / 2.0) * 20));
		Vector2D bulletVel = new Vector2D(this.vel.x + (Math.cos(this.rotation.getRadians() + Math.toRadians(this.bulletSpread*Math.random() - this.bulletSpread/2) - Math.PI / 2.0) * this.bulletVel), this.vel.y + (Math.sin(this.rotation.getRadians() - Math.PI / 2.0) * this.bulletVel));
		Bullet bullet = new Bullet(bulletPos, bulletVel, new Rotation(this.rotation), this.bulletSize, this.bulletDamage, this.currentLevel);
		return bullet;
	}
	
}
