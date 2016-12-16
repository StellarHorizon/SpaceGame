/***************************
 * Purpose: Rotation class to handle the
 * rotation of sprites.
 *
 * Contributors:
 * - Zachary Johnson
 ***************************/

public class Rotation
{
	private double rotation;
	
	public Rotation()
	{
		this.rotation = 0.0;
	}
	
	public Rotation(double degrees)
	{
		this.rotation = degrees;
		validateRotation();
	}
	
	public Rotation(Rotation rotation2) 
	{
		this.rotation = rotation2.rotation;
	}

	public void setRotation(double degrees)
	{
		this.rotation = degrees;
		validateRotation();
	}
	
	//Returns the current rotation in degrees
	public double getDegrees()
	{
		return this.rotation;
	}
	
	//Returns the current rotation in degrees
	public double getRadians()
	{
		return Math.toRadians(this.rotation);
	}
	
	public void addAmount(double degrees)
	{
		this.rotation += degrees;
		validateRotation();
	}
	
	private void validateRotation()
	{
		while(this.rotation < 0.0)
			rotation += 360;
		
		while(this.rotation > 360.0)
			rotation -= 360;
	}
}
