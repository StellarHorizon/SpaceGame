/***************************
 * Purpose: Game class, starting location for the
 * program. Initializes and configures core game
 * values and builds game loop to run model
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.Timer;

import javax.sound.sampled.*;

@SuppressWarnings("serial")
public class Game extends JFrame implements ActionListener
{
	//Master toggle for debug mode
	//Specific debugging functionality toggled in GameDebugVars
	public static final boolean DEBUG = true;
	
	private static AtomicBoolean Updating = new AtomicBoolean(false);
	
	Model model;
	public static Model primaryModel;
	View view;
	ButtonController buttonController;

	public Game() throws Exception
	{
		ResourceLoader.initialize();
		
		ViewCamera.windowDim = new Vector2D(800, 600);
		ViewCamera.pos = ViewCamera.windowDim.divide(new Vector2D(2.0,2.0));
		
		this.model = new Model();
		primaryModel = this.model;
		this.view = new View(this.model);
		
		//Set up Java window
		this.setTitle("Asteroid Escape");
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(MAXIMIZED_BOTH);
		//this.setUndecorated(true);
		this.setVisible(true);
		
		
		//Setup Model and Controller and Create View
		this.buttonController = new ButtonController(this.model);
		Controller controller = new Controller(this.model);
		this.addKeyListener(controller);
		
		
		ViewCamera.windowDim = new Vector2D(this.getWidth(), this.getHeight());
		ViewCamera.pos = ViewCamera.windowDim.divide(new Vector2D(2.0,2.0));
		
		//Setup View
		view.addMouseListener(controller);
		view.addMouseMotionListener(controller);
		view.addMouseWheelListener(controller);
		
		Game.music();
		
		new Timer((int) (1000.0 / GameConstant.goalFrameRate), this).start(); // Indirectly calls actionPerformed at regular intervals
	}

	public void actionPerformed(ActionEvent evt)
	{
		if (!Game.Updating.compareAndSet(false, true))
		{
			return;
		}
		
		ViewCamera.windowDim.x = this.view.getWidth();
		ViewCamera.windowDim.y = this.view.getHeight();
		
		ViewCamera.renderScale =  Math.max(Math.min(ViewCamera.windowDim.x/ViewCamera.renderRes.x, ViewCamera.windowDim.y/ViewCamera.renderRes.y),1);
		
		ViewCamera.scalingOffset = ViewCamera.windowDim.subtract(ViewCamera.renderRes.multiply(ViewCamera.renderScale)).divide(2);
		ViewCamera.scalingOffset.x = Math.max(ViewCamera.scalingOffset.x, 0);
		ViewCamera.scalingOffset.y = Math.max(ViewCamera.scalingOffset.y, 0);
		
		
		//Calculate current frame rate
		/*if (lastFrameTime == 0)
			lastFrameTime = System.currentTimeMillis();
		else
		{
			GameDebugVars.frameRate = 1000.0 / (System.currentTimeMillis() - lastFrameTime);
			lastFrameTime = System.currentTimeMillis();
		}*/
		
		//Call generic update function of the model
		try {
			this.model.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Thread GameUpdateThread = new Thread(new GameAdvanceThread(),"Game Update Thread.");
		//GameUpdateThread.start();

		//long paintStartTime = System.currentTimeMillis();
		repaint(); // Indirectly calls View.paintComponent
		//GameDebugVars.paintsPerSecond = 1000.0 / (System.currentTimeMillis() - paintStartTime);
		
		/*try {
			GameUpdateThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		
		Game.Updating.set(false);
	}

	public static void main(String[] args) throws Exception
	{
		new Game();
	}
	
	public static void music() throws FileNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException 
    {       
		/*AudioPlayer MGP = AudioPlayer.player;  
        AudioStream BGM;  
        AudioData MD;  
        ContinuousAudioDataStream loop = null;  

        try {  
            BGM = new AudioStream(new FileInputStream(System.getProperty("user.dir") + "\\src\\resources\\music\\Neon_Transit_G.mp3"));  
            MD = BGM.getData();  
            loop = new ContinuousAudioDataStream(MD);  
        } catch(IOException error)  {  
        	System.out.print(error.toString()); 
        	
        }  
        MGP.start(loop);  */
		
		FileInputStream filestream = new FileInputStream(System.getProperty("user.dir") + "\\src\\resources\\music\\Neon_Transit_G.wav");
		BufferedInputStream mystream = new BufferedInputStream(filestream);
		AudioInputStream ais = AudioSystem.getAudioInputStream(mystream);
		//AudioInputStream ais = AudioSystem.getAudioInputStream(new FileInputStream(System.getProperty("user.dir") + "\\src\\resources\\music\\Neon_Transit_G.mp3"));
		//AudioInputStream ais = AudioSystem.getAudioInputStream(new URL(System.getProperty("user.dir") + "\\src\\resources\\music\\Neon_Transit_G.mp3"));
		Clip clip = AudioSystem.getClip();
		clip.open(ais);
		clip.start();
		//clip.loop(Clip.LOOP_CONTINUOUSLY);
		/*if (clip != null)
		{
			new Thread() 
			{
                public void run() 
                {
                    synchronized (clip) 
                    {
                        clip.stop();
                        clip.setFramePosition(0);
                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                    }
                }
            }.start();
		}*/
		
    }
	
	//If both in debug mode and debugging type passed in is true, return true
	public static boolean isDebugging(boolean debugTypeEnabled)
	{
		if (DEBUG && debugTypeEnabled)
			return true;
		else
			return false;
	}
	
	public static void exitGame()
	{
		System.exit(0);
	}
}