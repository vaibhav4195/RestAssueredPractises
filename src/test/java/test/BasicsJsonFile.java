package test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

//We have to use static import to get the given() methods
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;
import files.ReUsableMethods;



public class BasicsJsonFile {

	public static void main(String[] args) throws IOException {
		//Add place -> Update place with new address -> Get place to validate if new address is present in response

		// Validate if Add Place API is working as expected

		//given - All input details(i.e. Key, Parameter, Header, Body)
		//when - Submit the API - resource, and http methods are written
		//then - Validate the response(Assertions,status code,get response as string
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		//Post place
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(new String(Files.readAllBytes(Paths.get("D:\\Users\\Temp\\Desktop\\RestAssueredAPI\\RestAssuredPractise\\src\\test\\resources\\addPlace.json"))))
				.when().post("maps/api/place/add/json")
				.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
				.header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
				
		System.out.println("Response after posting a place: "+response);
		
		JsonPath jp = ReUsableMethods.rawToJson(response);//For parsing JSON
		String placeId = jp.getString("place_id");
		System.out.println("Place id after creation: "+placeId);
		
		
		//Update the address
		String newAddress = "Raheja Plaza, Ghatkopar West, Mumbai";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body("{\r\n" + 
				"\"place_id\":\""+placeId+"\",\r\n" + 
				"\"address\":\""+newAddress+"\",\r\n" + 
				"\"key\":\"qaclick123\"\r\n" + 
				"}")
		.when().put("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get Place
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		JsonPath jp1 = ReUsableMethods.rawToJson(getPlaceResponse);//For parsing JSON
		String updatedAddress = jp1.getString("address");
		System.out.println("Address after updation: "+updatedAddress);
		
		Assert.assertEquals(newAddress, updatedAddress);

		
		
	}

}
