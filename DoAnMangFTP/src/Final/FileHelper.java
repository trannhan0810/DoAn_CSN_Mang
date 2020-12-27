package Final;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {
	//CREATE FOLDER
	public static boolean createFolder(String path) {
		boolean isDone = new File(path).mkdirs();
		return isDone;
	};
	
	
	//DELETE FILE|UNEMPTY FOLDER
	private static void deleteFile(File file) {
		try {
			if(file.isFile())	file.delete();
			else {
				for(File subFile:file.listFiles()) deleteFile(subFile);
				file.delete();
			}
		} catch (Exception e1) {
            System.out.println("Unable to delete this path :"+ file);
        }
	}
	public static boolean deleteFile(String path) {
		File file = new File(path);
		deleteFile(file);
		boolean isDelete = !file.exists();
		return isDelete;
	};
	
	//RENAME FILE|FOLDER
	public static boolean renameFile(String path, String newName) {
		Path source = Paths.get(path);
		try{
			Files.move(source, source.resolveSibling(newName));
			return true;
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
	}
	
}
