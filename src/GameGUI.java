/***************************
 * Purpose: GameGUI class encompassing all
 * GUI functionality. (Replaces and expands
 * on old FullScreenMainMenu class).
 *
 * Contributors:
 * - Zachary Johnson
 ***************************/

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameGUI extends Sprite{
	
	ArrayList<ActionButton> guiButtons;
	ArrayList<MenuText> guiTexts;
	ArrayList<GUIBar> guiBars;
	ActionButton currentButton; //The button currently being acted on
	GUIWindow guiWindow;
	
	public GameGUI()
	{
		this.guiButtons = new ArrayList<ActionButton>();
		this.guiTexts = new ArrayList<MenuText>();
		this.guiBars = new ArrayList<GUIBar>();
		this.currentButton = null;
		this.currentImage = new BufferedImage((int)ViewCamera.renderRes.x - 1, (int)ViewCamera.renderRes.y - 1, 
				BufferedImage.TYPE_INT_ARGB);
		this.setMainMenu();
	}
	
	public void setMainMenu()
	{
		//Close any windows if they are open
		this.guiWindow = null;
		
		synchronized(this.guiButtons)
		{
			this.currentButton = null;
			this.guiButtons.clear();
		}
		synchronized(this.guiTexts)
		{
			this.guiTexts.clear();
		}
		synchronized(this.guiBars)
		{
			this.guiBars.clear();
		}
		
		//Create main menu components
		MenuText title = new MenuText("ASTEROID ESCAPE", (int)(ViewCamera.renderRes.x / 2), 250, Color.GREEN, GameConstant.SLATE_GRAY);
		title.setFont(new Font("Courier New", Font.BOLD, 40));
		title.setPos(ReferencePositions.TOP_CENTER, new Vector2D(ViewCamera.renderRes.x / 2, 45));
		
		ActionButton startButton = new ActionButton("Start Game");
		startButton.setButtonSize(new Vector2D(400, 50));
		startButton.setPos(ReferencePositions.CENTER, new Vector2D(ViewCamera.renderRes.x / 2, 250));
//		startButton.setPos(ReferencePositions.TOP_CENTER, new Vector2D(ViewCamera.renderRes.x / 2, 200));
		startButton.setButtonAction(GUIButtonActions.START_GAME);
		startButton.setFontSize(18);
		startButton.setIsToggleButton(false);
		
		ActionButton quitButton = new ActionButton("Quit Game");
		quitButton.setButtonSize(new Vector2D(400, 50));
		quitButton.setPos(ReferencePositions.CENTER, new Vector2D(ViewCamera.renderRes.x / 2, 350));
		quitButton.setButtonAction(GUIButtonActions.QUIT_GAME);
		quitButton.setFontSize(18);
		quitButton.setIsToggleButton(false);
		
		//Add the buttons
		synchronized(this.guiButtons)
		{
			this.guiButtons.add(startButton);
			this.guiButtons.add(quitButton);
		}
		//Add the text elements
		synchronized(this.guiTexts)
		{
			this.guiTexts.add(title);
		}
		
		this.needsRedraw = true;
	}
	
	public void setGame()
	{
		
		synchronized(this.guiButtons)
		{
			this.currentButton = null;
			this.guiButtons.clear();
		}
		synchronized(this.guiTexts)
		{
			this.guiTexts.clear();
		}
		
		//Create GUI Buttons
		
		ActionButton endButton = new ActionButton("MENU", new Vector2D(5, 5));
		endButton.setButtonAction(GUIButtonActions.OPEN_MENU);
		endButton.setIsToggleButton(false);
		endButton.setTextColor(Color.YELLOW);
		endButton.setButtonSize(new Vector2D(50, 30));
		endButton.setFontSize(9);
		
		ActionButton pauseButton = new ActionButton("PAUSE", new Vector2D(5, 40));
		pauseButton.setFont(new Font("Monospace", Font.BOLD, 20));
		pauseButton.setButtonAction(GUIButtonActions.TOGGLE_PAUSED);
		pauseButton.setIsToggleButton(false);
		pauseButton.setTextColor(Color.RED);
		pauseButton.setButtonSize(new Vector2D(50, 30));
		pauseButton.setFontSize(9);
		
		ActionButton upgradesButton = new ActionButton("UPGRADE", new Vector2D(5, 75));
		upgradesButton.setFont(new Font("Monospace", Font.BOLD, 20));
		upgradesButton.setButtonAction(GUIButtonActions.OPEN_UPGRADES);
		upgradesButton.setIsToggleButton(false);
		upgradesButton.setTextColor(Color.GREEN);
		upgradesButton.setButtonSize(new Vector2D(50, 30));
		upgradesButton.setFontSize(9);
		
		updateGUIIndicators();
		
		//Add the buttons
		synchronized(this.guiButtons)
		{
			this.guiButtons.add(endButton);
			this.guiButtons.add(pauseButton);
			this.guiButtons.add(upgradesButton);
		}
		
		this.openInstructions();
		
		this.needsRedraw = true;
	}
	
	public void openInstructions()
	{
		this.guiWindow = new GUIWindow(GUIWindowType.REGULAR);
		this.guiWindow.setInstructions();
		this.guiWindow.setPos(ReferencePositions.CENTER, new Vector2D((int)(ViewCamera.renderRes.x - 1)/2, (int)(ViewCamera.renderRes.y - 1)/2));
		this.guiWindow.textColor = Color.WHITE;
		this.needsRedraw = true;
	}
	
	public void openMenu()
	{
		this.guiWindow = new GUIWindow(GUIWindowType.REGULAR);
		this.guiWindow.setMenu();
		this.guiWindow.setPos(ReferencePositions.CENTER, new Vector2D((int)(ViewCamera.renderRes.x - 1)/2, (int)(ViewCamera.renderRes.y - 1)/2));
		this.guiWindow.textColor = Color.WHITE;
		this.needsRedraw = true;
	}
	
	public void openUpgrades()
	{
		if  (SpriteList.getPlayerShip() == null)
			return;
		this.guiWindow = new GUIWindow(GUIWindowType.REGULAR);
		this.guiWindow.setUpgrades();
		this.guiWindow.setPos(ReferencePositions.CENTER, new Vector2D((int)(ViewCamera.renderRes.x - 1)/2, (int)(ViewCamera.renderRes.y - 1)/2));
		this.guiWindow.textColor = Color.WHITE;
		this.needsRedraw = true;
	}
	
	public void openGameWin()
	{
		Game.primaryModel.mv.paused = true;
		this.guiWindow = new GUIWindow(GUIWindowType.REGULAR);
		this.guiWindow.setGameWin();
		this.guiWindow.setPos(ReferencePositions.CENTER, new Vector2D((int)(ViewCamera.renderRes.x - 1)/2, (int)(ViewCamera.renderRes.y - 1)/2));
		this.guiWindow.textColor = Color.WHITE;
		this.needsRedraw = true;
	}
	
	public void openGameLose()
	{
		Game.primaryModel.mv.paused = true;
		this.guiWindow = new GUIWindow(GUIWindowType.REGULAR);
		this.guiWindow.setGameLose();
		this.guiWindow.setPos(ReferencePositions.CENTER, new Vector2D((int)(ViewCamera.renderRes.x - 1)/2, (int)(ViewCamera.renderRes.y - 1)/2));
		this.guiWindow.textColor = Color.WHITE;
		this.needsRedraw = true;
	}
	
	public void updateGUIIndicators()
	{
		
			this.guiBars.clear();
			this.guiTexts.clear();
			
			double currShield = 0, shieldMax = 0;
			double currHealth = 0, healthMax = 0;
			double currEnergy = 0, energyMax = 0;
			int player_y = 0, gameLength = 8000;
			
			PlayerShip ship = SpriteList.getPlayerShip();
			if (ship != null)
			{
				currShield = ship.shield;
				shieldMax = ship.shieldMax;
				
				currHealth = ship.health;
				healthMax = ship.healthMax;
				
				currEnergy = ship.energy;
				energyMax = ship.getNextUpgradeCost();
				
				player_y = (int)(gameLength - ship.pos.y);
			}
			
			//ADD progress indicator
			GUIBar progressBar = new GUIBar(player_y, gameLength, 400, 20, Color.GRAY);
			progressBar.setPos(ReferencePositions.BOTTOM_LEFT, new Vector2D(245, 5));
			this.guiBars.add(progressBar);
			
			//Add energy indicator
			GUIBar energyBar = new GUIBar((int)Math.min(currEnergy, energyMax), (int)energyMax, 120, 20, Color.YELLOW);
			energyBar.setPos(ReferencePositions.BOTTOM_LEFT, new Vector2D(5, 400));
			this.guiBars.add(energyBar);
			MenuText energyText = new MenuText("Energy: "+ (int)(currEnergy));
			energyText.setPos(ReferencePositions.BOTTOM_LEFT, new Vector2D(135, 420));
			energyText.setTextColor(Color.YELLOW);
			this.guiTexts.add(energyText);
			
			//Add shield indicators
			GUIBar shieldBar = new GUIBar((int)currShield, (int)shieldMax, 120, 20, Color.BLUE);
			shieldBar.setPos(ReferencePositions.BOTTOM_LEFT, new Vector2D(5, 430));
			this.guiBars.add(shieldBar);
			MenuText shieldText = new MenuText(Math.round((float)currShield / (float)shieldMax * 100) + "%");
			shieldText.setPos(ReferencePositions.BOTTOM_LEFT, new Vector2D(135, 450));
			shieldText.setTextColor(Color.BLUE);
			this.guiTexts.add(shieldText);
			
			//Add health bar
			GUIBar healthBar = new GUIBar((int)currHealth, (int)healthMax, 120, 20, Color.RED);
			healthBar.setPos(ReferencePositions.BOTTOM_LEFT, new Vector2D(5, 460));
			this.guiBars.add(healthBar);
			MenuText healthText = new MenuText(Math.round((float)currHealth / (float)healthMax * 100) + "%");
			healthText.setPos(ReferencePositions.BOTTOM_LEFT, new Vector2D(135, 480));
			healthText.setTextColor(Color.RED);
			this.guiTexts.add(healthText);
		
		//this.needsRedraw = true;
	}
	
	
	@Override
	public void draw(Graphics2D g2)
	{
		//synchronized (this.imageLock)
		//{
//			if (this.needsRedraw || this.currentImage == null)
			{
				if (!this.guiBars.isEmpty())
					updateGUIIndicators();
				
				//GameGUI is always the size of the screen
				//this.currentImage = new BufferedImage((int)ViewCamera.renderRes.x - 1, (int)ViewCamera.renderRes.y - 1, 
				//		BufferedImage.TYPE_INT_ARGB);
				//Graphics2D c2 = this.currentImage.createGraphics();
				Graphics2D c2 = g2;
				
				//Draw main menu background
				if (Game.primaryModel.mv.getGameState() == GameState.MAIN_MENU)
				{
					c2.drawImage(ResourceLoader.getBufferedImage("backgrounds/main_menu_background.jpg"), 0, 0, null);
				}
				
				//Draw Buttons
				synchronized(this.guiButtons)
				{
					for (ActionButton curr : this.guiButtons)
						curr.draw(c2);
				}
				//Draw Text
				synchronized(this.guiTexts)
				{
					for (MenuText curr : this.guiTexts)
						curr.draw(c2);
				}
				//Draw GUI Bars
				synchronized(this.guiBars)
				{
					for (GUIBar curr : this.guiBars)
						curr.draw(c2);
				}
				
				
				//Draw open window (if applicable)
				if (this.guiWindow != null)
					this.guiWindow.draw(c2);
				
				this.needsRedraw = false;
			}
			
			//super.drawStatic(g2);
		//}
		
	}
	
	public void closeWindow()
	{
		this.guiWindow = null;
	}
	
	public void mouseDown(MouseEvent e, Vector2D position)
	{
		Vector2D mousePoint = position;
		
		synchronized(this.guiButtons)
		{
			for (ActionButton curr : guiButtons)
				if (curr.isWithin(mousePoint) && !curr.isDisabled())
				{
					curr.startClick();
					this.currentButton = curr;
					this.needsRedraw = true;
					break;
				}
		}
		
		if (this.guiWindow != null)
			if (this.guiWindow.mouseDown(e, position))
				this.needsRedraw = true;
	}
	
	public void mouseUp(MouseEvent e, Vector2D position)
	{
		Vector2D mousePoint = position;
		
		synchronized(this.guiButtons)
		{
			if (this.currentButton != null)
			{
				if (this.currentButton.isWithin(mousePoint))
					this.currentButton.finishClick();
				else
					this.currentButton.cancelClick();
				
				this.needsRedraw = true;
				
				this.currentButton = null;
			}
		}
		
		if (this.guiWindow != null)
			if (this.guiWindow.mouseUp(e, position))
				this.needsRedraw = true;
	}
	
	public void mouseScroll(MouseWheelEvent e, Vector2D position)
	{
		if (this.guiWindow != null)
			if (this.guiWindow.mouseScroll(e, position))
				this.needsRedraw = true;
	}
	
	public void mouseDrag(MouseEvent e, Vector2D position)
	{
		if (this.guiWindow != null)
			if (this.guiWindow.mouseDrag(e, position))
				this.needsRedraw = true;
	}
	
	public void mouseMove(MouseEvent e, Vector2D position)
	{
		Vector2D mousePoint = position;
		
		synchronized(this.guiButtons)
		{
			for (ActionButton curr : this.guiButtons)
			{
				if (curr.isWithin(mousePoint))
				{
					if (!curr.isDisabled() && curr.getState() != GUIButtonStates.ACTIVE)
					{
						curr.setState(GUIButtonStates.HOVER);
						this.needsRedraw = true;
						break;
					}
				}
				else if (curr.getState() == GUIButtonStates.HOVER)
				{
					curr.setState(GUIButtonStates.NORMAL);
					this.needsRedraw = true;
				}
			}
		}
		
		if (this.guiWindow != null)
			if (this.guiWindow.mouseMove(e, position))
				this.needsRedraw = true;
	}
	
	public void keyPress(KeyEvent e)
	{
		if (this.guiWindow != null)
			if (this.guiWindow.keyPress(e))
				this.needsRedraw = true;
	}
}
