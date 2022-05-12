package files;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataDriven {
	
	public ArrayList<String> getData(String testCaseName,String sheetName) throws IOException {
		ArrayList<String> arr = new ArrayList<String>();
		FileInputStream fis = new FileInputStream("D:\\Users\\Temp\\Desktop\\RestAssueredAPI\\RestAssuredPractise\\src\\test\\resources\\Datademo.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheet(sheetName);//Sheet is collection of rows
		Iterator<Row> rows = sheet.iterator();
		Row firstRow = rows.next();
		Iterator<Cell> ce = firstRow.cellIterator();//Row is a collection of rows
		int k=0;
		int coloumn=0;
		while(ce.hasNext()) {
		Cell value = ce.next();
			if(value.getStringCellValue().equalsIgnoreCase("TestCases")) {
			//Desired Columns
				coloumn = k;
			}
			k++;
		}
		System.out.println(coloumn);
		
		
		while(rows.hasNext()) {
			Row r = rows.next();
			if(r.getCell(coloumn).getStringCellValue().equalsIgnoreCase(testCaseName)){
				Iterator<Cell> cp = r.cellIterator();
				while(cp.hasNext()) {
				Cell c = cp.next();
				if(c.getCellTypeEnum()==CellType.STRING) {
					arr.add(c.getStringCellValue());
				}else {
					arr.add(NumberToTextConverter.toText(c.getNumericCellValue()));
				
				}
				
				}
			}
		}
		workbook.close();
		return arr;
		
	}

}
