/***************************
 * Purpose: Model class of the Model-View-Controller
 * paradigm; core game model starts in this class
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



class Model
{
	int tick = 0;
	public ModelVars mv;
	
	/***************************
	 * Constructor
	 ***************************/
	Model() throws IOException
	{
		//Set up class containing model variables
		this.mv = new ModelVars(this);
		
		//Set up basic resources
		this.mv.rand = new Random(mv.seed);
		
		this.mv.setGameState(GameState.MAIN_MENU);
		
		
	}

	/***************************
	 * Called when game state changes to main menu
	 ***************************/
	public void init_MainMenu()
	{
		//mv.mainMenu = new FullScreenMenu();
	}
	 
	 /***************************
	 * Called when game state changes to the game
	 ***************************/
	public void init_Game()
	{		
		mv.gameMap.loadMap(GameMap.MapType.DEMO);
		
		return;
	}
	
	public void gameUpdate() throws Exception
	{
		if (this.mv.paused)
			return;
		
		mv.gameMap.updateMap();
	}
	
	/***************************
	 * Called when game is changing states
	 * Allows model to gain control before the resources are disposed of
	 * Returns false to cancel the state change, true otherwise
	 ***************************/
	public boolean leaveCurrentState()
	{
		//No special behavior needed yet
		return true;
	}
	
	//Called by ModelVar class after variables initialized
	public void initGameState()
	{
		switch (mv.getGameState())
		{
			case MAIN_MENU:
				init_MainMenu();
				break;
			case GAME:
				init_Game();
				break;
		}
	}
	
	//Update function called on each tick
	public void update() throws Exception
	{
		switch (mv.getGameState())
		{
			case MAIN_MENU:
				break;
			case GAME:
				gameUpdate();
				break;
			default:
				break;
		}
	}

	//Function called when left mouse button is clicked
	public void onLeftClick(Vector2D point){}
	
	//Function called when right mouse button is clicked
	public void onRightClick(Vector2D point){}
	
	//Function called when left mouse button is released
	public void onLeftClickRelease(Vector2D point){}
	
	//Function called when right mouse button is released
	public void onRightClickRelease(Vector2D point){}
	
	
}

