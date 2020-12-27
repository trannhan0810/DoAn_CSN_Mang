package Server;
import Type.JSONObject;
//===========================================================================================
class CookingTools {
	static String[] cookingToolNames = new String[] {"pot", "chopsticks", "wok", "cookingshovel","knife", "choppingboard"};
 	static int[] toolavailableNumber = new int[] {1, 1, 1, 1, 1, 1};
 	
	static public int getNumOfTool() {return cookingToolNames.length;}
	
	static public String[] getResourceNames() {return cookingToolNames;}
	
	static public int[] getAvailableResource() {return toolavailableNumber;}
	
	static public JSONObject getResource(int resID) {
		JSONObject res = new JSONObject();
		res.put("name", cookingToolNames[resID]);
		res.put("number", toolavailableNumber[resID]);
		return res;
	}
	
	static public JSONObject[] getResources(int resID) {
		JSONObject ress[] = new JSONObject[getNumOfTool()];
		for(int i =0 ; i<getNumOfTool(); i++) ress[i] = getResource(i);
		return ress;
	}
}