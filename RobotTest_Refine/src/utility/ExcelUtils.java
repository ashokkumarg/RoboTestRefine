package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/** This class is used to get the total number of row and columns from the excel sheet, to read the values from the excel sheet,
 * to write the values in the excel sheet, to create a new cell and write the data's in the excel sheet, applying the styles
 * for matching and mismatching row.
 */

public class ExcelUtils {
	
	private static File file = null;
	private static FileInputStream inputStream = null;
	private static FileOutputStream outputStream = null;
	
	public static Workbook workbook =null;
	private static Sheet sheet = null;
	private static Row row = null;
	private static Cell cell =null;
	
	private static CellStyle cellStyle = null;
	private static Font font =null;
	
	private static int totalRows=0;
	private static int totalCols=0;
	
	
	//To get the sheet name from workbook
	public static Sheet getSheet(String filePath,String fileName,String sheetName){
	
		try {
			file =    new File(filePath+fileName);
		    inputStream = new FileInputStream(file);     
		    String fileExtensionName = fileName.substring(fileName.indexOf("."));
		 
		     if(fileExtensionName.equals(".xlsx")){
		    	 workbook = new XSSFWorkbook(inputStream);
		    }
		     else if(fileExtensionName.equals(".xls")){
		         workbook = new HSSFWorkbook(inputStream);
		    }     
		     sheet = workbook.getSheet(sheetName);
		}
		catch(Exception ex){
			ex.getStackTrace();
		}
		
		return sheet;
	}
	
	
	//To get the total number of row in the excel sheet
	public static int getNumberOfRows(String filePath,String fileName,String sheetName){

	try {
	     sheet=getSheet(filePath, fileName, sheetName);
	     totalRows = sheet.getLastRowNum();    
	    }
		catch(Exception ex){
			System.out.println(ex.getStackTrace());
		}
	 return totalRows;
	}
	
	//To get the total number of columns in the excel sheet
	public static int getNumberOfCols(String filePath,String fileName,String sheetName){
	
	try {
	
	       sheet=getSheet(filePath, fileName, sheetName);
		   totalRows = sheet.getLastRowNum(); 
		   row=sheet.getRow(1);
		   totalCols=row.getLastCellNum();
    	 }
		catch(Exception ex){
			System.out.println(ex.getStackTrace());
		}
	return totalCols;
	}

