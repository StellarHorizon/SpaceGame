/***************************
 * Purpose: GUIWindow class containing the
 * functionality for a window.
 *
 * Contributors:
 * - Zachary Johnson
 ***************************/

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GUIWindow extends Sprite
{
	private ArrayList<ActionButton> windowButtons;
	private ArrayList<MenuText> windowTexts;
	private String backgroundImagePath;
	ActionButton currentButton; //The button currently being acted on
	public Color textColor;
	private BufferedImage windowImage;
	
	public GUIWindow(GUIWindowType windowType)
	{
		this.windowButtons = new ArrayList<ActionButton>();
		this.windowTexts = new ArrayList<MenuText>();
		this.backgroundImagePath = "gui/";
		this.textColor = Color.WHITE;
		this.updateBackgroundImage(windowType);
	}
	
	public void addButton(ActionButton button)
	{
		synchronized(this.windowButtons)
		{
			this.windowButtons.add(button);
			
			this.needsRedraw = true;
		}
	}
	public void removeButton(ActionButton button)
	{
		synchronized(this.windowButtons)
		{
			this.windowButtons.remove(button);
			
			this.needsRedraw = true;
		}
	}
	
	public void addMenuText(MenuText menuText)
	{
		synchronized(this.windowTexts)
		{
			this.windowTexts.add(menuText);
			
			this.needsRedraw = true;
		}
	}
	public void removeMenuText(MenuText menuText)
	{
		synchronized(this.windowTexts)
		{
			this.windowTexts.remove(menuText);
			
			this.needsRedraw = true;
		}
	}
	
	private void updateBackgroundImage(GUIWindowType windowType)
	{
		if (windowType == null)
		{
			windowImage = ResourceLoader.getBufferedImage(backgroundImagePath + "guiwindow.png");
			return;
		}
		else
			switch(windowType)
			{
				case REGULAR:
					this.windowImage = ResourceLoader.getBufferedImage(backgroundImagePath + "guiwindow.png");
					break;
				case WIDE:
					this.windowImage = ResourceLoader.getBufferedImage(backgroundImagePath + "guiwindow_wide.png");
					break;
				default:
					this.windowImage = ResourceLoader.getBufferedImage(backgroundImagePath + "guiwindow.png");
					break;
			}
		
		if (this.currentImage == null)
			this.currentImage = windowImage;
	}
	
	/*
	 * Method sets the guiWindow to the ingame menu state
	 */
	public void setMenu()
	{
		this.setInstructions();
		
		ActionButton mainMenuButton = new ActionButton("Return to Main Menu");
		mainMenuButton.setPos(ReferencePositions.CENTER, new Vector2D(this.windowImage.getWidth(), this.windowImage.getHeight()-50));
		mainMenuButton.setFont(new Font("Monospace", Font.BOLD, 20));
		mainMenuButton.setButtonAction(GUIButtonActions.MAIN_MENU);
		mainMenuButton.setIsToggleButton(false);
		mainMenuButton.setTextColor(Color.RED);
		mainMenuButton.setButtonSize(new Vector2D(160, 35));
		mainMenuButton.setFontSize(15);
		
		synchronized(this.windowButtons)
		{
			this.windowButtons.add(mainMenuButton);
		}
	}
	
	public void setInstructions()
	{
		synchronized(this.imageLock)
		{
			synchronized(this.windowButtons)
			{
				this.windowButtons.clear();
			}
			synchronized(this.windowTexts)
			{
				this.windowTexts.clear();
			}
			
			this.setStandardWindow();
			
			MenuText windowTitle = new MenuText("INSTRUCTIONS");
			windowTitle.setPos(ReferencePositions.CENTER, this.windowImage.getWidth()/2, 20);
			
			MenuText instructions1 = new MenuText("Blast your way through the asteroid field and", 20, 90);
			instructions1.setFontSize(16);
			MenuText instructions2 = new MenuText("make it safely to the other side. Gather energy", 20, 110);
			instructions2.setFontSize(16);
			MenuText instructions3 = new MenuText("by destroying asteroids to upgrade your ship.", 20, 130);
			instructions3.setFontSize(16);
			MenuText instructions4 = new MenuText("Be careful! You need energy to recharge your", 20, 150);
			instructions4.setFontSize(16);
			MenuText instructions5 = new MenuText("shields, use thrusters, and run life support.", 20, 170);
			instructions5.setFontSize(16);
			MenuText instructions6 = new MenuText("The goal is to reach the end of the asteroid", 20, 190);
			instructions6.setFontSize(16);
			MenuText instructions7 = new MenuText("field, the top of the map.", 20, 210);
			instructions7.setFontSize(16);
			MenuText instructions8 = new MenuText("Run out of energy and you will be...", 20, 240);
			instructions8.setFontSize(16);
			MenuText instructions9 = new MenuText("LOST IN SPACE", 100, 270);
			instructions9.setFontSize(24);
			
			synchronized(this.windowTexts)
			{
				this.windowTexts.add(windowTitle);
				this.windowTexts.add(instructions1);
				this.windowTexts.add(instructions2);
				this.windowTexts.add(instructions3);
				this.windowTexts.add(instructions4);
				this.windowTexts.add(instructions5);
				this.windowTexts.add(instructions6);
				this.windowTexts.add(instructions7);
				this.windowTexts.add(instructions8);
				this.windowTexts.add(instructions9);
			}
			
			this.needsRedraw = true;
		}
	}
	
	/*
	 * Method sets the guiWindow to the ingame upgrade menu state
	 */
	public void setUpgrades()
	{
		synchronized(this.imageLock)
		{
			synchronized(this.windowButtons)
			{
				this.windowButtons.clear();
			}
			synchronized(this.windowTexts)
			{
				this.windowTexts.clear();
			}
			
			this.setStandardWindow();
		
			//Display ship upgrades
			MenuText confirmText1 = new MenuText("UPGRADE?");
			confirmText1.setPos(ReferencePositions.CENTER, this.windowImage.getWidth()/2, 20);
			MenuText confirmText2 = new MenuText("This will cost " + SpriteList.getPlayerShip().getNextUpgradeCost() + " energy", 20, 100);
			MenuText confirmText3 = new MenuText("You have " + (int) SpriteList.getPlayerShip().energy + " energy", 20, 60);
			
			ActionButton upgradeButton = new ActionButton("UPGRADE !");
			upgradeButton.setButtonSize(new Vector2D(this.windowImage.getWidth() - 30, 60));
			upgradeButton.setPos(ReferencePositions.CENTER, this.windowImage.getWidth()/2, 250);
			upgradeButton.setFontSize(20);
			upgradeButton.setTextColor(Color.YELLOW);
			upgradeButton.setButtonAction(GUIButtonActions.UPGRADE_SHIP);
			upgradeButton.setIsToggleButton(false);
			
			MenuText shipLevelText = new MenuText("Current Ship Level: " + SpriteList.getPlayerShip().getUpgradeLevel());
			shipLevelText.setPos(ReferencePositions.CENTER, this.windowImage.getWidth()/2, 300);
			
			//Add buttons
			synchronized(this.windowButtons)
			{
				this.windowButtons.add(upgradeButton);
			}
			synchronized(this.windowTexts)
			{
				this.windowTexts.add(confirmText1);
				this.windowTexts.add(confirmText2);
				this.windowTexts.add(confirmText3);
				this.windowTexts.add(shipLevelText);
			}
			
			this.needsRedraw = true;
		}
	}
	
	public void setGameWin()
	{
		synchronized(this.imageLock)
		{
			synchronized(this.windowButtons)
			{
				this.windowButtons.clear();
			}
			synchronized(this.windowTexts)
			{
				this.windowTexts.clear();
			}
			
			this.setStandardWindow();
		
			//Display ship upgrades
			MenuText winText = new MenuText("YOU WIN !!");
			winText.setPos(ReferencePositions.CENTER, this.windowImage.getWidth()/2, 20);
			winText.setTextColor(Color.GREEN);
			
			synchronized(this.windowTexts)
			{
				this.windowTexts.add(winText);
			}
			
			this.needsRedraw = true;
		}
	}
	
	public void setGameLose()
	{
		synchronized(this.imageLock)
		{
			synchronized(this.windowButtons)
			{
				this.windowButtons.clear();
			}
			synchronized(this.windowTexts)
			{
				this.windowTexts.clear();
			}
			
			this.setStandardWindow();
		
			//Display ship upgrades
			MenuText resultText = new MenuText("GAME OVER !");
			resultText.setFontSize(20);
			resultText.setTextColor(Color.RED);
			resultText.setPos(ReferencePositions.CENTER, this.windowImage.getWidth()/2, 20);
			
			MenuText scoreDescription = new MenuText("Final Score:");
			scoreDescription.setFontSize(10);
			scoreDescription.setTextColor(Color.RED);
			scoreDescription.setPos(ReferencePositions.CENTER, this.windowImage.getWidth()/2, 40);
			
			MenuText scoreText = new MenuText("0");
			scoreText.setFontSize(20);
			scoreText.setTextColor(Color.RED);
			scoreText.setPos(ReferencePositions.CENTER, this.windowImage.getWidth()/2, 60);
			
			synchronized(this.windowTexts)
			{
				this.windowTexts.add(resultText);
			}
			
			this.needsRedraw = true;
		}
	}
	
	public void setStandardWindow()
	{
		synchronized (this.imageLock)
		{
			updateBackgroundImage(GUIWindowType.REGULAR);
			
			this.currentImage = this.windowImage;
			
			//Add a close Button
			ActionButton closeButton = new ActionButton("X");
			closeButton.setPos(ReferencePositions.TOP_LEFT, new Vector2D(this.windowImage.getWidth()-37, 10));
			closeButton.setFont(new Font("Monospace", Font.BOLD, 20));
			closeButton.setButtonAction(GUIButtonActions.CLOSE_WINDOW);
			closeButton.setIsToggleButton(false);
			closeButton.setTextColor(Color.RED);
			closeButton.setButtonSize(new Vector2D(25, 25));
			closeButton.setFontSize(15);
			
			//Add large close window button
			ActionButton closeButton2 = new ActionButton("Close Window");
			closeButton2.setPos(ReferencePositions.CENTER, new Vector2D(this.windowImage.getWidth(), this.windowImage.getHeight()));
			closeButton2.setFont(new Font("Monospace", Font.BOLD, 20));
			closeButton2.setButtonAction(GUIButtonActions.CLOSE_WINDOW);
			closeButton2.setIsToggleButton(false);
			closeButton2.setTextColor(Color.RED);
			closeButton2.setButtonSize(new Vector2D(160, 35));
			closeButton2.setFontSize(15);
			
			//Add buttons
			synchronized(this.windowButtons)
			{
				this.windowButtons.add(closeButton);
				this.windowButtons.add(closeButton2);
			}
			
			this.needsRedraw = true;
		}
	}
	
	public void updateUpgradeWindow()
	{
		this.setUpgrades();
//		synchronized(this.imageLock)
//		{
//			for (int i = 0; i < this.windowTexts.size(); i++)
//			{
//				this.windowTexts.get(i).needsRedraw = true;
//			}
//			this.needsRedraw = true;
//		}
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		synchronized (this.imageLock)
		{
			if (this.needsRedraw || this.currentImage == null)
			{
				AffineTransform at = new AffineTransform();
				
				if (this.windowImage == null)
					updateBackgroundImage(GUIWindowType.REGULAR);
				
				this.currentImage = new BufferedImage((int)windowImage.getWidth()-1, (int)windowImage.getHeight()-1, BufferedImage.TYPE_INT_ARGB);
				Graphics2D c2 = this.currentImage.createGraphics();
				
				c2.drawImage(windowImage.getScaledInstance(this.currentImage.getWidth()-1, this.currentImage.getHeight()-1, Image.SCALE_SMOOTH), 
						0, 0, null);
				
				//Draw the buttons
				for (int i = 0; i < this.windowButtons.size(); i++)
				{
					this.windowButtons.get(i).draw(c2);
				}
				
				if (SpriteList.getPlayerShip() != null && SpriteList.getPlayerShip().upgradesUpdated)
				{
					this.updateUpgradeWindow();
					SpriteList.getPlayerShip().upgradesUpdated = false;
				}
				
				//Draw the text elements
				for (int i = 0; i < this.windowTexts.size(); i++)
				{
					this.windowTexts.get(i).draw(c2);
				}
				
				at.translate(this.pos.x, this.pos.y);
				
				this.needsRedraw = false;
				
				super.drawStatic(g2, at);
			}
			else
				super.drawStatic(g2);
		}
	}
	
	/*
	 * Method called when the mouse is pressed. Returns true 
	 * if the component needs to be redrawn, false otherwise
	 */
	public boolean mouseDown(MouseEvent e, Vector2D position)
	{
		Vector2D mousePoint = new Vector2D(position.x - this.pos.x, position.y - this.pos.y);
		
		synchronized(this.windowButtons)
		{
			for (ActionButton curr : windowButtons)
				if (curr.isWithin(mousePoint) && !curr.isDisabled())
				{
					curr.startClick();
					this.currentButton = curr;
					this.needsRedraw = true;
					return true;
				}
		}
		
		return false;
	}
	
	/*
	 * Method called when the mouse is released. Returns true
	 * if the component needs to be redrawn, false otherwise
	 */
	public boolean mouseUp(MouseEvent e, Vector2D position)
	{
		//Translate coordinates to coordinates relative to window
		Vector2D mousePoint = new Vector2D(position.x - this.pos.x, position.y - this.pos.y);
		
		synchronized(this.windowButtons)
		{
			if (this.currentButton != null)
			{
				if (this.currentButton.isWithin(mousePoint))
					this.currentButton.finishClick();
				else
					this.currentButton.cancelClick();
				
				this.needsRedraw = true;
				
				this.currentButton = null;
				
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * Method called when a mouse scroll event occurs. Returns
	 * true if the component needs to be redrawn, false otherwise
	 */
	public boolean mouseScroll(MouseWheelEvent e, Vector2D position)
	{
		return false;
	}
	
	
	/*
	 * Method called when a mouse drag event occurs. Returns
	 * true if the component needs to be redrawn, false otherwise
	 */
	public boolean mouseDrag(MouseEvent e, Vector2D position)
	{
		return false;
	}
	
	
	/*
	 * Method called when a mouse move event occurs. Returns
	 * true if the component needs to be redrawn, false otherwise
	 */
	public boolean mouseMove(MouseEvent e, Vector2D position)
	{
		Vector2D mousePoint = new Vector2D(position.x - this.pos.x, position.y - this.pos.y);
		
		synchronized(this.windowButtons)
		{
			for (ActionButton curr : this.windowButtons)
			{
				if (curr.isWithin(mousePoint))
				{
					if (!curr.isDisabled() && curr.getState() != GUIButtonStates.ACTIVE)
					{
						curr.setState(GUIButtonStates.HOVER);
						this.needsRedraw = true;
						return true;
					}
				}
				else if (curr.getState() == GUIButtonStates.HOVER)
				{
					curr.setState(GUIButtonStates.NORMAL);
					this.needsRedraw = true;
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	/*
	 * Method called when a key press event occurs. Returns
	 * true if the component needs to be redrawn, false otherwise
	 */
	public boolean keyPress(KeyEvent e)
	{
		return false;
	}
}

























