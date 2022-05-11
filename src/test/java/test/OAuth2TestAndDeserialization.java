package test;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;
public class OAuth2TestAndDeserialization {

	public static void main(String[] args) {
		
		String[] webAutoCourseTitles = {"Selenium Webdriver Java","Cypress","Protractor"};
		
		String url ="https://rahulshettyacademy.com/getCourse.php?code=4%2F0AX4XfWjjVNEipe0uUzP8cgpI8FqQ8oneJG0MJHUwJ2KukIYNnijpqW85qPhZ0RTPugBswg&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=none";
		String partialcode=url.split("code=")[1];

		String code=partialcode.split("&scope")[0];

		System.out.println(code);
		
		String accessTokenResponse = given().urlEncodingEnabled(false)
		.queryParams("code", code)
		.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type", "authorization_code")
		.when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();
	
	JsonPath jp = new JsonPath(accessTokenResponse);
	String accessToken = jp.getString("access_token");
	System.out.println(accessToken);
	
		//Need Access token to get course details 
	GetCourse gc = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
			.when()
			.get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
	System.out.println(gc.toString());
	
	System.out.println(gc.getLinkedIn());
	System.out.println(gc.getInstructor());
	System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());
	
	List<Api> apicourse = gc.getCourses().getApi();
	for(int i = 0; i < apicourse.size(); i++) {
		if(apicourse.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
			System.out.println("Price of SoapUI course : "+apicourse.get(i).getPrice());
		}
	}
	ArrayList<String> arr =new ArrayList<String>();
	
	List<WebAutomation> webAutomationCourse = gc.getCourses().getWebAutomation();
	for(int j = 0; j < webAutomationCourse.size(); j++) {
	arr.add(webAutomationCourse.get(j).getCourseTitle());
	}
	
	List<String> expectedList = Arrays.asList(webAutoCourseTitles);
//	System.out.println("Checking expected and actual");
	Assert.assertTrue(arr.equals(expectedList));
	if(arr.equals(expectedList) == true) {
		System.out.println("expected and actual array are same");
	}
	}

}
