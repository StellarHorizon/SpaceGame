/***************************
 * Purpose: ButtonController class containing
 * a list of static actions that ActionButtons
 * can reference. Each ActionButton has an enum
 * corresponding to the action it will perform,
 * while this class matches the enum action to
 * the corresponding code.
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

public class ButtonController
{
	public static Model model;
	
	public ButtonController(Model m)
	{
		ButtonController.model = m;
	}
	
	public static void doAction(GUIButtonActions action, ActionButton button)
	{
		switch (action)
		{
			case START_GAME:
				model.mv.setGameState(GameState.GAME);
				break;
			case MAIN_MENU:
				model.mv.setGameState(GameState.MAIN_MENU);
				break;
			case TOGGLE_PAUSED:
				model.mv.paused = !model.mv.paused;
				break;
			case OPEN_MENU:
				SpriteList.getGUI().openMenu();
				model.mv.paused = true;
				break;
			case OPEN_UPGRADES:
				SpriteList.getGUI().openUpgrades();
				model.mv.paused = true;
				break;
			case CLOSE_WINDOW:
				SpriteList.getGUI().closeWindow();
				model.mv.paused = false;
				break;
			case UPGRADE_SHIP:
				model.mv.gameMap.upgradePlayerShip();
				SpriteList.getGUI().openUpgrades();
				break;
			case QUIT_GAME:
				Game.exitGame();
			default:
				break;
			
		}
		return;
	}
}