	//To write the data in the concerned cell
	public static void writeExcel(String filePath,String fileName,String sheetName,String dataToWrite,int RowNumber,int ColNumber) throws IOException{
	
	try{
		   sheet= getSheet(filePath, fileName, sheetName);
		    cell = sheet.getRow(RowNumber).getCell(ColNumber);   
		    cell.setCellValue(dataToWrite);
		    inputStream.close();
		 
		    outputStream = new FileOutputStream(file);
		    workbook.write(outputStream);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getStackTrace());
		}
		finally{
			outputStream.flush();
		    outputStream.close();
		}
	}

	//To read the data from the concerned cell
	public static String readExcel(String filePath,String fileName,String sheetName,int RowNumber,int ColNumber) throws Exception{

		Object result = null;
		try
		{
		
			sheet=getSheet(filePath, fileName, sheetName);
		    row=sheet.getRow(RowNumber);
		    
		    if(row != null)
		    {
		    	System.out.println("Row is not empty");
		    	cell= row.getCell(ColNumber);
		    	
		    	switch (cell.getCellType()) {
		    	
				case Cell.CELL_TYPE_NUMERIC:// numeric value in excel
					result = cell.getNumericCellValue();
					break;
					
				case Cell.CELL_TYPE_STRING: // string value in excel
					result = cell.getStringCellValue();
					break;
					
				case Cell.CELL_TYPE_BOOLEAN: // boolean value in excel
					result = cell.getBooleanCellValue();
					break;
					
				case Cell.CELL_TYPE_BLANK: // blank value in excel
					result = cell.getStringCellValue();
					break;
				
				case Cell.CELL_TYPE_ERROR: // Error value in excel
					result = cell.getErrorCellValue()+"";
					break;
		    	}
		    }
		    else
		    {
		    	System.out.println("Row is empty");
		    	return null;
		    }
		    System.out.println(RowNumber +" "+ ColNumber+ " in "+ sheetName);
		    inputStream.close(); 
		}
		
		catch (Exception ex){
			ex.printStackTrace();
			}
			return result.toString();
	    	
		}
	
	//Create a new and write the concerned data in the excel sheet.
	public static void createCellAndWrite(String filePath,String fileName,String sheetName,String dataToWrite,int RowNumber,int ColNumber) throws Exception
	{
	
		sheet=getSheet(filePath, fileName, sheetName);
		
		if(ColNumber==0)
		{
		    row = sheet.createRow(RowNumber);
		    cell = row.createCell(ColNumber);
	        cell.setCellValue(dataToWrite);
            sheet.autoSizeColumn(ColNumber);
 	      		        
		 }
		 else
		 {
			 row = sheet.getRow(RowNumber); 
			 cell = row.createCell(ColNumber);			    
		     cell.setCellValue(dataToWrite);
		     sheet.autoSizeColumn(ColNumber);
		  
		 }
	    System.out.println("datatowrite: "+dataToWrite);
	    System.out.println("Row no: "+RowNumber);
	    System.out.println("Col no: "+ColNumber);
	    inputStream.close();
    
	    try {
	        outputStream = new FileOutputStream(file);
	        workbook.write(outputStream);
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (outputStream != null) {
	            try {
	            	outputStream.flush();
	            	outputStream.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	 }
	
	//Change the row style  for mismatched results
	public static void setMismatchingRowStyle(String filePath,String fileName,String sheetName,int RowNumber) throws Exception
	{
	
		getSheet(filePath, fileName, sheetName);
		row = sheet.getRow(RowNumber);
	    font = workbook.createFont();
	    cellStyle = workbook.createCellStyle();
	    
	    font.setColor(IndexedColors.BLACK.index);
	    cellStyle.setFont(font);
	    cellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.index);
	    cellStyle.setFillPattern(CellStyle.BIG_SPOTS);
	    
	    cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
	    
	    int firstCell = row.getFirstCellNum();
	    int lastCell = row.getLastCellNum();
	    
	    for(int i=firstCell; i < lastCell; i++) {
	    	cell=row.getCell(i);	 
	    	cell.setCellStyle(cellStyle);
		}
	    inputStream.close();
	    
	    try {
	        outputStream = new FileOutputStream(file);
	        workbook.write(outputStream);
	    	}
	    catch (IOException e) {
	        e.printStackTrace();
	    	}
	    finally {
	    if (outputStream != null) {
		    try {
		    		outputStream.flush();
		            outputStream.close();
		        }
		    	catch (IOException e) {
	            e.printStackTrace();
		        }
	    	}
	    }
	}
	
	//A particular style is applied to the matching row.
	public static void setMatchingRowStyle(String filePath,String fileName, String sheetName,int RowNumber) throws Exception
	{
		getSheet(filePath, fileName, sheetName);
		
	    row = sheet.getRow(RowNumber);
	    font = workbook.createFont();
	    cellStyle = workbook.createCellStyle();
	    font.setColor(IndexedColors.BLACK.index);
	    cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
	    cellStyle.setFont(font);
	    
	    int firstCell = row.getFirstCellNum();
	    int lastCell = row.getLastCellNum();
	    
	    for(int i=firstCell; i < lastCell; i++) {
	    	cell=row.getCell(i);
	    	cell.setCellStyle(cellStyle);
		}
	    inputStream.close();
	    
	    try {
	        outputStream = new FileOutputStream(file);
	        workbook.write(outputStream);
	    	}
	    catch (IOException e) {
	        e.printStackTrace();
	    	}
	    finally {
	    if (outputStream != null) {
		    try {
		    		outputStream.flush();
		            outputStream.close();
		        }
		    	catch (IOException e) {
	            e.printStackTrace();
		        }
	    	}
	    }
	}

	//To read the cell style from the excel sheet.
	public static CellStyle readCellStyle(String filePath,String fileName,String sheetName,int RowNumber, int ColNumber) throws Exception {
		
		// TODO Auto-generated method stub
		getSheet(filePath, fileName, sheetName);
	    cell = sheet.getRow(RowNumber).getCell(ColNumber);
	    cellStyle=cell.getCellStyle();		
		return cellStyle;
		}

	public static void setHeaderRowStyle(String filePath,String fileName, String sheetName,int RowNumber) throws Exception
	{
		sheet=getSheet(filePath, fileName, sheetName);
		
	    row = sheet.getRow(RowNumber);
	    font = workbook.createFont();
	    cellStyle = workbook.createCellStyle();
	    font.setColor(IndexedColors.BLACK.index);
	    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
	    cellStyle.setFont(font);
	    
	    int firstCell = row.getFirstCellNum();
	    int lastCell = row.getLastCellNum();
	    
	    for(int i=firstCell; i < lastCell; i++) {
	    	cell=row.getCell(i);	 
	    	cell.setCellStyle(cellStyle);
		}
	    inputStream.close();
	    
	    try {
	        outputStream = new FileOutputStream(file);
	        workbook.write(outputStream);
	    	}
	    catch (IOException e) {
	        e.printStackTrace();
	    	}
	    finally {
	    if (outputStream != null) {
		    try {
		    		outputStream.flush();
		            outputStream.close();
		        }
		    	catch (IOException e) {
	            e.printStackTrace();
		        }
	    	}
	    }
	}
}
