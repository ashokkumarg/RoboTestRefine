
package android_ios_comparison;

import java.io.File;
import java.io.FileInputStream; 
import java.io.FileOutputStream; 
import java.io.IOException;
 


import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell; 
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row; 
import org.apache.poi.ss.usermodel.Sheet; 
import org.apache.poi.ss.usermodel.Workbook;

import config.Constants;
import utility.ExcelUtils;

/**
 * This class describes about comparing the two excel sheets,rows,cells between Android workbook and iOS workbook.
 * With this comparison the matched, mismatched test cases will be written in the Mismatched file. The Hyper link
 * will be created to all the test case IDs clicking on the link will redirects to the concerned data sheet. 
 */

public class Android_iOS_Comparison {
	
	static Workbook androidWorkbook=null;
	static Workbook iosWorkbook=null;
	static Workbook mismatchedWorkbook=null;
	
	
	static Font androidFontStyle=null;
	static Font iosFontStyle=null;
	
	static CellStyle androidCellStyle=null;
	static CellStyle iosCellStyle=null;
	
	static Row row=null;
	
	private static CellStyle cellStyle = null;
	private static Font cellFont = null;
	
	static FileOutputStream mismatchedfos = null;
    static FileOutputStream demoAndroidfos = null;
    static FileOutputStream demoiOSfos = null;
    
    static FileInputStream androidTestResultFile = null;
    static FileInputStream iOSTestResultFile = null;
    static FileInputStream mismatchedFile = null;
    
