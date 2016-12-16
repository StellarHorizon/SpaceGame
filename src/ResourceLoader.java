/***************************
 * Purpose: Static ResourceLoader class handling
 * the loading of all resources from the file system.
 * 
 * Currently supports BufferedImage loading in
 * resources directory. Future versions will also
 * handle JSON data file parsing.
 *
 * Contributors:
 * - Zachary Johnson
 ***************************/

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.TreeMap;
import javax.imageio.ImageIO;

public class ResourceLoader
{
	private static TreeMap<String, BufferedImage> resources = new TreeMap<String, BufferedImage>();
	private static BufferedImage testImage;
	private static String directoryPath = System.getProperty("user.dir") + "\\src\\resources\\";
	//private static String directoryPath = "\\resources\\";
	
	public ResourceLoader()
	{
//		directoryPath = "/resources/";
//		
//		initialize();
	}
	
	public ResourceLoader(String path)
	{
//		directoryPath = path;
//		
//		initialize();
	}
	
	public static void initialize()
	{
		//Load default/test image
		try
		{
			testImage = loadBufferedImage(directoryPath + "debug\\TestTriangle.png");
		}
		catch (IOException e)
		{
			System.out.println("Warning: Unable to load debug placeholder image: " + directoryPath + "debug/TestTriangle.png");
			e.printStackTrace();
		}
		
		//loadAllResources(directoryPath);
	}
	
	//Recursively load all BufferedImages within the resources directory
	public static void loadAllResources(String path)
	{
		if (path == null)
			return;
		else if (Game.isDebugging(GameDebugVars.VERBOSE_IMAGE_LOADING))
			System.out.println("ResourceLoader: Searching directory: " + path);
		
		File[] dirFiles = new File(path).listFiles();
		if (dirFiles.length == 0)
			return;
		
		for(File currFile : dirFiles)
		{
			//Only load images
			if (currFile.isFile() && (currFile.getName().endsWith(".png") || (currFile.getName().endsWith(".jpg"))))
			{
				Image target = null;
				try
				{
					//System.out.println(path.substring(directoryPath.length()) + "\\" + currFile.getName());
					target = loadBufferedImage(currFile.getAbsolutePath());
				}
				catch (IOException e)
				{
					if (Game.isDebugging(GameDebugVars.VERBOSE_IMAGE_LOADING))
						System.out.println("ResourceLoader: Unable to load image: " + currFile.getAbsolutePath());
					continue;
				}
				if (Game.isDebugging(GameDebugVars.VERBOSE_IMAGE_LOADING))
					System.out.println("ResourceLoader: Successfully loaded: " + currFile.getName());
				resources.put(currFile.getName(), (BufferedImage) target);
					
			}
			else if (currFile.isDirectory())
			{
				loadAllResources(currFile.getAbsolutePath());
			}
		}
	}
	
	//Return the image saved in memory, or load from file if not loaded
	public static Image getImage(String imageName)
	{
		Image target = null;
		
		//First, see if the desired image is already loaded
		if (resources.containsKey(imageName))
			target = resources.get(imageName);
		
		if (target != null)
			return target;
		
		//If image not found, attempt to load from file, add to loaded resources
		try
		{
			target = loadImage(directoryPath + imageName);
			if (target != null)
			{
				resources.put(imageName, (BufferedImage) target);
				return target;
			}
		}
		catch (IOException e)
		{
			System.out.println("Could not find image: " + directoryPath + imageName);
			e.printStackTrace();
		}
		
		//If desired image could not be loaded, use placeholder image
		return testImage;
		
	}
	public static BufferedImage getBufferedImage(String imageName)
	{
		return (BufferedImage) getImage(imageName);
	}
	
	private static Image loadImage(String filename) throws IOException
	{
		Image img = ImageIO.read(new FileInputStream(filename));
		
		return img;
	}
	private  static BufferedImage loadBufferedImage(String filename) throws IOException
	{
		return (BufferedImage) loadImage(filename);
	}
}
