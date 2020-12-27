package Server;
import java.util.*;
import Type.JSONObject;

public class Game {
	// Data
	int numOfRes = CookingTools.getNumOfTool();
	int[] availResource = CookingTools.getAvailableResource();
	
	int numOfFood = Foods.getNumOfFood();
	int[][] foodResourceNeed = Foods.getFoodNeededResource();
		
	// Player realate
	int numOfPlayer = 2;
	int[] curentFood;
	boolean[][] completeState;

	//State of system
	int[] available;
	int[][] allocated;
	int[][] maxNeed;
	String message;
	
	public boolean isStartGame = false;
	public boolean isEndGame = false;
	public Integer winner = null;
	
	//===========================================================================

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Game game = new Game();
		game.INIT();
		while(!game.isEndGame) {
			//show resource state
			game.showResourceState(); 
			//received command from server and process
			String cmd = new Scanner(System.in).nextLine();
			game.getCommand(cmd);
		}
		game.showResourceState(); 
	}

	public void showResourceState() {
		System.out.println("\n====================================");
		System.out.println("Resource on Server: ");
		for(int res: available) System.out.print(" " + res + " ");
		System.out.print("\n");
		System.out.println("Resource on Client: ");
		for(int j=0; j< numOfPlayer; j++)
		{
		for(int i=0; i< numOfRes;i++)
		System.out.print(""+ allocated[j][i] +"/"+ maxNeed[j][i] + " ");
		System.out.print("\n");
		}
		System.out.println("====================================");
		for(int j=0; j< numOfPlayer; j++)
		{
			for(int i=0; i< numOfFood;i++)
			System.out.print(" "+ (completeState[j][i]?"OK":"NO") + " ");
			System.out.print("\n");
		}
		System.out.println("====================================");
		System.out.println(this.message);
		System.out.println("====================================");
	}

	public boolean getCommand(String cmd) {
		/**
		 * playerID	CHANGE_FOOD 	foodID
		 * playerID	GRANT_RESOURCE  [resID:number]
		 * playerID RECEIVE_FOOD			 
		 */
		System.out.println(cmd);
		String[] tokens = cmd.trim().split(" +");
		boolean cmdStatus = true;
		if(tokens.length<2) cmdStatus = false; 
		else switch(tokens[1]) {
			case "CHANGE_FOOD": cmdStatus = CHANGE_FOOD(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[2])); break;
			case "GRANT_RESOURCE": cmdStatus = GRANT_RESOURCE(Integer.parseInt(tokens[0]),Arrays.copyOfRange(tokens, 2, tokens.length));break;
			case "RECEIVE_FOOD": cmdStatus = RECEIVE_FOOD(Integer.parseInt(tokens[0]));break;
			default: setReturnMessage("Wrong command");
		}
		return cmdStatus;
	}
	
	//===========================================================================
	//Main and public method of the game

	public void INIT() {
		System.out.println("Initialize game . . .");
		//2 player
		this.numOfPlayer = 2;
		//player choose food
		this.curentFood = new int[] {0,0}; // = {0,0}
		//Init state of the game
		initAvailableResource(); 	//-> the resources system have
		initCompleteState();		//-> the state of player's dish(completed and non-completes)
		initAllocatedResource();	//-> the resources player have
		initPlayerNeededResource();	//-> the resources player will need to complete current dish	
		this.isStartGame = true;
		this.isEndGame = false;
	}
	
	public boolean CHANGE_FOOD(int playerID, int foodID) {
		if(foodID > numOfFood) {
			setReturnMessage("fail to change food");
			return false;
		} 
		//get back all resource
		getBackAllResource(playerID);
		//set new food 
		this.curentFood[playerID] = foodID;
		this.maxNeed[playerID] = foodResourceNeed[foodID];
		setReturnMessage("success to change food");
		return true;
	}

	public boolean GRANT_RESOURCE(int playerID, String[] listResString) {
		//Clone 
		int [] newAvailable = this.available.clone();
		int [][] newAllocated 
			= Arrays.stream(this.allocated).map(int[]::clone).toArray(int[][]::new);
		final int [][] maxNeed = this.maxNeed;
		
		int[][] listRes = new int[listResString.length][2];
		for(int i=0;i<listResString.length;i++) {
			String[] res = listResString[i].split(":");
			listRes[i][0] = Integer.parseInt(res[0]); // -> ResID
			listRes[i][1] = Integer.parseInt(res[1]); // -> ResNumber
		}
		//try allocate resource
		boolean allocatable = allocateResource(listRes, newAvailable, newAllocated[playerID]);
		if(!allocatable) {setReturnMessage("Resource not avaiable");return false;}
		//
		for(int i = 0; i< numOfRes; i++) 
			if(newAllocated[playerID][i] > maxNeed[playerID][i]) {
				setReturnMessage("Request too much");
				return false;
			}
		//check safe new state
		boolean isSafe = isSafeState(newAvailable, newAllocated, maxNeed);
		//allocate | deny
		if(isSafe) {
			allocateResource(listRes, this.available, this.allocated[playerID]);
			setReturnMessage("Success grant resource");
			return true;
		} else {
			setReturnMessage("Fail to get, because may to cause dead-lock");
			return false;
		}
	}

	public boolean RECEIVE_FOOD(int playerID) {
		//check summitable
		boolean isCookingDone = true;
		for(int i = 0; i < numOfRes; i++) {
			if(this.allocated[playerID][i]<this.maxNeed[playerID][i]) {isCookingDone=false;break;}
		}
		if(!isCookingDone) {setReturnMessage("Food is not cooked"); return false;}
		//get back all resource
		getBackAllResource(playerID);
		//update player state 
		completeState[playerID][curentFood[playerID]] = true;
		//CheckWin
		boolean win = true;
		for(boolean isCooked: completeState[playerID]) {
			win = win&&isCooked;
		};
		if(win) {
			this.winner = playerID;
			this.isEndGame = true;
			this.isStartGame = false;
		}
		setReturnMessage("Received the food ");
		return true;
	}
	
	public JSONObject STATE(int playerID) {
		JSONObject state = new JSONObject();
		state.put("type", "state");
		state.put("currentFood", this.curentFood[playerID]);
		state.put("resourceNeed", this.maxNeed[playerID]);
		state.put("resourceHave", this.allocated[playerID]);
		state.put("completeState", this.completeState[playerID]);
		if(this.message!=null) state.put("returnMessage", this.message);
		this.message=null;
		return state;
	}

	//===========================================================================
	/**Init the state of resource before provide to any player */
	void initAvailableResource() {
		available = CookingTools.getAvailableResource();
	}

	/**Init the progress of player, it will show which food player has completed and which not*/
	void initCompleteState() {
		this.completeState = new boolean[numOfPlayer][numOfFood];
		for(boolean[] playerCompletedList: completeState)
			Arrays.fill(playerCompletedList, false);
	}

	/**Init the state of resource player hold at start, 
	 * it will all equals 0 for each pair player-resource */
	void initAllocatedResource() {
		this.allocated = new int[numOfPlayer][numOfRes];
		for(int[] playerAllocatedResource: allocated) 
			Arrays.fill(playerAllocatedResource, 0);
	}

	/**This function will get the current choosen food of player and 
	 * calculate the maximun resource each player need */
	void initPlayerNeededResource() {
		this.maxNeed = new int[numOfPlayer][];
		for(int i=0; i<numOfPlayer; i++) this.maxNeed[i] = Foods.getFoodNeededResource()[this.curentFood[i]];
	}	
	
	/**Alocate resource*/
	boolean allocateResource(int[][] listResource, int[] from, int[] to) {
		for(int[] res:listResource) {
			int resID = res[0];
			int resNumber = res[1];
			from[resID] -= resNumber;
			to[resID] += resNumber;
			if(from[resID]<0) return false;
		}
		return true;
	};

	/**Get back all resource player holding*/
	private void getBackAllResource(int playerID) {
		for(int i=0; i<numOfRes; i++) {
			this.available[i]+=this.allocated[playerID][i];
			this.allocated[playerID][i] = 0;
		}
	}

	/**Check the new state may cause deadlock or not*/
	boolean isSafeState(int[] available, int[][] allocated, int[][] maxNeed) {
		boolean[] isDone = new boolean[numOfPlayer];
		for(int i = 0; i < numOfPlayer; i++) isDone[i] = false;
		//init work
		int[] work = available.clone();
		//init need
		int[][] need = new int[numOfPlayer][numOfRes];
		for(int i = 0; i < numOfPlayer; i++) 
			for(int j = 0; j < numOfRes; j++) 
				need[i][j] = maxNeed[i][j] - allocated[i][j];
		//banker's algorithm
		outerloop: for(int i = 0; i < numOfPlayer;) {
			//If process done, go to next process
			if (isDone[i] == true ) { i++; continue; }
			//If not enough resource, go to next process
			for (int j = 0; j < numOfRes; j++)  
				if (work[j] < need[i][j]) { i++; continue outerloop; }
			/**If enough resource, process it and release resource, 
			 * go back to the first process*/
			isDone[i] = true;
			for (int j = 0; j < numOfRes; j++) 
				work[j] += allocated[i][j];
			i = 0;
		}
		//Check all process is done, if true new state is safe, else unsafe
		for(int i = 0; i < numOfPlayer; i++)
			if(isDone[i] == false ) return false;
		return true;
	}

	/**Set return message after excute command*/
	void setReturnMessage(String message) {
		this.message = message;
	}
}




