package Final;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;

import java.io.File;
import java.awt.Color;
import java.awt.Component;

import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.JTextArea;
import java.awt.GridLayout;
import javax.swing.JTree;
import javax.swing.JList;
import org.apache.commons.net.ftp.FTPFile;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.tree.DefaultTreeModel;

@SuppressWarnings("serial")
public class BaseUI extends JFrame {

	protected JPanel contentPane;
	protected JTextField txtHost;
	protected JTextField txtPort;
	protected JTextField txtUser;
	protected JPasswordField txtPass;
	protected JTextArea txtLogger;
	protected JButton btnConnect;
	protected JList<File> listLocalFile;
	protected JList<FTPFile> listRemoteFile;
	protected JTextField txtLocalDirPath;
	protected JTree localDirTree;
	protected JTextField txtRemoteDirPath;
	protected JTree remoteDirTree;
	protected JButton btnLocalNew;
	protected JButton btnLocalDelete;
	protected JButton btnLocalRename;
	protected JButton btnUpload;
	protected JButton btnRemoteNew;
	protected JButton btnRemoteDelete;
	protected JButton btnRemoteRename;
	protected JButton btnDownload;

	/**
	 * Create the frame.
	 */
	public BaseUI() {
		setForeground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 981, 553);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 10));
		setContentPane(contentPane);
		
		JPanel login = new JPanel();
		login.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(login, BorderLayout.NORTH);
		GridBagLayout gbl_login = new GridBagLayout();
		gbl_login.columnWidths = new int[]{22, 102, 20, 38, 48, 102, 46, 102, 73, 0};
		gbl_login.rowHeights = new int[]{23, 0};
		gbl_login.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_login.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		login.setLayout(gbl_login);
		
		JLabel lblNewLabel = new JLabel("Host");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		login.add(lblNewLabel, gbc_lblNewLabel);
		
		txtHost = new JTextField();
		txtHost.setText("localhost");
		GridBagConstraints gbc_txtHost = new GridBagConstraints();
		gbc_txtHost.anchor = GridBagConstraints.WEST;
		gbc_txtHost.insets = new Insets(0, 0, 0, 5);
		gbc_txtHost.gridx = 1;
		gbc_txtHost.gridy = 0;
		login.add(txtHost, gbc_txtHost);
		txtHost.setColumns(12);
		
		JLabel lblNewLabel_1 = new JLabel("Port");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_1.gridx = 2;
		gbc_lblNewLabel_1.gridy = 0;
		login.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		txtPort = new JTextField();
		txtPort.setText("21");
		GridBagConstraints gbc_txtPort = new GridBagConstraints();
		gbc_txtPort.anchor = GridBagConstraints.WEST;
		gbc_txtPort.insets = new Insets(0, 0, 0, 5);
		gbc_txtPort.gridx = 3;
		gbc_txtPort.gridy = 0;
		login.add(txtPort, gbc_txtPort);
		txtPort.setColumns(4);
		
		JLabel lblNewLabel_2 = new JLabel("Username");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_2.gridx = 4;
		gbc_lblNewLabel_2.gridy = 0;
		login.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		txtUser = new JTextField();
		txtUser.setText("FTPUser");
		GridBagConstraints gbc_txtUser = new GridBagConstraints();
		gbc_txtUser.anchor = GridBagConstraints.WEST;
		gbc_txtUser.insets = new Insets(0, 0, 0, 5);
		gbc_txtUser.gridx = 5;
		gbc_txtUser.gridy = 0;
		login.add(txtUser, gbc_txtUser);
		txtUser.setColumns(12);
		
		JLabel lblNewLabel_3 = new JLabel("Password");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_3.gridx = 6;
		gbc_lblNewLabel_3.gridy = 0;
		login.add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		txtPass = new JPasswordField();
		txtPass.setColumns(12);
		txtPass.setText("Nhan1234");
		GridBagConstraints gbc_txtPass = new GridBagConstraints();
		gbc_txtPass.anchor = GridBagConstraints.WEST;
		gbc_txtPass.insets = new Insets(0, 0, 0, 5);
		gbc_txtPass.gridx = 7;
		gbc_txtPass.gridy = 0;
		login.add(txtPass, gbc_txtPass);
		
		btnConnect = new JButton("Connect");
		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnConnect.gridx = 8;
		gbc_btnConnect.gridy = 0;
		login.add(btnConnect, gbc_btnConnect);
		
		JPanel main = new JPanel();
		contentPane.add(main, BorderLayout.CENTER);
		main.setLayout(new BorderLayout(0, 10));
		
		txtLogger = new JTextArea();
		txtLogger.setLineWrap(true);
		txtLogger.setEditable(false);
		txtLogger.setRows(4);
		JScrollPane logger = new JScrollPane(txtLogger);
		logger.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		main.add(logger, BorderLayout.NORTH);
		
		JPanel folder = new JPanel();
		main.add(folder, BorderLayout.CENTER);
		folder.setLayout(new GridLayout(0, 2, 10, 0));
		
		JPanel panel_3 = new JPanel();
		folder.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		txtLocalDirPath = new JTextField();
		txtLocalDirPath.setColumns(10);
		panel_3.add(txtLocalDirPath, BorderLayout.NORTH);
		
		localDirTree = new JTree();
		localDirTree.setVisibleRowCount(8);
		JScrollPane scrollPane_2 = new JScrollPane(localDirTree);
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_3.add(scrollPane_2, BorderLayout.CENTER);
		
		JPanel panel_4 = new JPanel();
		folder.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		txtRemoteDirPath = new JTextField();
		txtRemoteDirPath.setColumns(10);
		panel_4.add(txtRemoteDirPath, BorderLayout.NORTH);
		
		remoteDirTree = new JTree();
		remoteDirTree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("/") {
				{
				}
			}
		));
		remoteDirTree.setVisibleRowCount(10);
		remoteDirTree.setEnabled(false);
		JScrollPane scrollPane_3 = new JScrollPane(remoteDirTree);
		scrollPane_3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_4.add(scrollPane_3, BorderLayout.CENTER);
		
		JPanel file = new JPanel();
		main.add(file, BorderLayout.SOUTH);
		file.setLayout(new GridLayout(0, 2, 10, 10));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		file.add(scrollPane);
		
		listLocalFile = new JList<File>();
		scrollPane.setViewportView(listLocalFile);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		file.add(scrollPane_1);
		
		listRemoteFile = new JList<FTPFile>();
		scrollPane_1.setViewportView(listRemoteFile);
		
		JPanel buttons = new JPanel();
		contentPane.add(buttons, BorderLayout.SOUTH);
		buttons.setLayout(new GridLayout(1, 0, 10, 0));
		
		JPanel localButtons = new JPanel();
		buttons.add(localButtons);
		localButtons.setLayout(new GridLayout(0, 4, 5, 0));
		
		btnLocalNew = new JButton("New Folder");
		localButtons.add(btnLocalNew);
		
		btnLocalDelete = new JButton("DELETE");
		localButtons.add(btnLocalDelete);
		
		btnLocalRename = new JButton("RENAME");
		localButtons.add(btnLocalRename);
		
		btnUpload = new JButton("UPLOAD");
		localButtons.add(btnUpload);
		
		JPanel remoteButtons = new JPanel();
		buttons.add(remoteButtons);
		remoteButtons.setLayout(new GridLayout(0, 4, 5, 0));
		
		btnRemoteNew = new JButton("New Folder");
		remoteButtons.add(btnRemoteNew);
		
		btnRemoteDelete = new JButton("DELETE");
		remoteButtons.add(btnRemoteDelete);
		
		btnRemoteRename = new JButton("RENAME");
		remoteButtons.add(btnRemoteRename);
		
		btnDownload = new JButton("DOWNLOAD");
		remoteButtons.add(btnDownload);
		
		
		localDirTree.setCellRenderer(new FileTreeCellRenderer());
		listLocalFile.setCellRenderer(new IconListRenderer());
		remoteDirTree.setCellRenderer(new FileTreeCellRenderer());
		listRemoteFile.setCellRenderer(new IconListRenderer());
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
