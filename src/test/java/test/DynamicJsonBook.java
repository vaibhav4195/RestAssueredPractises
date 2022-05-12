package test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;
import files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static  io.restassured.RestAssured.*;

public class DynamicJsonBook {
	@Test(dataProvider="BooksData")
	public void addBook(String isbn,String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		
		String response = given().log().all().header("Content-Type", "application/json")
		.body(Payload.addBook(isbn,aisle))
		.when().post("/Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		
		
	JsonPath jp = ReUsableMethods.rawToJson(response);
	String id = jp.get("ID");
	System.out.println("The Id for newly added book is: "+id);
	}
	
	
	@DataProvider(name="BooksData")
	public Object[][] getData() {
		//Multi-directional array = collection of arrays
		return new Object[][] {{"gghghvg","8514"},{"tatsjhqay","8893"},{"wqwccjg","2993"}};
	}
}
