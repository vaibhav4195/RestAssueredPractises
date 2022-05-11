package files;

import io.restassured.path.json.JsonPath;

public class ReUsableMethods {
	
	public static JsonPath rawToJson(String text) {
		JsonPath jp = new JsonPath(text);
		return jp;
	}

}