	public static void main(String[] args) throws Exception
	{
		
		try {
			
			Workbook workbook = new HSSFWorkbook();
			workbook.createSheet(Constants.mismatchSheetName);
			FileOutputStream out = new FileOutputStream(new File(Constants.mismatchFilePath));
			
			workbook.write(out);
            out.close();
           
            // Get input excel files
            androidTestResultFile = new FileInputStream(new File(Constants.androidFilePath));
            iOSTestResultFile = new FileInputStream(new File(Constants.iosFilePath));
            mismatchedFile = new FileInputStream(new File(Constants.mismatchFilePath));
            
            // Create Workbook instance holding reference to .xls file
            androidWorkbook = new HSSFWorkbook(androidTestResultFile);
            iosWorkbook = new HSSFWorkbook(iOSTestResultFile);
            mismatchedWorkbook = new HSSFWorkbook(mismatchedFile);
            
            // Get the desired sheet from the workbook
            Sheet androidSheet = androidWorkbook.getSheetAt(1);
            Sheet iosSheet = iosWorkbook.getSheetAt(1);
            Sheet mismatchedSheet = mismatchedWorkbook.getSheetAt(0);
            
            // Comparing the sheets
            if(compareTwoSheets(androidSheet, iosSheet,mismatchedSheet)) {
                System.out.println("\n\nThe two excel sheets are Equal");
            } else {
                System.out.println("\n\nThe two excel sheets are Not Equal");
            }
            
            //Closing the files
            androidTestResultFile.close();
            iOSTestResultFile.close();
            mismatchedFile.close();
            
            writableHyperLink();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
    // Comparison between Android sheet vs iOS sheet.
    public static boolean compareTwoSheets(Sheet androidFileSheet, Sheet iosFileSheet, Sheet mismatchedFileSheet) throws Exception {
        int firstRow = androidFileSheet.getFirstRowNum();
        int lastRow = androidFileSheet.getLastRowNum();
        boolean equalSheets = true;
        
        if(firstRow==0)
        {
        	writeHeaderToMistmatchedFile(androidFileSheet,iosFileSheet,mismatchedFileSheet);
        	firstRow++;
        }
        
        for(int i=firstRow; i <= lastRow; i++) {
        	           
            System.out.println("\n\nComparing Row "+i);
            Row androidFileRow = androidFileSheet.getRow(i);
            Row iosFileRow = iosFileSheet.getRow(i);
            if(!compareTwoRows(androidFileRow, iosFileRow)) {
                equalSheets = false;
                System.out.println("Row "+i+" - Not Equal");
                writeMismatchedRowsToMismatchedfile(i,androidFileSheet,iosFileSheet,mismatchedFileSheet);
                //break;
            } else {
                System.out.println("Row "+i+" - Equal");
                writeMatchingRowsToMismatchedfile(i,androidFileSheet,iosFileSheet,mismatchedFileSheet);
            }
        }
        return equalSheets;
    }

    // Comparison between Android row vs iOS row.
    public static boolean compareTwoRows(Row androidFileRow, Row iosFileRow) throws Exception {
        if((androidFileRow == null) && (iosFileRow == null))
        {
            return true;
        }
        else if((androidFileRow == null) || (iosFileRow == null))
        {
            return false;
        }
        // Both android file and iosFile will have the same row.
        int firstCell = androidFileRow.getFirstCellNum();
        int lastCell = androidFileRow.getLastCellNum();
        boolean equalRows = true;
        
        // Compare all cells in a row
        for(int i=firstCell; i < lastCell; i++) {
            Cell androidFileCell = androidFileRow.getCell(i);
            Cell iosFileCell = iosFileRow.getCell(i);
            
            System.out.println("Android: "+androidFileCell.toString());
            System.out.println("ios: "+iosFileCell.toString());
            
            if(!compareTwoCells(androidFileCell, iosFileCell)) {
            	
                equalRows = false;
                System.out.println("Cell "+androidFileCell.toString()+" - Not Equal");
                
                //Android Style
                androidFontStyle = androidWorkbook.createFont();
                androidCellStyle = androidWorkbook.createCellStyle();
                androidFontStyle.setColor(IndexedColors.WHITE.index);
                androidCellStyle.setFont(androidFontStyle);
                androidCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
                androidCellStyle.setFillPattern(CellStyle.BIG_SPOTS);
                
                //Borders
                androidCellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
                androidCellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
                androidCellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
                androidCellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
                
                //iOS Style
                iosFontStyle = iosWorkbook.createFont();
                iosCellStyle = iosWorkbook.createCellStyle();
                iosFontStyle.setColor(IndexedColors.WHITE.index);
                iosCellStyle.setFont(iosFontStyle);
                iosCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
                iosCellStyle.setFillPattern(CellStyle.BIG_SPOTS);
                //Borders
                iosCellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
                iosCellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
                iosCellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
                iosCellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
                
                androidFileCell.setCellStyle(androidCellStyle);
                iosFileCell.setCellStyle(iosCellStyle);
                
                demoAndroidfos = new FileOutputStream(Constants.androidFilePath);
                androidWorkbook.write(demoAndroidfos);
                demoAndroidfos.close();//close output stream
                
                demoiOSfos= new FileOutputStream(Constants.iosFilePath);
                iosWorkbook.write(demoiOSfos);
                demoiOSfos.close(); //close output stream
                
            } else {
                
            	androidFontStyle = androidWorkbook.createFont();
                androidCellStyle = androidWorkbook.createCellStyle();
                androidFontStyle.setColor(IndexedColors.BLACK.index);
                androidCellStyle.setFont(androidFontStyle);
                androidCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                androidCellStyle.setBorderTop(CellStyle.BORDER_THIN);
                androidCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                androidCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                
                //iOS Style
                iosFontStyle = iosWorkbook.createFont();
                iosCellStyle = iosWorkbook.createCellStyle();
                iosFontStyle.setColor(IndexedColors.BLACK.index);
                iosCellStyle.setFont(iosFontStyle);
                iosCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                iosCellStyle.setBorderTop(CellStyle.BORDER_THIN);
                iosCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                iosCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                
                androidFileCell.setCellStyle(androidCellStyle);
                iosFileCell.setCellStyle(iosCellStyle);
                
                demoAndroidfos = new FileOutputStream(Constants.androidFilePath);
                androidWorkbook.write(demoAndroidfos);
                demoAndroidfos.close();//close output stream
                
                demoiOSfos= new FileOutputStream(Constants.iosFilePath);
                iosWorkbook.write(demoiOSfos);
                demoiOSfos.close(); //close output stream
                System.out.println("Cell "+i+" - Equal");
            }
        }
        return equalRows;
    }

    // Comparison between Android cell vs iOS cell.
    public static boolean compareTwoCells(Cell androidFileCell, Cell iosFileCell) {
        if((androidFileCell == null) && (iosFileCell == null)) {
            return true;
        } else if((androidFileCell == null) || (iosFileCell == null)) {
            return false;
        }
        
        boolean equalCells = false;
        int androidFileCellType = androidFileCell.getCellType();
        int iosFileCellType = iosFileCell.getCellType();
        if (androidFileCellType == iosFileCellType) {
            // Compare cells based on its type
            switch (androidFileCell.getCellType()) {
            case Cell.CELL_TYPE_FORMULA:
                if (androidFileCell.getCellFormula().equals(iosFileCell.getCellFormula())) {
                    equalCells = true;
                }
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (androidFileCell.getNumericCellValue() == iosFileCell
                        .getNumericCellValue()) {
                    equalCells = true;
                }
                break;
            case Cell.CELL_TYPE_STRING:
                if (androidFileCell.getStringCellValue().equals(iosFileCell
                        .getStringCellValue())) {
                    equalCells = true;
                }
                break;
            case Cell.CELL_TYPE_BLANK:
                if (iosFileCell.getCellType() == Cell.CELL_TYPE_BLANK) {
                    equalCells = true;
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                if (androidFileCell.getBooleanCellValue() == iosFileCell
                        .getBooleanCellValue()) {
                    equalCells = true;
                }
                break;
            case Cell.CELL_TYPE_ERROR:
                if (androidFileCell.getErrorCellValue() == iosFileCell.getErrorCellValue()) {
                    equalCells = true;
                }
                break;
            default:
                if (androidFileCell.getStringCellValue().equals(
                        iosFileCell.getStringCellValue())) {
                    equalCells = true;
                }
                break;   
            }
        }
        return equalCells;
    }
  
    //Write the Header mismatched file.
  	private static void writeHeaderToMistmatchedFile(Sheet androidFileSheet, Sheet iosFileSheet,Sheet mismatchedFileSheet) throws Exception {
  	
      	row = androidFileSheet.getRow(0);
      	int firstCell = row.getFirstCellNum();
          int lastCell = row.getLastCellNum();
          
          ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedFileSheet.getSheetName(),"Original Row Number", 0,0);
          for(int i=firstCell; i <= lastCell-1; i++)
          {
          	String data=ExcelUtils.readExcel(Constants.mismatchedPath,Constants.androidFileName, androidFileSheet.getSheetName(),0,i);
          	System.out.println(data);
          	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedFileSheet.getSheetName(),data,0,i+1);
          }
          
      	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedFileSheet.getSheetName(),"Mismatched Or Not?",0,9);
      	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedFileSheet.getSheetName(),"Platform(Android/iOS)",0,10);
      	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedFileSheet.getSheetName(),"Platform(Android/iOS)",0,11);   	
      	ExcelUtils.setHeaderRowStyle(Constants.mismatchedPath,Constants.mismatchFileName, mismatchedFileSheet.getSheetName(),0);
  	}
    
    //Write the mismatched rows between Android and iOS in the mismatched file
    private static void writeMismatchedRowsToMismatchedfile(int rowNum, Sheet androidFileSheet, Sheet iosFileSheet,Sheet mismatchedSheet) throws Exception {
		// TODO Auto-generated method stub

    	if(ExcelUtils.readExcel(Constants.mismatchedPath,Constants.mismatchFileName, mismatchedSheet.getSheetName(),Constants.rowToWrite-1,9)!=null)
    	{
    		//When the rows are mismatches new empty row is added
    		Constants.rowToWrite=Constants.rowToWrite+1;
    	}
    	
    	//Android file and ios file will have same rows
     	row = androidFileSheet.getRow(rowNum);
    	int firstCell = row.getFirstCellNum();
        int lastCell = row.getLastCellNum();
        
        ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(),String.valueOf(rowNum),Constants.rowToWrite,0);
        ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(),String.valueOf(rowNum),Constants.rowToWrite+1,0);
        
        for(int i=firstCell; i <= lastCell-1; i++)
        {    	
        	String androidData=ExcelUtils.readExcel(Constants.mismatchedPath,Constants.androidFileName, androidFileSheet.getSheetName(),rowNum,i);
        	String iosData=ExcelUtils.readExcel(Constants.mismatchedPath,Constants.iosFileName, iosFileSheet.getSheetName(),rowNum,i);
        	
        	System.out.println("row to write: "+Constants.rowToWrite);
        	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(),androidData,Constants.rowToWrite,i+1);
        	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(),iosData,Constants.rowToWrite+1,i+1);
        }
        
    	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(),"MissMatching Results",Constants.rowToWrite,9);
    	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(),"Android",Constants.rowToWrite,10);
    	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(),"",Constants.rowToWrite,11);
    	ExcelUtils.setMismatchingRowStyle(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(),Constants.rowToWrite);
    	
    	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(),"MissMatching Results",Constants.rowToWrite+1,9);
    	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(),"",Constants.rowToWrite+1,10);
    	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(),"iOS",Constants.rowToWrite+1,11);
    	ExcelUtils.setMismatchingRowStyle(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(),Constants.rowToWrite+1);
    	
    	//To skip the two rows of mismatched rows
    	Constants.rowToWrite=Constants.rowToWrite+3;
      }
    
  	//Write the matching rows between Android and iOS in the mismatched file.
    private static void writeMatchingRowsToMismatchedfile(int rowNum, Sheet androidFileSheet, Sheet iosFileSheet,Sheet mismatchedSheet) throws Exception {
		// TODO Auto-generated method stub
    	row = androidFileSheet.getRow(rowNum);
    	int firstCell = row.getFirstCellNum();
        int lastCell = row.getLastCellNum();
        
        ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(), String.valueOf(rowNum), Constants.rowToWrite, 0);
       
        for(int i=firstCell; i <= lastCell-1; i++)
        {    	
        	String data=ExcelUtils.readExcel(Constants.mismatchedPath,Constants.androidFileName, androidFileSheet.getSheetName(),rowNum,i);
        	System.out.println("Data:"+data);
           	System.out.println("row to write: "+Constants.rowToWrite);
        	
        	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(), data,Constants.rowToWrite,i+1);
        }
        
    	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(),"Matching Results",Constants.rowToWrite,9);
    	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(),"Android",Constants.rowToWrite,10);
    	ExcelUtils.createCellAndWrite(Constants.mismatchedPath,Constants.mismatchFileName,mismatchedSheet.getSheetName(),"iOS",Constants.rowToWrite,11);
    	ExcelUtils.setMatchingRowStyle(Constants.mismatchedPath,Constants.mismatchFileName, mismatchedSheet.getSheetName(),Constants.rowToWrite);
    	
    	//When the rows are matches, new empty row is denied.
    	Constants.rowToWrite=Constants.rowToWrite+1;
      }

	/*
	 * This method is used for creating Hyper link on all the Mismatched test case ID.
	 * Clicking on the link will redirects to the concerned data sheet. 
	 */
	private static void writableHyperLink() throws Exception {
		// TODO Auto-generated method stub
		
        Sheet mismatchedSheet = mismatchedWorkbook.getSheet(Constants.mismatchSheetName);
	    Sheet demoAndroidSheetName = androidWorkbook.getSheet(Constants.testSheetName);
	    
	    int mismatchedTotalRows = mismatchedSheet.getLastRowNum();
	    int demoAndroidTotalRows = demoAndroidSheetName.getLastRowNum();
	    
	    String hyperlinkPath="file:///D:/MyWorkspace/RobotTest_Refine/src/comparison_output/DemoTestcaseAndData_Android.xls#";
	    
	    for(int i=1;i<=mismatchedTotalRows;i++)
	    {
	    	String testCaseID=ExcelUtils.readExcel(Constants.mismatchedPath,Constants.mismatchFileName,Constants.mismatchSheetName,i,2);
	    	String screenName=ExcelUtils.readExcel(Constants.mismatchedPath,Constants.mismatchFileName,Constants.mismatchSheetName,i,8);
	    	if(screenName==null){
	    		continue;
	    	}
	    	
	    	for(int j=1;j<=demoAndroidTotalRows;j++)
	    	{
	    		String  testCaseIDDemoFile=ExcelUtils.readExcel(Constants.mismatchedPath,Constants.androidFileName,screenName, j,0);
	    		if(testCaseID.equals(testCaseIDDemoFile)){
	    			
	    			Cell cell = mismatchedSheet.getRow(i).getCell(2);
	    			HSSFHyperlink fileLink= new HSSFHyperlink(HSSFHyperlink.LINK_FILE);
	    			String cellNo= "A"+(j+1);
	    		    fileLink.setAddress(hyperlinkPath+screenName+"!"+cellNo);
	    			cell.setHyperlink(fileLink);
	    			
	    			cellFont=mismatchedWorkbook.createFont();
	    			cellFont.setColor(IndexedColors.BLUE.index);
	    			cellFont.setUnderline(Font.U_SINGLE);
	    			
	    			cellStyle=mismatchedWorkbook.createCellStyle();
	    			cellStyle.setFont(cellFont);
	    			cell.setCellStyle(cellStyle);
    			}
	    		else{
	    			System.out.println("TestCase ID's are Not equal");
	    		}
	    	}
	    }
	    
	    try {
	        mismatchedfos = new FileOutputStream(Constants.mismatchFilePath);
	        mismatchedWorkbook.write(mismatchedfos);
	        
	        demoAndroidfos = new FileOutputStream(Constants.androidFilePath);
	        androidWorkbook.write(demoAndroidfos);
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (mismatchedfos != null || demoAndroidfos !=null) {
	            try {
	                mismatchedfos.flush();
	                mismatchedfos.close();
	                demoAndroidfos.flush();
	                demoAndroidfos.close();            
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}    
}
