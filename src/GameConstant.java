/***************************
 * Purpose: GameConstant class, centralized location
 * to view critical game values for quick changes
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.awt.Color;

public class GameConstant
{
	/***************************/
	//Global Variables
	/***************************/
	public static final boolean DEBUG = true;
	public static final Color SLATE_GRAY = new Color(112,128,144);
	
	/***************************/
	//Model Variables
	/***************************/
	public static final int gameSeed = 10;
	public static final int goalFrameRate = 30; //in FPS
}
