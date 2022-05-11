package test;

import org.testng.Assert;
import org.testng.annotations.Test;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class SumValidationComplexJson {

	@Test
	public void sumOfCourses() {
		//It is response that we expected
		JsonPath jp = new JsonPath(Payload.complexJson());
		int courseCount = jp.getInt("courses.size()");
		int sum = 0;
		for(int i = 0; i < courseCount; i++) {
			int price = jp.getInt("courses["+i+"].price");
			int copiesCount = jp.get("courses["+i+"].copies");
			int amount = price*copiesCount;
			System.out.println(amount);
			sum = sum + amount;
		}
		System.out.println("Total sum is: "+sum);
		int purchaseAmount = jp.getInt("dashboard.purchaseAmount");
		//Validation to check
		Assert.assertEquals(purchaseAmount, sum);
	}
}
