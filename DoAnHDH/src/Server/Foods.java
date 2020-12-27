package Server;
import Type.JSONObject;
//============================================================================
class Foods {
	
	static String[] foodNames = new String[] {"pasta", "steak", "friedrice"};
 	static int[][] resourceNeed = new int[][]  {{1, 1, 0, 0, 0, 0},
 												{0, 0, 1, 1, 0, 0},
 												{0, 1, 1, 0, 1, 1}};
	
	
	static public int getNumOfFood() {return foodNames.length;}
	
	static public String[] getFoodNames() {return foodNames;}
	
	static public int[][] getFoodNeededResource() {return resourceNeed;}
	
	static public JSONObject getFood(int foodID) {
		JSONObject food = new JSONObject();
		food.put("name", foodNames[foodID]);
		food.put("need", resourceNeed[foodID]);
		return food;
	}
	
	static public JSONObject[] getFoods(int foodID) {
		JSONObject foods[] = new JSONObject[getNumOfFood()];
		for(int i =0 ; i<getNumOfFood(); i++) foods[i] = getFood(i);
		return foods;
	}	
}