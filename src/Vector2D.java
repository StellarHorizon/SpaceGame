/**
 * 
 * @author Derek Paschal
 * 
 * The purpose of Vector2D is to represent a two dimensional vector or point, and allow
 * vector operations to be performed on the vector.  All methods which perform vector 
 * operations must return a Vector2D.
 */
public class Vector2D 
{
	public double x=0.0;
	public double y=0.0;
	
	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(Vector2D vect)
	{
		this.x = vect.x;
		this.y = vect.y;
	}
	
	public Vector2D()
	{
		this.x = 0.0;
		this.y = 0.0;
	}
	
	
	public Vector2D add(Vector2D vect)
	{
		return new Vector2D(this.x + vect.x, this.y + vect.y);
	}
	public Vector2D add(double value) {
		
		return new Vector2D(this.x + value, this.y + value);
	}
	
	public Vector2D subtract(Vector2D vect)
	{
		return new Vector2D(this.x - vect.x, this.y - vect.y);
	}
	public Vector2D subtract(double value) {
		
		return new Vector2D(this.x - value, this.y - value);
	}
	
	public Vector2D multiply(Vector2D vect)
	{
		return new Vector2D(this.x * vect.x, this.y * vect.y);
	}
	public Vector2D multiply(double value) {
		
		return new Vector2D(this.x * value, this.y * value);
	}
	
	public Vector2D divide(Vector2D vect)
	{
		return new Vector2D(this.x / vect.x, this.y / vect.y);
	}
	public Vector2D divide(double value) {
		
		return new Vector2D(this.x / value, this.y / value);
	}
	
	
	public double distance(Vector2D vect)
	{
		return this.subtract(vect).length();
	}
	
	public double length()
	{
		return Math.sqrt((this.x * this.x) + (this.y * this.y));
	}
	
	public double dot_product(Vector2D vect)
	{
		return (this.x * vect.x) + (this.y * vect.y);
	}
}
