/***************************
 * Purpose: ModelVars class to contain primary
 * variables of the model class. Note that functionality
 * in this class is oriented towards managing variables
 * themselves, which model handles behavior of the variables,
 * and the model may add to or remove from lists.
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.util.Random;

public class ModelVars
{
	/***************************
	 * Constructor
	 ***************************/
	public ModelVars(Model model)
	{
		this.m = model;
		
		this.gui = new GameGUI();
		SpriteList.add(this.gui);
		this.gameState = GameState.MAIN_MENU;
	}
	
	/***************************
	 * General Public Variables
	 ***************************/
	public Model m;
	Random rand;
	int seed = GameConstant.gameSeed;
	
	
	/***************************
	 * MAIN MENU Public Variables
	 ***************************/
	public GameGUI gui;
	
	
	/***************************
	 * MAIN MENU Private Variables
	 ***************************/
	private GameState gameState;
	
	public GameState getGameState() { return gameState; }
	public void setGameState(GameState newState)
	{
		//Request state change from model
		//Model can cancel state change by returning false
		if (!m.leaveCurrentState())
			return;
		
		//Dispose of resources for current state
		switch (gameState)
		{
			case MAIN_MENU:
				break;
			case GAME:
				this.gameMap.removeAllSprites();
				//SpriteList.clearPlayerShip();
				this.gameMap = null;
				break;
			default:
				break;
		}
		
		//Initialize resources for new state
		switch (newState)
		{
			case MAIN_MENU:
				this.gui.setMainMenu();
				break;
			case GAME:
				this.gui.setGame();
				this.gameMap = new GameMap(Game.primaryModel);
				this.paused = true;
				break;
			default:
				break;
		}
		
		//Change the GameState
		this.gameState = newState;
		
		//Inform model to prepare any resources
		m.initGameState();
	}
	
	
	/***************************
	 * GAME Public Variables
	 ***************************/
	GameMap gameMap;
	boolean paused;
	
	/***************************
	 * GAME Private Variables
	 ***************************/
}
