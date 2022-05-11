package test;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
	public static void main(String[] args) {
		//It is response that we expected
		JsonPath jp = new JsonPath(Payload.complexJson());
		
		//Print Number of courses returned by API
		int courseCount = jp.getInt("courses.size()");
		System.out.println("Courses count: "+courseCount);
		
		//Print purchase amount
		int totalAmount = jp.getInt("dashboard.purchaseAmount");
		System.out.println("Purchase amount is: "+totalAmount);
		
		//Print title of first course
		String firstCourseTitle = jp.get("courses[0].title");
		System.out.println("First Course title is: "+firstCourseTitle);
		
		//Print all courses title and their respective prices
		for(int i = 0; i < courseCount; i++) {
			String title = jp.get("courses["+i+"].title");
			int price = jp.getInt("courses["+i+"].price");
			System.out.println("Course title is "+title +" Price is "+price);
		}
		
		//Print number of copies sold by RPA
		for(int i = 0; i < courseCount; i++) {
			String title = jp.get("courses["+i+"].title");
			if(title.equalsIgnoreCase("RPA")) {
				//return copies sold
				int copiesCount = jp.get("courses["+i+"].copies");
				System.out.println("Copies sold by RPA: "+copiesCount);
				break;
			}
//			System.out.println("Course title is "+title +" Price is "+price);
		}
	}

}
