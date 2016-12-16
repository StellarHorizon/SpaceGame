/***************************
 * Purpose: MenuText helper class for drawing
 * text with additional features (such as
 * font, shadow, position). Used as part of
 * ActionButton.
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.FontMetrics;

public class MenuText extends Sprite
{
	private String text;
	private Color textColor, shadowColor;
	private Font font;
	private int textWidth, textHeight;
	public final int shadowOffset = 3;
	
	/*******************
	 * Constructors
	 ******************/
	 
	public MenuText()
	{
		this.text = "SAMPLE TEXT";
		this.pos = new Vector2D(200, 200);
	}
	
	//Universal constructor
	private void constrMenuText(String s, Vector2D position, Color colorText, Color colorShadow)
	{
		if (s != null)
			this.text = s;
		else
			this.text = "SAMPLE TEXT";
		
		if (position != null)
			this.pos = new Vector2D((int) position.x, (int) position.y);
		else
			this.pos = new Vector2D(200, 200);
		
		if (colorText != null)
			this.textColor = colorText;
		else
			this.textColor = Color.WHITE;
		
		
		if (colorShadow != null)
			setShadowColor(colorShadow);
		else
			setShadowColor(Color.BLACK);
		
		this.font = new Font("MONOSPACE", Font.BOLD, 20);
		
		this.currentImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D c2 = this.currentImage.createGraphics();
		c2.setFont(this.font);
		FontMetrics metrics = c2.getFontMetrics();
        int textWidth = metrics.stringWidth(this.text);
		int textHeight = metrics.getHeight();
		this.currentImage = new BufferedImage(textWidth + this.shadowOffset, 
				textHeight + this.shadowOffset, 
				BufferedImage.TYPE_INT_ARGB);
		
	}
	
	public MenuText(String s) { constrMenuText(s, null, null, null); }
	
	public MenuText(Vector2D position) { constrMenuText(null, new Vector2D((int) position.x, (int) position.y), null, null); }
	
	public MenuText(String s, Vector2D position) { constrMenuText(s, new Vector2D((int) position.x, (int) position.y), null, null); }
	public MenuText(String s, Vector2D position, Color colorText) { constrMenuText(s, new Vector2D((int) position.x, (int) position.y), colorText, null); }
	public MenuText(String s, Vector2D position, Color colorText, Color colorShadow) { constrMenuText(s, new Vector2D((int) position.x, (int) position.y), colorText, colorShadow); }
	
	public MenuText(String s, int posX, int posY) { constrMenuText(s, new Vector2D(posX, posY), null, null); }
	public MenuText(String s, int posX, int posY, Color colorText) { constrMenuText(s, new Vector2D(posX, posY), colorText, null); }
	public MenuText(String s, int posX, int posY, Color colorText, Color colorShadow) { constrMenuText(s, new Vector2D(posX, posY), colorText, colorShadow); }
	
	/*******************
	 * Getters/Setters
	 ******************/
	public String getText() { return this.text; }
	public void setText(String s) { this.text = s; }
	
	public Vector2D getPos() { return this.pos; }
	public void setPos(Vector2D point) {	 this.pos = new Vector2D((int) point.x, (int) point.y); }
	public void setPos(int posX, int posY) { this.pos = new Vector2D(posX, posY); }
	
	public Color getTextColor() { return this.textColor; }
	public void setTextColor(Color color) { this.textColor = color; }
	
	public Color getShadowColor() { return this.shadowColor; }
	public void setShadowColor(Color color)
	{
		this.shadowColor = new Color ((float)color.getRed()/255, (float)color.getGreen()/255, (float)color.getBlue()/255, (float)0.3);
	}
	
	public Font getFont() { return this.font; }
	public void setFont(Font f) { this.font = f; updateImageSize(); }
	public int getFontSize()
	{
		return this.getFont().getSize();
	}
	public void setFontSize(int fontSize)
	{
		this.font = new Font(this.getFont().getFontName(), this.getFont().getStyle(), fontSize);
		this.needsRedraw = true;
	}
	
	private void updateImageSize()
	{
		Graphics2D metricGraphics = this.currentImage.createGraphics();
		metricGraphics.setFont(this.font);
		FontMetrics metrics = metricGraphics.getFontMetrics();
        textWidth = metrics.stringWidth(this.text);
		textHeight = metrics.getHeight();
		
		synchronized(this.imageLock)
		{
			this.currentImage = new BufferedImage(textWidth + this.shadowOffset, 
					textHeight + this.shadowOffset, 
					BufferedImage.TYPE_INT_ARGB);
		}
		
		this.needsRedraw = true;
	}
	
	/*******************
	 * Drawing
	 ******************/
	@Override
	public void draw(Graphics2D g2)
	{
		//synchronized(this.imageLock)
		//{
			//Get dimensions of button text
			//Change font to get accurate height and width values
			//if (this.needsRedraw || this.currentImage == null)
			//{
				updateImageSize();
				
				Graphics2D c2 = this.currentImage.createGraphics();
				c2.setFont(this.font);
				
				c2.setColor(this.shadowColor);
				c2.drawString(this.text, this.shadowOffset, textHeight + this.shadowOffset);
				
				c2.setColor(this.textColor);
				c2.drawString(this.text, 0, textHeight);
				
				this.needsRedraw = false;
			//}
			
			super.drawStatic(g2, this.pos.x, this.pos.y);
		//}
	}
}
