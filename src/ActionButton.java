/***************************
 * Purpose: ActionButton class containing the
 * functionality of a button that can be clicked to
 * perform an action. The possible states for a
 * button are defined in the GUIButtonStates enum,
 * while the possible actions for a button are
 * defined in the GUIButtonActions enum.
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ActionButton extends Sprite
{
	//Member variables
	private MenuText text;
	private Vector2D size;
	private String imagePath;
	
	private BufferedImage buttonImages[] = new BufferedImage[5];
	
	private GUIButtonActions buttonAction;
	private GUIButtonStates buttonState, previousState;
	
	private boolean isDisabled;
	private boolean isToggleButton;
	
	
	public ActionButton()
	{
		initializeButton("TEST BUTTON", new Vector2D(0, 0));
	}
	
	public ActionButton(String description)
	{
		initializeButton(description, new Vector2D(0, 0));
	}
	
	public ActionButton(Vector2D position)
	{
		initializeButton("TEST BUTTON", position);
	}
	
	public ActionButton(String description, Vector2D position)
	{
		initializeButton(description, position);
	}
	
	private void initializeButton(String description, Vector2D position)
	{
		this.pos = new Vector2D(position);
		
		this.previousState = null;
		this.isDisabled = false;
		this.isToggleButton = true;
		this.text = new MenuText(description);
		this.buttonAction = GUIButtonActions.DO_NOTHING;
		this.rotation = new Rotation(0);
		this.visible = true;
		this.remove = false;
		
		if (this.imagePath == null)
//			setImagePath("resources/buttons/testButton/");
			setImagePath("buttons/standard/");
		else
			setImagePath(this.imagePath);
		
		//String imagePath = "resources/buttons/testButton/";
//		this.imagePath = "buttons/testButton/";
		
		
		
//		buttonImages[0] = ResourceLoader.getBufferedImage(imagePath + "active.png");
//		buttonImages[1] = ResourceLoader.getBufferedImage(imagePath + "disabled.png");
//		buttonImages[2] = ResourceLoader.getBufferedImage(imagePath + "hover.png");
//		buttonImages[3] = ResourceLoader.getBufferedImage(imagePath + "normal.png");
//		buttonImages[4] = ResourceLoader.getBufferedImage(imagePath + "pressed.png");
//		
		this.setState(GUIButtonStates.NORMAL);
		this.needsRedraw = true;
	}
	
	public void setImagePath(String path)
	{
		this.imagePath = path;
		
		synchronized (this.imageLock)
		{
			buttonImages[0] = ResourceLoader.getBufferedImage(imagePath + "active.png");
			buttonImages[1] = ResourceLoader.getBufferedImage(imagePath + "disabled.png");
			buttonImages[2] = ResourceLoader.getBufferedImage(imagePath + "hover.png");
			buttonImages[3] = ResourceLoader.getBufferedImage(imagePath + "normal.png");
			buttonImages[4] = ResourceLoader.getBufferedImage(imagePath + "pressed.png");
		}
		
		this.needsRedraw = true;
	}
	
	public void disable()
	{
		this.isDisabled = true;
		this.needsRedraw = true;
	}
	public void enable()
	{
		this.isDisabled = false;
		this.needsRedraw = true;
	}
	public boolean isDisabled()
	{
		return this.isDisabled;
	}
	
	public void setText(String newText)
	{
		this.text.setText(newText);
	}
	public String getText()
	{
		return this.text.getText();
	}
	
	public void setButtonAction(GUIButtonActions action)
	{
		this.buttonAction = action;
	}
	public GUIButtonActions getButtonAction()
	{
		return this.buttonAction;
	}
	
	public void setTextColor(Color newColor)
	{
		this.text.setTextColor(newColor);
	}
	public Color getTextColor()
	{
		return this.text.getTextColor();
	}
	
	public void setShadowColor(Color newColor)
	{
		this.text.setShadowColor(newColor);
	}
	public Color getShadowColor()
	{
		return this.text.getShadowColor();
	}
	
	
	public void setIsToggleButton(boolean toggleChoice)
	{
		this.isToggleButton = toggleChoice;
		this.needsRedraw = true;
	}
	
	//Called on mouse press while hovering over this button
	public void startClick()
	{
		this.previousState = this.getState();
		
		currentImage = buttonImages[4];
		
		this.needsRedraw = true;
	}
	
	//Called if user cancels clicking button (by click and holding button, but releasing outside of button)
	public void cancelClick()
	{
		this.setState(this.previousState);
	}
	
	//Called when mouse released while hovering over this button
	public void finishClick()
	{
		if (this.previousState == GUIButtonStates.ACTIVE)
			this.setState(GUIButtonStates.NORMAL);
		else if (this.previousState == GUIButtonStates.NORMAL || this.previousState == GUIButtonStates.HOVER)
		{
			ButtonController.doAction(this.buttonAction, this);
			
			//Set button back to correct state
			if (this.isToggleButton)
				this.setState(GUIButtonStates.ACTIVE);
			else
				this.setState(GUIButtonStates.NORMAL);
			
			this.needsRedraw = true;
		}
	}
	
	public void setPos(ReferencePositions refPoint, int x, int y)
	{
		setPos(refPoint, new Vector2D(x, y));
	}
	public void setPos(ReferencePositions refPoint, Vector2D newPos)
	{
		switch (refPoint)
		{
			case TOP_LEFT:
				this.pos = new Vector2D(newPos);
				break;
			case TOP_CENTER:
				this.pos = new Vector2D((int) (newPos.x - this.size.x / 2), (int) newPos.y);
				break;
			case TOP_RIGHT:
				this.pos = new Vector2D((int) (newPos.x - this.size.x), (int) newPos.y);
				break;
			case CENTER_LEFT:
				this.pos = new Vector2D((int) newPos.x, (int) (newPos.y - this.size.y / 2));
				break;
			case CENTER:
				this.pos = new Vector2D((int) (newPos.x - this.size.x / 2), (int) (newPos.y - this.size.y / 2));
				break;
			case CENTER_RIGHT:
				this.pos = new Vector2D((int) (newPos.x - this.size.x), (int) (newPos.y - this.size.y / 2));
				break;
			case BOTTOM_LEFT:
				this.pos = new Vector2D((int) newPos.x, (int) (newPos.y - this.size.y));
				break;
			case BOTTOM_CENTER:
				this.pos = new Vector2D((int) (newPos.x - this.size.x / 2), (int) (newPos.y - this.size.y));
				break;
			case BOTTOM_RIGHT:
				this.pos = new Vector2D((int) (newPos.x - this.size.x), (int) (newPos.y - this.size.y));
				break;
		}
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
				return new Vector2D((int) (this.pos.x + this.size.x / 2), (int) this.pos.y);
			case TOP_RIGHT:
				return new Vector2D((int) (this.pos.x + this.size.x), (int) this.pos.y);
			case CENTER_LEFT:
				return new Vector2D((int) this.pos.x, (int) (this.pos.y + this.size.y / 2));
			case CENTER:
				return new Vector2D((int) (this.pos.x + this.size.x / 2), (int) (this.pos.y + this.size.y / 2));
			case CENTER_RIGHT:
				return new Vector2D((int) (this.pos.x + this.size.x), (int) (this.pos.y + this.size.y / 2));
			case BOTTOM_LEFT:
				return new Vector2D((int) this.pos.x, (int) (this.pos.y + this.size.y));
			case BOTTOM_CENTER:
				return new Vector2D((int) (this.pos.x + this.size.x / 2), (int) (this.pos.y + this.size.y));
			case BOTTOM_RIGHT:
				return new Vector2D((int) (this.pos.x + this.size.x), (int) (this.pos.y + this.size.y));
			default:
				return this.pos;
		}
	}
	
	public void setButtonSize(Vector2D newSize)
	{
		//Change font size to fit
		this.setFontSize((int) Math.floor(this.getFontSize() * (newSize.x / this.size.x)));
		
		this.size = newSize;
		
		this.needsRedraw = true;
	}
	public Font getFont()
	{
		return this.text.getFont();
	}
	public void setFont(Font f)
	{
		this.text.setFont(f);
		this.needsRedraw = true;
	}
	public int getFontSize()
	{
		return this.text.getFont().getSize();
	}
	public void setFontSize(int fontSize)
	{
		this.text.setFont(new Font(this.text.getFont().getFontName(), this.text.getFont().getStyle(), fontSize));
		this.needsRedraw = true;
	}
	
	public void setState(GUIButtonStates newState)
	{
		switch (newState)
		{
			case ACTIVE:
				currentImage = buttonImages[0];
				break;
			case DISABLED:
				currentImage = buttonImages[1];
				break;
			case HOVER:
				currentImage = buttonImages[2];
				break;
			case NORMAL:
				currentImage = buttonImages[3];
				break;
			case PRESSED:
				currentImage = buttonImages[4];
				break;
			default: //default to disabled button
				currentImage = buttonImages[1];
				break;
		}
		
		if (this.size == null)
			this.size = new Vector2D(currentImage.getWidth(), currentImage.getHeight());
		
		this.previousState = this.buttonState;
		this.buttonState = newState;
		this.needsRedraw = true;
	}
	
	public GUIButtonStates getState()
	{
		return this.buttonState;
	}
	
	boolean update(Random rand)
	{
		return true;
	}
	
	private BufferedImage getBackgroundImage()
	{
		switch(this.buttonState)
		{
			case ACTIVE:
				return buttonImages[0];
			case DISABLED:
				return buttonImages[1];
			case HOVER:
				return buttonImages[2];
			case NORMAL:
				return buttonImages[3];
			case PRESSED:
				return buttonImages[4];
			default:
				return buttonImages[1];
		}
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		synchronized (this.imageLock)
		{
			if (this.needsRedraw || this.currentImage == null)
			{
				AffineTransform at = new AffineTransform();
				
				this.currentImage = new BufferedImage((int)this.size.x, (int)this.size.y, BufferedImage.TYPE_INT_ARGB);
				Graphics2D c2 = this.currentImage.createGraphics();
				BufferedImage sourceImage = getBackgroundImage();
				
				//Draw source image scaled to button's current size
				c2.drawImage(sourceImage.getScaledInstance(this.currentImage.getWidth()-1, this.currentImage.getHeight()-1, Image.SCALE_SMOOTH), 
						0, 0, null);
				this.text.setPos(ReferencePositions.CENTER, new Vector2D(this.currentImage.getWidth()/2, this.currentImage.getHeight()/2 - this.text.shadowOffset/2));
				this.text.draw(c2);
				
				at = new AffineTransform();
				at.translate(this.pos.x, this.pos.y);
				
				this.needsRedraw = false;
				
				super.drawStatic(g2, at);
			}
			else
				super.drawStatic(g2);
		}
	}
	
	//Tests whether the point is within the clickable area for the button
	public boolean isWithin(Vector2D point)
	{
		if (point.x >= this.pos.x && point.x <= this.pos.x + this.size.x
			&& point.y >= this.pos.y && point.y <= this.pos.y + this.size.y)
			return true;
		else
			return false;
	}
}
