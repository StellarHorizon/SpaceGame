/***************************
 * Purpose: PlayerShip class extending SpaceShip,
 * containing behaviors specific to the player's
 * ship and not shared between all spaceships.
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class PlayerShip extends SpaceShip
{
	private int baseUpgradeCost, maxUpgradeLevel;
	public boolean upgradesUpdated = false;
	
	public PlayerShip(Vector2D position) 
	{
		super(position,new Rotation(0),40,10,5,0.025);
		this.baseUpgradeCost = 1000;
		this.maxUpgradeLevel = 7;
		this.shipImage = ResourceLoader.getBufferedImage("ships/13B1.png"); //Load Player Ship Image
		this.shieldColor = new Color((float)0.0, (float)0.7, (float)0.8);
	}
	
	public int getNextUpgradeCost()
	{
		return (int) (this.baseUpgradeCost + (1000 * (this.currentLevel-1)));
	}
	
	public int getUpgradeLevel()
	{
		return this.currentLevel;
	}
	
	public void upgradeShip()
	{
		if (this.energy > this.getNextUpgradeCost() && this.currentLevel < this.maxUpgradeLevel)
		{
			this.energy -= this.getNextUpgradeCost();
			this.healthMax *= 1.1;
			this.health = this.healthMax;
			this.shieldMax *= 1.3;
			this.shieldRegen *= 1.4;
			this.bulletVel *= 1.1;
			this.bulletSize += 1;
			this.bulletDamage *= 1.4;
			this.bulletSpread *= 1.1;
			this.thrustPower *= 1.1;
			this.bulletCooldown -= 1;
			this.currentLevel++;
			synchronized (this.imageLock)
			{
				switch (this.currentLevel)
				{
				case 2 :
					this.shipImage = ResourceLoader.getBufferedImage("ships/13B2.png");
					this.shieldColor = new Color((float)0.9, (float)0.2, (float)0.0);
					break;
				case 3 :
					this.shipImage = ResourceLoader.getBufferedImage("ships/13B3.png");
					this.shieldColor = new Color((float)0.1, (float)0.9, (float)0.4);
					break;
				case 4 :
					this.shipImage = ResourceLoader.getBufferedImage("ships/13B4.png");
					this.shieldColor = new Color((float)0.8, (float)0.8, (float)0.0);
					break;
				case 5 :
					this.shipImage = ResourceLoader.getBufferedImage("ships/13B5.png");
					this.shieldColor = new Color((float)1.0, (float)0.7, (float)0.0);
					break;
				case 6 :
					this.shipImage = ResourceLoader.getBufferedImage("ships/13B6.png");
					this.shieldColor = new Color((float)1.0, (float)1.0, (float)1.0);
					break;
				case 7 :
					this.shipImage = ResourceLoader.getBufferedImage("ships/13B7_0.png");
					break;
				default :
					this.shipImage = ResourceLoader.getBufferedImage("ships/13B1.png");
					this.shieldColor = new Color((float)0.0, (float)0.7, (float)0.8);
					break;
				}
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		synchronized (this.imageLock)
		{
			
				this.currentImage = new BufferedImage((int)this.size*2, (int)this.size*2, BufferedImage.TYPE_INT_ARGB); //create blank current image
				Graphics2D c2 = this.currentImage.createGraphics(); //Create graphics object for current Image
				c2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON); //Set Anti Aliasing
				
				Vector2D shipDims = new Vector2D(shipImage.getWidth(), shipImage.getHeight()); //Get Maximum dimensions of ship image
				double shipScale = (this.size*2*0.8) / Math.max(shipDims.x, shipDims.y); //Set scale size that will make ship image fit into size
				
				float shieldRatio = (float)(this.shield / this.shieldMax);
				int i = 0;
				float alpha = (float)(0.1*shieldRatio);
				for (; (alpha <= 1.0 && alpha >= 0.0) && (i < 4); alpha += (0.1*shieldRatio) ,i++)
				{
					c2.setColor(new Color(this.shieldColor.getRed(),this.shieldColor.getGreen(),this.shieldColor.getBlue(),(int)(alpha*255)));
					c2.drawOval(i, i, (int)(Math.round(this.size*2)-1-i*2), (int)(Math.round(this.size*2)-1-i*2));
				}
				for (; (alpha >= 0.0 && alpha <= 1.0) && (this.size - i > 1); alpha-= (0.04*shieldRatio), i++)
				{
					c2.setColor(new Color(this.shieldColor.getRed(),this.shieldColor.getGreen(),this.shieldColor.getBlue(),(int)(alpha*255)));
					c2.drawOval(i, i, (int)(Math.round(this.size*2)-1-i*2), (int)(Math.round(this.size*2)-1-i*2));
				}
								
				c2.scale(shipScale, shipScale); //Apply scaler for ship image drawing
				c2.drawImage(shipImage, (int)((this.size - (shipDims.x * 0.5 * shipScale))/shipScale), (int)((this.size - (shipDims.y * 0.5 * shipScale))/shipScale), null); //Draw ship image onto current image
						
				/*c2.scale(1.0/shipScale, 1.0/shipScale);
				c2.setColor(Color.white);
				c2.drawRect(0, 0, this.currentImage.getWidth()-1, this.currentImage.getHeight()-1);
				c2.fillOval((int)this.size - 1, (int)this.size - 1, 2, 2);*/
				
				c2.dispose();
			
			
			super.draw(g2);
		}
	}

}
