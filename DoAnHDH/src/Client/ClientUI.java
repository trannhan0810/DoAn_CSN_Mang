package Client;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
@SuppressWarnings({ "rawtypes", "serial" })
public class ClientUI extends JFrame {

	protected JPanel contentPane;
	protected JButton btnFood;
	protected JButton btnSubmit;
	protected JButton btnRes;
	protected JButton btnQuit;
	protected JButton btnStart;
	protected JTextArea txtLog;
	private JPanel panel_8;
	private JPanel panel_9;
	private JLabel lblNewLabel;
	protected JTextField txtCurrentFood;
	private JPanel panel_10;
	private JPanel panel_11;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	protected JList listRes;
	protected JList listFood;
	private JPanel panel_12;
	protected JButton btnHost;
	protected JButton btnHelp;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					ClientUI frame = new ClientUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClientUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 2, 0, 0));
		
		btnFood = new JButton("Change Food");
		btnFood.setEnabled(false);
		btnFood.setBackground(Color.LIGHT_GRAY);
		panel_4.add(btnFood);
		
		btnRes = new JButton("Get cooking tool");
		btnRes.setEnabled(false);
		btnRes.setBackground(Color.LIGHT_GRAY);
		panel_4.add(btnRes);
		
		btnSubmit = new JButton("Cook&Submit");
		btnSubmit.setEnabled(false);
		btnSubmit.setBackground(Color.LIGHT_GRAY);
		panel_4.add(btnSubmit);
		
		btnQuit = new JButton("Quit");
		btnQuit.setEnabled(false);
		btnQuit.setBackground(Color.LIGHT_GRAY);
		panel_4.add(btnQuit);
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		btnStart = new JButton("START");
		btnStart.setBackground(Color.LIGHT_GRAY);
		panel_3.add(btnStart);
		
		panel_12 = new JPanel();
		panel_3.add(panel_12, BorderLayout.WEST);
		panel_12.setLayout(new GridLayout(0, 1, 0, 0));
		
		btnHost = new JButton("Host");
		btnHost.setBackground(Color.LIGHT_GRAY);
		panel_12.add(btnHost);
		
		btnHelp = new JButton("HELP");
		btnHelp.setEnabled(false);
		btnHelp.setBackground(Color.LIGHT_GRAY);
		panel_12.add(btnHelp);
		
		JPanel panel = new JPanel();
		panel_1.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLayeredPane panel_6 = new JLayeredPane();
		panel.add(panel_6);
		panel_6.setLayout(new CardLayout(0, 0));
		
		ImagePanel panel_7 = new ImagePanel(new ImageIcon("food.jpg").getImage());
		//panel_7.setVisible(false);
		panel_6.setLayer(panel_7, 0);
		panel_6.add(panel_7);
		
		txtLog = new JTextArea();
		txtLog.setLineWrap(true);
		txtLog.setOpaque(true);
		panel_6.setLayer(txtLog, 1);
		panel_6.add(txtLog, "name_641684350509500");
		
		JPanel panel_5 = new JPanel();
		panel.add(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		panel_8 = new JPanel();
		panel_5.add(panel_8);
		panel_8.setLayout(new GridLayout(0, 1, 0, 0));
		
		panel_10 = new JPanel();
		panel_10.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_8.add(panel_10);
		panel_10.setLayout(new BorderLayout(0, 0));
		
		lblNewLabel_1 = new JLabel("Required tool: ");
		panel_10.add(lblNewLabel_1, BorderLayout.NORTH);
		
		listRes = new JList();
		panel_10.add(listRes, BorderLayout.CENTER);
		
		panel_11 = new JPanel();
		panel_11.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_8.add(panel_11);
		panel_11.setLayout(new BorderLayout(0, 0));
		
		lblNewLabel_2 = new JLabel("Complete state: ");
		panel_11.add(lblNewLabel_2, BorderLayout.NORTH);
		
		listFood = new JList();
		panel_11.add(listFood, BorderLayout.CENTER);
		
		panel_9 = new JPanel();
		panel_9.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_5.add(panel_9, BorderLayout.NORTH);
		panel_9.setLayout(new BorderLayout(0, 0));
		
		lblNewLabel = new JLabel(" Current food: ");
		panel_9.add(lblNewLabel, BorderLayout.WEST);
		
		txtCurrentFood = new JTextField();
		panel_9.add(txtCurrentFood, BorderLayout.CENTER);
		txtCurrentFood.setColumns(10);
		
		setVisible(true);
	}
}


@SuppressWarnings("serial")
class ImagePanel extends JPanel {

	  private Image img;

	  public ImagePanel(String img) {
	    this(new ImageIcon(img).getImage());
	  }

	  public ImagePanel(Image img) {
	    this.img = img.getScaledInstance(240, 240, Image.SCALE_DEFAULT);;
	    Dimension size = new Dimension(this.img.getWidth(null), this.img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	  }

	  public void paintComponent(Graphics g) {
	    g.drawImage(img, 0, 0, null);
	  }
}