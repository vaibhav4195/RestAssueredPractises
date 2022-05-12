package test;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.annotations.Test;

import files.DataDriven;
import files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class PostDataXml {
	@Test
	public void addedBook() throws IOException {
		
		DataDriven d = new DataDriven();
		ArrayList<String> data = d.getData("RestAddBook","RestAssured");
		HashMap<String, Object>  jsonAsMap = new HashMap<String, Object>();
		jsonAsMap.put("name", data.get(1));
		jsonAsMap.put("isbn", data.get(2));
		jsonAsMap.put("aisle", data.get(3));
		jsonAsMap.put("author", data.get(4));
		
		
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().header("Content-Type", "application/json")
		.body(jsonAsMap)
		.when().post("/Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		
	
	JsonPath jp = ReUsableMethods.rawToJson(response);
	String id = jp.getString("ID");
	System.out.println("The Id for newly added book is: "+id);
	}
	
	
//	@DataProvider(name="BooksData")
//	public Object[][] getData() {
//		//Multi-directional array = collection of arrays
//		return new Object[][] {{"gghvg","8414"},{"tatsqay","8793"},{"wqwjg","2163"}};
//	}
}
