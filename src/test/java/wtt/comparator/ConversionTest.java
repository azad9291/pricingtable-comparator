package wtt.comparator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ConversionTest {

	@Test(testName = "Price Conversion Test Pass",description = "The values in the sheets are as expected")
	public void Price_Conversion_Test_Pass() {
		try {

			FileInputStream excelFile = new FileInputStream(new File("ProductPrices.xlsx"));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Assert.assertEquals(fetchData(workbook.getSheet("Sterling"), 1.5), fetchData(workbook.getSheet("Euro"), 1.0));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test(testName = "Price Conversion Test Fail",description = "The values in the sheets should not match, Variety Added ")
	public void Price_Conversion_Test_Data_Mismatch() {
		
		try {

			FileInputStream excelFile = new FileInputStream(new File("ProductPricesFailed.xlsx"));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Assert.assertNotEquals(fetchData(workbook.getSheet("Sterling"), 1.5), fetchData(workbook.getSheet("Euro"), 1.0));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test(testName = "Price Conversion Test Fail",description = "The values in the sheets should not match, Product Added")
	public void Price_Conversion_Test_Data_Additional_Product() {
		
		try {

			FileInputStream excelFile = new FileInputStream(new File("ProductPricesFailedAdditionalValue.xlsx"));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Assert.assertNotEquals(fetchData(workbook.getSheet("Sterling"), 1.5), fetchData(workbook.getSheet("Euro"), 1.0));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private HashMap<String, List<Double>> fetchData(Sheet datatypeSheet, Double conversion) {
		HashMap<String, List<Double>> productList = new HashMap<String, List<Double>>();
		Iterator<Row> iterator = datatypeSheet.iterator();
		iterator.next();
		Iterator cellIterator;
		String productName;
		List<Double> priceList = new ArrayList<Double>();
		Cell cell;
		while (iterator.hasNext()) {
			Row currentRow = iterator.next();
			cellIterator = currentRow.cellIterator();
			cell = (Cell) cellIterator.next();
			productName = cell.getStringCellValue();
			if (productName != null || productName != "") {
				while (cellIterator.hasNext()) {
					cell = (Cell) cellIterator.next();
					priceList.add(cell.getNumericCellValue() * conversion);
				}
				productList.put(productName, priceList);
			}
		}
		return productList;
	}
}
