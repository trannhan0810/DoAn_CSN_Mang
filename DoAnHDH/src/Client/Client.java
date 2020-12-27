package Client;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import Type.JSONObject;


@SuppressWarnings("serial")
public class Client extends ClientUI {

	public static void main(String[] args) {
		new Client();
	}
	
	String host = "localhost";
	int port = 4444;
	Socket socket = null;
	
	//Resource
	String help = "";
	String[] foodNames;;
	String[] resNames;
	
	//Player state
	int[] resourceNeed;
	int[] resourceHave;
	boolean[] completeState;
	
	Client() {
		addListener();
	}
	
	private void addListener() {
		addStartBtnListener();
		addHelpBtnListener();
		addFoodBtnListener();
		addResBtnListener();
		addSubmitBtnListener();
		addQuitBtnListener();
		addHostBtnLister();
	}
	
	//=========================================================================
	//Add UI Listener
	void addStartBtnListener() {
		this.btnStart.addActionListener((ActionEvent e) -> {
			try {
				socket = new Socket(host, port);
				JOptionPane.showMessageDialog(this, "Connected to server");
				new ClientThread(socket) {		
					public void excute(JSONObject receivedObj) {
						receiveStateFromServer(receivedObj);
					}
				}.start();
			} catch (IOException err) {
				JOptionPane.showMessageDialog(this, "Fail to connect server");
			}
		});
	}
	
	@SuppressWarnings("unused")
	void addHostBtnLister() {
		this.btnHost.addActionListener((ActionEvent e) -> {
			String host = JOptionPane.showInputDialog(this, "Enter server host name: ");
			try {
				InetAddress IP = InetAddress.getByName(host);
				this.host = host;
			} catch (UnknownHostException e1) {
				JOptionPane.showMessageDialog(this, "invalid host name");
			}
		});
	}
	
	void addHelpBtnListener() {
		this.btnHelp.addActionListener((ActionEvent e) -> {
			JOptionPane.showMessageDialog(this, this.help);	
		});
	}
	
	void addFoodBtnListener() {
		this.btnFood.addActionListener((ActionListener)->{
			ArrayList<JSONObject> foodOption = new ArrayList<JSONObject>();
			for(int i = 0; i< this.foodNames.length; i++) {
				if(this.completeState[i]!=true) {
					JSONObject food = new JSONObject(this.foodNames[i]);
					food.put("foodID", i);
					foodOption.add(food);
				}
			}
			Object selected = JOptionPane.showInputDialog(this, "Choose new food: ", "Change food", JOptionPane.DEFAULT_OPTION, null, foodOption.toArray(), null);
			if ( selected != null ){//null if the user cancels. 
				JSONObject request = (JSONObject) selected;
				request.put("type", "change_food");
				this.send(request);
			}
		});
	}
	
	void addResBtnListener() {
		this.btnRes.addActionListener((ActionListener)->{
			ArrayList<JSONObject> resOption = new ArrayList<JSONObject>();
			for(int i = 0; i< this.resNames.length; i++) {
				if(this.resourceNeed[i]>0) {
					JSONObject res = new JSONObject(this.resNames[i]);
					res.put("resID", i);
					resOption.add(res);
				}
			}
			Object selected = JOptionPane.showInputDialog(this, "Selecte a resource to request: ", "Request resource", JOptionPane.DEFAULT_OPTION, null, resOption.toArray(), null);
			if ( selected != null ){//null if the user cancels. 
				JSONObject request = (JSONObject) selected;
				request.put("type", "request_for_resource");
				try {
					int num = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter number to request: ", 1));
					request.put("resNumber", num);
					this.send(request);
				} catch(Exception e) {
					
				}
			}
		});
	}
	
	void addSubmitBtnListener() {
		this.btnSubmit.addActionListener((ActionListener)->{
			JSONObject request = new JSONObject();
			request.put("type", "send_food");
			this.send(request);
		});
	}
	
	void addQuitBtnListener() {
		this.btnQuit.addActionListener((ActionListener)->{
			QUIT();
		});
	}
	
