package test;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

public class JiraTest {

	public static void main(String[] args) {
		
		RestAssured.baseURI = "http://localhost:8084";
		SessionFilter session = new SessionFilter();
		//Get authentication
		String response = given().header("Content-Type", "application/json").body("{ \"username\": \"vaibhav.pawde\", \"password\": \"Vkgsckp@357\" }")
			.log().all().filter(session).when().post("/rest/auth/1/session")
			.then().log().all().extract().response().asString();
		
		String expectedMessage ="Hi, how are you?"; 
		
		//Add Comment
		String addCommentResponse =	given().pathParam("id", "10100").log().all().header("Content-Type", "application/json").body("{\r\n" + 
				"    \"body\": \""+expectedMessage+"\",\r\n" + 
				"    \"visibility\": {\r\n" + 
				"        \"type\": \"role\",\r\n" + 
				"        \"value\": \"Administrators\"\r\n" + 
				"    }\r\n" + 
				"}")
		.filter(session).when().post("/rest/api/2/issue/{id}/comment")
		.then().log().all().assertThat().statusCode(201).extract().response().asString();
		JsonPath jp = new JsonPath(addCommentResponse);
		String commentId = jp.getString("id");
		
		//Add attachments
		given().header("X-Atlassian-Token", "no-check").filter(session).pathParam("id", "10100")
		.header("Content-Type", "multipart/form-data")
		.multiPart("file", new File("D:\\Users\\Temp\\Desktop\\RestAssueredAPI\\RestAssuredPractise\\src\\test\\resources\\jira.txt"))
		.when().post("/rest/api/2/issue/{id}/attachments")
		.then().log().all().assertThat().statusCode(200);
		
		//Get issue
		String issueDetails = given().filter(session).pathParam("id", "10100")
				.queryParam("fields", "comment").log().all()
		.when().get("/rest/api/2/issue/{id}")
		.then().log().all().extract().response().asString();
		System.out.println("Issue is :"+issueDetails);
		
		JsonPath jp1 = new JsonPath(issueDetails);
		int commentCount = jp1.getInt("fields.comment.comments.size()");
		for(int i = 0; i < commentCount; i++) {
		String commentIdIssue =	jp1.get("fields.comment.comments["+i+"].id").toString();
		if(commentIdIssue.equalsIgnoreCase(commentId)) {
			String resMessage = jp1.get("fields.comment.comments["+i+"].body").toString();
			System.out.println(resMessage);
			Assert.assertEquals(resMessage, expectedMessage);
		}
		}
		
		
		
	}

}
