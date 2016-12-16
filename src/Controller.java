/***************************
 * Purpose: Controller class, takes input from the user
 * and sends actions to the Model
 *
 * Contributors:
 *  - Zachary Johnson
 *  - Derek Paschal
 ***************************/

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

class Controller implements MouseListener, MouseWheelListener, MouseMotionListener, KeyListener
{
	Model model;
	public static Vector2D mousePos = new Vector2D(0,0);
	boolean draggingMap = false, draggingWindow = false;
	boolean upPressed = false, downPressed = false, leftPressed = false, rightPressed = false, spacePressed = false;

	Controller(Model m)
	{
		this.model = m;
	}
	
	private Vector2D GetPosition(MouseEvent e)
	{
		return new Vector2D((e.getX()-ViewCamera.scalingOffset.x)/ViewCamera.renderScale, (e.getY()-ViewCamera.scalingOffset.y)/ViewCamera.renderScale);
	}

	public void mousePressed(MouseEvent e)
	{
		Vector2D position = GetPosition(e);
		model.mv.gui.mouseDown(e, position);
		switch (model.mv.getGameState())
		{
			case MAIN_MENU:
				break;
			case GAME:		
				if (e.getButton() == MouseEvent.BUTTON1)
					this.model.onLeftClick(position);
				if (e.getButton() == MouseEvent.BUTTON3)
					this.model.onRightClick(position);
				break;
			default:
				break;
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		Vector2D position = GetPosition(e);
		model.mv.gui.mouseUp(e, position);
		switch (model.mv.getGameState())
		{
			case MAIN_MENU:
				break;
			case GAME:
				if (e.getButton() == MouseEvent.BUTTON1)
					this.model.onLeftClickRelease(new Vector2D(e.getX(), e.getY()));
				if (e.getButton() == MouseEvent.BUTTON3)
					this.model.onRightClickRelease(new Vector2D(e.getX(), e.getY()));
				
				draggingMap = false;
				draggingWindow = false;
				break;
			default:
				break;
		}
	}
	
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		Vector2D position = GetPosition(e);
		model.mv.gui.mouseScroll(e, position);
		switch (model.mv.getGameState())
		{
			case MAIN_MENU:
				break;
			case GAME:
				break;
			default:
				break;
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{
		Vector2D position = GetPosition(e);
		model.mv.gui.mouseDrag(e, position);
		switch (model.mv.getGameState())
		{
			case MAIN_MENU:
				break;
			case GAME:
				Controller.mousePos = new Vector2D(e.getX(), e.getY());
				break;
			default:
				break;
		}
	}
	
	public void mouseMoved(MouseEvent e)
	{
		synchronized (SpriteList.SpriteLock)
		{
			if (model.mv.gui == null)
				return;
			
			Vector2D position = GetPosition(e);
			model.mv.gui.mouseMove(e, position);
			switch (model.mv.getGameState())
			{
				case MAIN_MENU:
					break;
				case GAME:
					Controller.mousePos = position;
					break;
				default:
					break;
			}
		}
	}
	
	public void mouseClicked(MouseEvent e) {    }
	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void keyPressed(KeyEvent e)
	{
		synchronized (SpriteList.SpriteLock)
		{
			if (SpriteList.getPlayerShip() == null)
				return;
			model.mv.gui.keyPress(e);
			switch (model.mv.getGameState())
			{
				case MAIN_MENU:
					break;
				case GAME:
					int keyCode = e.getKeyCode();
					
					switch (keyCode)
					{
						case KeyEvent.VK_UP:
							this.upPressed = true;
							SpriteList.getPlayerShip().forward = true;
							break;
						case KeyEvent.VK_DOWN:
							this.downPressed = true;
							SpriteList.getPlayerShip().backward = true;
							break;
						case KeyEvent.VK_LEFT:
							this.leftPressed = true;
							if(!SpriteList.getPlayerShip().left)
							{
								SpriteList.getPlayerShip().left = true;
							}
							break;
						case KeyEvent.VK_RIGHT:
							this.rightPressed = true;
							if (!SpriteList.getPlayerShip().right)
							{
								SpriteList.getPlayerShip().right = true;
							}
							break;
						case KeyEvent.VK_SPACE:
							this.spacePressed = true;
							SpriteList.getPlayerShip().firing = true;
							break;
					}
					break;
				default:
					break;
			}
		}
	}
	public void keyReleased(KeyEvent e)
	{
		synchronized (SpriteList.SpriteLock)
		{
			if (SpriteList.getPlayerShip() == null)
				return;
			
			int keyCode = e.getKeyCode();
			switch (model.mv.getGameState())
			{
				case MAIN_MENU:
					break;
				case GAME:
					switch (keyCode)
					{
						case KeyEvent.VK_UP:
							this.upPressed = false;
							SpriteList.getPlayerShip().forward = false;
							break;
						case KeyEvent.VK_DOWN:
							this.downPressed = false;
							SpriteList.getPlayerShip().backward = false;
							break;
						case KeyEvent.VK_LEFT:
							this.leftPressed = false;
							SpriteList.getPlayerShip().left = false;
							break;
						case KeyEvent.VK_RIGHT:
							this.rightPressed = false;
							SpriteList.getPlayerShip().right = false;
							break;
						case KeyEvent.VK_SPACE:
							this.spacePressed = false;
							SpriteList.getPlayerShip().firing = false;
							break;
					}
					break;
				default:
					break;
			}
		}
	}
	public void keyTyped(KeyEvent k) {    }

}
