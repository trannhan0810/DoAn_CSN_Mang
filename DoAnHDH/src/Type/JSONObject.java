package Type;

import java.util.HashMap;

@SuppressWarnings("serial")
public class JSONObject extends HashMap<String, Object> {
	
	String name;
	
	public JSONObject(){
		super();
	};
	
	public JSONObject(String name){
		super();
		this.name = name;
	};
	
	@Override
	public String toString() {
		if(this.name != null) return this.name;
		return super.toString();
	}
}
