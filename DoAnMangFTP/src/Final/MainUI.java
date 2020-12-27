package Final;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

@SuppressWarnings("serial")
public class MainUI extends BaseUI{
	FTPUtil ftpUtil;

	public static void main(String[] args) {
		MainUI ui = new MainUI();
		ui.setVisible(true);

	}

	private MainUI() {
		super();
		ftpUtil = new FTPUtil();
		initView();
		addListener();
	}
	
	private void initView() {
		showLocalDirTree();
	}
	
	private void addListener() {
		addConnectButtonListener();
		addLocalTreeListener();
		addLocalNewButtonListener();
		addLocalDeleteButtonListener();
		addLocalRenameButtonListener();
		addRemoteTreeListener();
		addRemoteNewButtonListener();
		addRemoteDeleteButtonListener();
		addRemoteRenameButtonListener();
		addUploadButtonListener();
		addDownloadButtonListener();
	}


	@SuppressWarnings("unused")
	private void addConnectButtonListener() {
		this.btnConnect.addActionListener((ActionEvent e) -> {
			String host = txtHost.getText();
			int port = Integer.parseInt(txtPort.getText());
			String user = txtUser.getText();
			@SuppressWarnings("deprecation")
			String pass = txtPass.getText();
			
			boolean isConneted = ftpUtil.connect(host, port);
			txtLogger.append(ftpUtil.getReply());
			if(!isConneted) return;
			
			boolean isLogin = ftpUtil.login(user, pass);
			txtLogger.append(ftpUtil.getReply());
			if(isLogin) showRemoteDirTree();
		});
	}
	
