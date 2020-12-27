package aaaa;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.ScrollPane;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.net.ftp.FTPFile;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.JList;

public class TheFuckingJTree extends JFrame {

	JTree tree = new JTree();
	private JPanel contentPane;
	JList list = new JList();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TheFuckingJTree frame = new TheFuckingJTree();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TheFuckingJTree() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		
		contentPane.add(new JScrollPane(tree), BorderLayout.CENTER);
		
		contentPane.add(new JScrollPane(list), BorderLayout.SOUTH);
		
		tree.setCellRenderer(new FileTreeCellRenderer());
		showLocalDirTree();
		addLocalTreeListener();
	}
	
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
		this.tree.setModel(model);
		this.list.setListData(new File[0]);
	}
	
	private void addLocalTreeListener() {
		this.tree.addTreeSelectionListener((TreeSelectionEvent e) -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
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
	        	    	DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file) {
	        	    		public String toString() {
	        					return file.getName();
	        	    		}
	        	    	};
	        	    	node.add(childNode);
	        	    }
	        	    //Set directory path
	        		//this.txtLocalDirPath.setText(seletedFile.toString()); 
	        		//Expand tree 
	        	    //this.localDirTree.expandPath(new TreePath(node.getPath()));
	        	    //Show child file and folder
	        	    this.list.setListData(subItems);
	            }          	
			}
		});		
	}

}

@SuppressWarnings("serial")
class FileTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        
        Icon directoryIcon = UIManager.getIcon("FileView.directoryIcon");
        Icon fileIcon = UIManager.getIcon("FileView.fileIcon");
        
        if (value instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            if (node.getUserObject() instanceof File) {
            	if (((File)node.getUserObject()).isFile()) setIcon(fileIcon);
                else setIcon(directoryIcon);              
            } else if (node.getUserObject() instanceof FTPFile) {
            	if (((FTPFile)node.getUserObject()).isFile()) setIcon(fileIcon);
            	else setIcon(directoryIcon);     
            }
        }
        return this;
    }
}

@SuppressWarnings("serial")
class IconListRenderer extends DefaultListCellRenderer {
	Icon directoryIcon = UIManager.getIcon("FileView.directoryIcon");
	Icon fileIcon = UIManager.getIcon("FileView.fileIcon");
	@SuppressWarnings("rawtypes")
	@Override	
	public Component getListCellRendererComponent(
			JList list, Object value, int index,	
			boolean isSelected, boolean cellHasFocus) {
		// Get the renderer component from parent class
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		// Get icon to use for the list item value
		if (value instanceof File) {
			if (((File)value).isFile()) label.setIcon(fileIcon);
			else label.setIcon(directoryIcon);              
		} else if (value instanceof FTPFile) {
			if (((FTPFile)value).isFile()) label.setIcon(fileIcon);
			else label.setIcon(directoryIcon);     
		}
				
		return label;
	}
}
