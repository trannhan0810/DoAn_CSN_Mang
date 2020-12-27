package Final;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPUtil {
	
	public FTPClient ftpClient = new FTPClient();;
	
	String reply;
	public String getReply() {return reply;}
	private void getServerReply() {
		String[] replies = ftpClient.getReplyStrings();
		reply = "";
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                reply += "SERVER: " + aReply + "\n";
            }
        }
	}

	boolean connect(String host, int port) {
		try {
			ftpClient.connect(host, port);
			getServerReply();
			int replyCode = ftpClient.getReplyCode();
	        if (!FTPReply.isPositiveCompletion(replyCode)) {
	            return false;
	        } else return true;
		} catch (Exception err) {
			err.printStackTrace();
			return false;
		}
	};
	
	boolean login(String username, String password) {
		try {
			ftpClient.login(username, password);
			getServerReply();
			int replyCode = ftpClient.getReplyCode();
	        if (!FTPReply.isPositiveCompletion(replyCode)) {
	            return false;
	        } else {
	        	ftpClient.enterLocalPassiveMode();
	        	return true;
	        }
		} catch (Exception err) {return false;}
	}
	
	public FTPFile getFile(String path) {
		try {
			FTPFile result = this.ftpClient.mlistFile("/");
			System.out.println(result==null);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	};
	
	public FTPFile[] listFiles(String path) {
		try {
			return this.ftpClient.listFiles(path);
		} catch (IOException e) {
			return null;
		}
	}
	
	boolean changeWorkingDirectory(String pathname) {
		try {
		return ftpClient.changeWorkingDirectory(pathname);
		} catch(Exception e) {return false;}
	}
	
	boolean changeToParentDirectory() {
		try {
			return ftpClient.changeToParentDirectory();
		} catch(Exception e) {return false;}
	}
	
	public String getParentDirectory(String path) {
		changeWorkingDirectory(path);
		changeToParentDirectory();
		try {
			return ftpClient.printWorkingDirectory();
		} catch (IOException e) {return "";}
	}
	
	public boolean makeDirectory(String pathname){
		try {
			return ftpClient.makeDirectory(pathname);
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean deleteDirectory(String path) {
		try {
			FTPFile[] subFiles = ftpClient.listFiles(path);
			for(FTPFile file: subFiles) {
				if(file.getName().equals(".")||file.getName().equals("..")) continue;
				String filepath = path + '/' + file.getName();
				if(file.isDirectory()) deleteDirectory(filepath); 
				else deleteFile(filepath); 
			}
			return ftpClient.removeDirectory(path);
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteFile(String path) {
		try {
		return ftpClient.deleteFile(path);
		} catch (Exception e){ return false; }
	}
	
	public boolean renameFile(String oldName, String newName) {
		try {
			return ftpClient.rename(oldName, newName);
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean downloadFile(String localPath, String remoteFile) {
		try { 
			String[] s = remoteFile.split("/") ;
			String localFile = localPath + "/" + s[s.length-1];
			File downloadFile = new File(localFile);
			
			OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile));
			boolean success = ftpClient.retrieveFile(remoteFile, outputStream1);
			outputStream1.close();
			return success;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean uploadFile(File localFile, String remotePath) {
        InputStream inputStream;
		try {
			String remoteFile = remotePath + "/" + localFile.getName() + "/";
			inputStream = new FileInputStream(localFile);
			boolean done = ftpClient.storeFile(remoteFile, inputStream);
			inputStream.close();
			return done;
		} catch (Exception e) { e.printStackTrace();return false; }
	}
	
	public boolean downLoadDirectoy(String localPath, String remoteDir) {
		String[] s = remoteDir.split("/") ;
		String localDir = localPath + "/" + s[s.length-1];
		File downloadDir = new File(localDir);
		downloadDir.mkdirs();
		try {
			FTPFile[] files = ftpClient.listFiles(remoteDir);
			for(FTPFile file:files) {
				if(file.getName() == "." || file.getName() == "..");
				if(file.isDirectory()) downLoadDirectoy(localDir, remoteDir + "/" + file.getName());
				else downloadFile(localDir, remoteDir + "/" + file.getName());
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public boolean uploadDirectory(File localDir, String remotePath) {
		try {
			String dirName = localDir.getName();
			String uploadDir = remotePath + "/" + dirName;
			makeDirectory(uploadDir);
			for(File file: localDir.listFiles()) {
				if(file.getName() == "." || file.getName() == "..");
				if(file.isDirectory()) uploadDirectory(file, uploadDir);
				else uploadFile(file, uploadDir);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