	private void addLocalTreeListener() {
		this.localDirTree.addTreeSelectionListener((TreeSelectionEvent e) -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) localDirTree.getLastSelectedPathComponent();
			if(node!=null) {
				//Get selected file but ignore if file name is This PC
				File seletedFile = (File)node.getUserObject();
				if(seletedFile.toString().equals("This PC")) return;
				//If it is folder
	        	if (seletedFile.isDirectory()) {	
	        		//Get child file and folder
	        		File[] subItems = seletedFile.listFiles();
	        		if(subItems == null) subItems = new File[0];
	        		//Add child node to file tree
	        	    for (File file : subItems) {
	        	    	if(file.isFile()) continue;
	        	    	DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file) {
	        	    		public String toString() {
	        					return file.getName();
	        	    		}
	        	    	};
	        	    	node.add(childNode);
	        	    }
	        	    //Set directory path
	        		this.txtLocalDirPath.setText(seletedFile.toString()); 
	        		//Expand tree 
	        	    this.localDirTree.expandPath(new TreePath(node.getPath()));
	        	    //Show child file and folder
	        	    this.listLocalFile.setListData(subItems);
	            }          	
			}
		});		
	}
	
	private void addLocalNewButtonListener() {
		this.btnLocalNew.addActionListener((ActionEvent e) -> {
			String folderName = (String)JOptionPane.showInputDialog(null, "Enter new folder name: ");
			String path = this.txtLocalDirPath.getText();
			boolean isDone = FileHelper.createFolder(path+"/"+folderName);
			if(isDone) showLocalDirTree();
		});
	}
	
	private void addLocalDeleteButtonListener() {
		this.btnLocalDelete.addActionListener((ActionEvent e) -> {
			File selectedFile = (File)this.listLocalFile.getSelectedValue();
			if(selectedFile == null) selectedFile = new File(this.txtLocalDirPath.getText());
			System.out.println(selectedFile);
			boolean isDelete = FileHelper.deleteFile(selectedFile.getPath());
			showLocalDirTree();
		});
	}
	
	private void addLocalRenameButtonListener() {
		this.btnLocalRename.addActionListener((ActionEvent e) -> {
			File selectedFile = (File)this.listLocalFile.getSelectedValue();
			if(selectedFile == null) selectedFile = new File(this.txtLocalDirPath.getText());
			
			String newName = (String)JOptionPane.showInputDialog(null, "Enter new name: ");
			if(newName == null) return;
			boolean isRename = FileHelper.renameFile(selectedFile.getPath(), newName);
			showLocalDirTree();
		});
	}
	
	private void addRemoteTreeListener() {
		this.remoteDirTree.addTreeSelectionListener((TreeSelectionEvent e) -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) remoteDirTree.getLastSelectedPathComponent();
			if(node!=null) {
				//Get selected file but ignore if file name is This PC
				if(node.toString().equals("/")) {
					this.txtRemoteDirPath.setText("/"); 
					FTPFile[] childFile = ftpUtil.listFiles("/");
					this.listRemoteFile.setListData(childFile);
					return;
				}
				FTPFile seletedFile = (FTPFile)node.getUserObject();
				//Get path
				String path = "";
				Object elements[] = node.getPath();
               	for (int i = 1, n = elements.length; i < n; i++)          
               		path+="/" + elements[i];
				//If it is folder
	        	if (seletedFile.isDirectory()) {	
	        		//Get child file and folder
	        		FTPFile[] subItems = ftpUtil.listFiles(path);
	        		if(subItems == null) subItems = new FTPFile[0];
	        		//Add child node to file tree
	        	    for (FTPFile file : subItems) {
	        	    	if(file.isFile()) continue;
	        	    	DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file) {
	        	    		public String toString() {
	        					return file.getName();
	        	    		}
	        	    	};
	        	    	node.add(childNode);
	        	    }
	        	    //Set directory path
	        		this.txtRemoteDirPath.setText(path); 
	        		//Expand tree 
	        	    this.remoteDirTree.expandPath(new TreePath(node.getPath()));
	        	    //Show child file and folder
	        	    this.listRemoteFile.setListData(subItems);
	            }          	
			}
		});		
	}
	
	private void addRemoteNewButtonListener() {
		this.btnRemoteNew.addActionListener((ActionEvent e) -> {
			String folderName = (String)JOptionPane.showInputDialog(null, "Enter new folder name: ", "Create new folder", JOptionPane.PLAIN_MESSAGE);
			String path = this.txtRemoteDirPath.getText();
			if(folderName==null) return;
			boolean isDone = ftpUtil.makeDirectory(path+"/"+folderName);
			if(isDone) showRemoteDirTree();
		});
	}
	
	private void addRemoteDeleteButtonListener() {
		this.btnRemoteDelete.addActionListener((ActionEvent e) -> {
			FTPFile selectedFile = (FTPFile)this.listRemoteFile.getSelectedValue();
			String path = this.txtRemoteDirPath.getText();
			String pathToDelete = (selectedFile==null)?path:path + "/" + selectedFile.getName();
			boolean isDelete;
			if(selectedFile==null||selectedFile.isDirectory())
				isDelete = ftpUtil.deleteDirectory(pathToDelete);
			else isDelete = ftpUtil.deleteFile(pathToDelete);
			showRemoteDirTree();
		});
	}
	
	private void addRemoteRenameButtonListener() {
		this.btnRemoteRename.addActionListener((ActionEvent e) -> {
			String newName = (String)JOptionPane.showInputDialog(null, "Enter new name: ", "Rename", JOptionPane.PLAIN_MESSAGE);
			if(newName == null) return;
			FTPFile selectedFile = (FTPFile)this.listRemoteFile.getSelectedValue();
			String path = this.txtRemoteDirPath.getText();
			String oldPath, newPath;
			if(selectedFile!=null) {
				oldPath = path + selectedFile.getName();
				newPath = path +  "/" + newName;
			} else {
				if(path=="/") return;
				oldPath = path;
				newPath = ftpUtil.getParentDirectory(path) + "/" + newName;
			}
			boolean isRename = ftpUtil.renameFile(oldPath,newPath);
			showRemoteDirTree();
		});
	}
	
	private void addUploadButtonListener() {
		this.btnUpload.addActionListener((ActionEvent e) -> {
			File localFile = (File)this.listLocalFile.getSelectedValue();
			String remotePath = this.txtRemoteDirPath.getText();
		
			boolean uploaded = false;
			if(localFile.isFile()) {
				uploaded = ftpUtil.uploadFile(localFile, remotePath);
			} else uploaded = ftpUtil.uploadDirectory(localFile, remotePath);
			showRemoteDirTree();
			//System.out.println(uploaded);
			if(uploaded) JOptionPane.showMessageDialog(this, "Upload successful");
			else JOptionPane.showMessageDialog(this, "Upload failed");
		});
	}
	
	private void addDownloadButtonListener() {
		this.btnDownload.addActionListener((ActionEvent e) -> {
			FTPFile downloadFile = (FTPFile)this.listRemoteFile.getSelectedValue();
			String remotePath = this.txtRemoteDirPath.getText() + "/" + downloadFile.getName();
			String localPath = this.txtLocalDirPath.getText();
			
			boolean downloaded = false;
			if(downloadFile.isFile()) {
				downloaded = ftpUtil.downloadFile(localPath, remotePath);
			} downloaded = ftpUtil.downLoadDirectoy(localPath, remotePath);
			showLocalDirTree();
			//System.out.println(downloaded);
			if(downloaded) JOptionPane.showMessageDialog(this, "Download successful");
			else JOptionPane.showMessageDialog(this, "Download failed");
		});
	}
		

	
	//For local tree 
	void showLocalDirTree() {
		File rootFile = new File("This PC");
		//Get all drives
		File[] drives = File.listRoots();
		//Init tree node
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootFile);
		DefaultTreeModel model = new DefaultTreeModel(root);
		//Add drives to the tree
		
		if (drives != null && drives.length > 0) {
		    for (File drive : drives) {
		    	DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(drive);
		    	root.add(childNode);
		    }
		}
		this.localDirTree.setModel(model);
		this.listLocalFile.setListData(new File[0]);
	}
	
	void showRemoteDirTree() {
		String rootFile = "/";
		//Get all drives
		FTPFile[] childFile = ftpUtil.listFiles("/");
		//Init tree node
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootFile);
		DefaultTreeModel model = new DefaultTreeModel(root);
		//Add drives to the tree
		
		if (childFile != null && childFile.length > 0) {
		    for (FTPFile file : childFile) {
		    	DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file) {
		    		public String toString() {
		    			return file.getName();
		    		}
		    	};
		    	root.add(childNode);
		    }
		}
		this.remoteDirTree.setModel(model);
		this.remoteDirTree.setEnabled(true);
		this.listRemoteFile.setListData(new FTPFile[0]);
	}
	

}
