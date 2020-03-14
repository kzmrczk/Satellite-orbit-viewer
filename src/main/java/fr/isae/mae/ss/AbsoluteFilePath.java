package fr.isae.mae.ss;

import java.io.File;

/**
 * 
 * @author mkyong at https://mkyong.com/java/how-to-get-the-filepath-of-a-file-in-java/
 * modified and commented by: Dawid Kazimierczak
 *
 * Method getPath is used to find the location of a specific file. It takes
 * the filename as an argument and returns its path.
 * In case of OrbitViewer1000 the method is used to find "orekit-data-master.zip"
 */

public class AbsoluteFilePath
{
    public static String getPath(String args)
    {	
    	// Creating a File object with a specific name
    	File orekit = new File(args);
    	
		// Getting the absolute path to that file
		String absolutePath = orekit.getAbsolutePath();
		
		// Cutting the part after the last separator from the path
		String filePath = absolutePath.
			     substring(0,absolutePath.lastIndexOf(File.separator));
			
		return filePath;    	
    }
}