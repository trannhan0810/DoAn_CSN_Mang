package Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import Type.JSONObject;

public class Server {

	int port = 4444;
	ServerSocket serverSocket = null;
	List<ServerThread> threads = null;
	Game game;
	//ServerLog logger;
	Server server = this;
	
	private boolean keepGoing = false;
	//private int numOfPlayer = 0;
	
	public static void main(String[] args) {
		Server server = new Server(4444);
		server.start();
	}

	Server(Integer port) {
		if(port!=null) this.port = port;
		
		threads =  new ArrayList<>();
		
		//logger = new ServerLog();
		//logger.setVisible(true);
	}
	
	public void start() {
		keepGoing = true;
		try {
			game = new Game();
			game.INIT();
			serverSocket = new ServerSocket(this.port);
			Log("Server is running on port "+ this.port);
			//Waitting for new player
			while(keepGoing) {
				Log("Waiting for new player");
				
				Socket socket = serverSocket.accept();
				if(!keepGoing) break;
				
				int ID = threads.size();
				
				ServerThread t = new ServerThread(socket,ID) {
					@Override
					public void sendCommandToServer(int playerID, JSONObject clientRequest) {
						getCmdAndExcute(playerID, clientRequest);
						JSONObject state = game.STATE(playerID);
						this.send(state);
						if(game.isEndGame) {
							removeAll();
						}
					}
				};		
				Log("New player connected");
				threads.add(t);
				t.start();	
			}
			//Keep going = false, game finish
			try {
				Log("Finish");
				serverSocket.close();
				for(ServerThread t : threads) {
					try {
						t.socket.close();
					} catch (IOException err) {
						//Nothing to do
					}
				}
			} catch(IOException err) {
				//Error when closing server and client
				Log("Exception closing the server and clients: " + err);
			}
		} catch(IOException err) {
			//Some thing went wrong
			Log("Cant create server: " + err);
		}
	}
	
	@SuppressWarnings("resource")
	public void stop() {
		keepGoing = false;
        // connect to myself as Client to exit statement
        try {
            //new Socket("localhost", port);
        } catch (Exception e) {
            // nothing to do
        }
	}
	
	synchronized void remove(int id) {
        // scan the array list until we found the Id
		for(ServerThread t : threads) {
			if (t.ID == id) {
                threads.remove(t);
                return;
            }
		}
    }
	
	@SuppressWarnings("deprecation")
	synchronized void removeAll() {
		for(ServerThread t : threads) {
			JSONObject end = new JSONObject();
			end.put("type", "finish");
			end.put("gameFinished", game.winner);
			t.send(end);
			try {
				t.socket.close();
			} catch (Exception e) {
			}
            t.stop();              
        }
		threads.clear();
		start();
    }
	
	synchronized void getCmdAndExcute(int playerID, JSONObject clientRequest) {
        String type = (String) clientRequest.get("type");
        switch (type) {
            case "change_food":
            	int foodID = (int) clientRequest.get("foodID");
            	game.CHANGE_FOOD(playerID, foodID);
                break;
            case "send_food":
            	game.RECEIVE_FOOD(playerID);
                break;
            case "request_for_resource":  	
            	int resID = (int) clientRequest.get("resID");
            	int resNumber = (int) clientRequest.get("resNumber");
            	String res = resID + ":" + resNumber;
            	game.GRANT_RESOURCE(playerID, new String[] {res});
                break;
            case "get_state":
                break;
        }
        game.showResourceState();
	}
	
	void Log(String mess){
		System.out.println(mess);
		//String text = logger.getTextArea().getText();
		//text += mess + "\n";
		//logger.getTextArea().setText(text);
	}

//==============================================================================
//SERVER THREAD
	class ServerThread extends Thread {
		public Socket socket;
		int ID;
		ObjectInputStream in;
		ObjectOutputStream out;
		
		public ServerThread(Socket socket,int ID) {		
			this.socket = socket;
			this.ID = ID;
		}
		
		@Override
		public void run() {
			super.run();
			JSONObject obj = new JSONObject();		
			obj.put("type", "connected");
			obj.put("content", "You have connected to the server... ..."
					+ "\nFollowing is the rule:"
	                + "\n1.You need cooked all food to win;" 
					+ "\n2.Press button 'get cooking tool' to get your tools;"
	                + "\n3.Press button 'cook&submit' once you get all the needed resources;"
	                + "\n4.Press button 'change food' to change to another dish when you cant get the resources from your current dish.");
			this.send(obj);
			
			obj = new JSONObject();
			obj.put("type", "data");
			obj.put("numOfFoods", Foods.getNumOfFood());
			obj.put("foods", Foods.foodNames);
			obj.put("numOfRes", CookingTools.getNumOfTool());
			obj.put("resources", CookingTools.cookingToolNames);
			this.send(obj);
			
			System.out.println("waiting for another player . . .");
			while(threads.size()!=2){
            	try {
					Thread.sleep(2000);
				} catch (InterruptedException err) {
					Log("Thread is interrupted" + err);
				}
            }
			
			obj = new JSONObject();
			obj.put("type", "get_state");
			sendCommandToServer(ID, obj);
			
			obj = new JSONObject();
            obj.put("type", "ready");
            this.send(obj);
            
            try {
            	JSONObject receivedObj;
                while ((receivedObj = (JSONObject) this.received()) != null) {
                    try {
                        sendCommandToServer(ID, receivedObj);
                    } catch (Exception err) {
                    	//Nothing to do...
                    }        
                }
                removeAll();
            } catch (Exception err) {
                //Log("Error when close client "+ID+" socket: " + err);
            }
		}
		
		public void sendCommandToServer(int playerID, JSONObject clientRequest) {
			
		}
		
		public void send(JSONObject obj){
			try {
				out = new ObjectOutputStream(this.socket.getOutputStream());
				this.out.writeObject(obj);
			} catch (IOException err) {
				Log("Cant not send to client "+ this.ID + ": " +err);
			}
		}
		
 		public Object received() {
			try {
				in = new ObjectInputStream(this.socket.getInputStream());
				return this.in.readObject();
			} catch (ClassNotFoundException | IOException err) {
				Log("Cant not read from client "+ this.ID + ": " +err);
				return null;
			}
		}
	}
}