	//=========================================================================
	//Handle receive state
	void receiveStateFromServer(JSONObject state) {
		String type = (String) state.get("type");
		Log(type);
		switch(type) {
		case "connected":
			CONNECTED(state);
			break;
		case "ready":
			READY(state);
			break;
		case "data":
			GET_DATA(state);
			System.out.print(state.get("foods"));
			break;
		case "state":
			CHANGE_STATE(state);
			break;
		case "finish":
			Object winner = (Object) state.get("gameFinished");
			if(winner != null) {
				JOptionPane.showMessageDialog(this, "Winner is player "+ winner);
			} else JOptionPane.showMessageDialog(this, "Opponent quit, you win");
			QUIT();
			break;
		}
	}
	
	void CONNECTED(JSONObject receivedObj) {
		this.btnStart.setEnabled(false);
		this.btnHelp.setEnabled(true);
		this.help = (String) receivedObj.get("content");
	}
	
	void READY(JSONObject receivedObj) {
		this.btnFood.setEnabled(true);
		this.btnRes.setEnabled(true);
		this.btnSubmit.setEnabled(true);
		this.btnQuit.setEnabled(true);
	}
		
	void GET_DATA(JSONObject receivedObj) {
		this.resNames = (String[]) receivedObj.get("resources");
		this.foodNames = (String[]) receivedObj.get("foods");
		
	}
	
	@SuppressWarnings("unchecked")
	void CHANGE_STATE(JSONObject state) {
		this.resourceNeed = (int[]) state.get("resourceNeed");
		this.resourceHave = (int[]) state.get("resourceHave");
		this.completeState = (boolean[]) state.get("completeState");
		
		this.txtCurrentFood.setText(this.foodNames[(int) state.get("currentFood")]);
		ArrayList<String> foodToShow = new ArrayList<String>();
		ArrayList<String> resToShow = new ArrayList<String>();
		for(int i=0; i< this.foodNames.length; i++) 
			foodToShow.add(this.foodNames[i] + " " + (completeState[i]?"OK":"--"));
		for(int i=0; i< this.resNames.length; i++) {
			if(resourceNeed[i]!=0) 
				resToShow.add(this.resNames[i] + " " + resourceHave[i] + "/" + resourceNeed[i]);
		}
		this.listFood.setListData(foodToShow.toArray());
		this.listRes.setListData(resToShow.toArray());
		
		String message = (String) state.get("returnMessage");
		if(message != null) JOptionPane.showMessageDialog(this, message);
	}
	
	@SuppressWarnings("unchecked")
	void QUIT() {
		try { 
			socket.close();
			this.btnStart.setEnabled(true);
			this.btnHelp.setEnabled(false);
			this.btnFood.setEnabled(false);
			this.btnRes.setEnabled(false);
			this.btnSubmit.setEnabled(false);
			this.btnQuit.setEnabled(false);
			this.listFood.setListData(new Object[]{});
			this.listRes.setListData(new Object[]{});
			this.txtCurrentFood.setText("");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	//=====================================================================
	public void send(JSONObject obj){
		try {		
			ObjectOutputStream out = new ObjectOutputStream(this.socket.getOutputStream());
			out.writeObject(obj);
		} catch (IOException err) {
		}
	}
	
	public Object received() {
		try {
			//System.out.println("receivedSomething");
			ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream());
			return in.readObject();
		} catch (ClassNotFoundException | IOException err) {
			return null;
		}
	}
	
	void Log(String mess) {
		System.out.println(mess);
		String log = this.txtLog.getText() + "\n" + mess;
		this.txtLog.setText(log);
	}
	
	class ClientThread extends Thread {
		public ClientThread(Socket socket) {	
		}
		
		@Override
		public void run() {
			JSONObject receivedObj;
			while(true) {	
				receivedObj = (JSONObject) received();
				if(receivedObj == null) break;
				excute(receivedObj);
			}
		}

		public void excute(JSONObject receivedObj) {
		}
	}

}
