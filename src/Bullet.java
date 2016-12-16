import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Bullet extends PhysicsSprite
{
	boolean armed = false;
	boolean insideShip = true;
	double damage = 0;
	int lifeTime = 0, lifeExpectancy = 50;
	
	public Bullet(Vector2D position, Vector2D velocity, Rotation rotation, double size, double damage, int bulletImage) 
	{
		super(position, rotation, size, 1.0, 100);
		this.mass = 0.0;
		this.vel = velocity;
		this.damage = damage;
		this.currentImage = ResourceLoader.getBufferedImage("weapons/short_fat_beam_"+bulletImage+".png");
		//this.currentImage = new BufferedImage((int)Math.round(this.size*2), (int)Math.round(this.size*2), BufferedImage.TYPE_INT_ARGB);
		//Graphics2D c2 = this.currentImage.createGraphics();
		//c2.setColor(new Color(255,255,255));
		//c2.fillOval(0, 0, (int)(this.size*2), (int)(this.size*2));
	}

	@Override
	public void updateAcc(int index) 
	{
		super.CollisionDetect(index);
	}

	@Override
	public void collisionAlert(PhysicsSprite impactor, double impact) 
	{
		if (!this.armed && impactor instanceof SpaceShip)
		{
			this.insideShip = true;
		}
		
		if (this.armed)
		{
			if (impactor instanceof Asteroid)
			{
				impactor.health -= this.damage;
				if (impactor.health <= 0)
					impactor.remove = true;
				
				this.remove = true;
			}
		}
	}
	
	@Override
	public void updateVelPos()
	{
		if (!this.armed && !this.insideShip)
		{
			this.armed = true;
		}
		this.insideShip = false;
		
		this.lifeTime++;
		if (this.lifeTime > this.lifeExpectancy)
		{
			this.remove = true;
		}
		
		super.updateVelPos();
	}
	
	@Override
	public void draw(Graphics2D g2)
	{ 		
		super.draw(g2);
	}

}
