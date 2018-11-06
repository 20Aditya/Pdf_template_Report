package com.example.aditya.pdf_report;

import android.os.Environment;
import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static android.content.ContentValues.TAG;

public class CreateExcel {

    static XSSFRow row = null;
    static XSSFCell cell = null;
    static String[] headers = null;
    static int rowNum = 0;
    static int colNum = 0;
    static CellStyle cellStyle = null;
    static CellStyle headerStyle = null;
    static XSSFFont font = null;
    static CellStyle datecellStyle = null;

    public static void createExcel()
    {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Reports Generated Data");
        headers = new String[] {"S.No", "Branch Visit Reports", "Group Initiatives"};
        row = sheet.createRow(rowNum);
        font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);


        headerStyle = workbook.createCellStyle();
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        headerStyle.setFillForegroundColor((short) 200);
        headerStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setFont(font);



        cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);


        for (String header : headers) {
            cell = row.createCell(colNum);
            cell.setCellValue(header);
            cell.setCellStyle(headerStyle);
            ++colNum;
        }



        Map<String, Object[]> data = new TreeMap<String, Object[]>();



        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report");
        if(file.isDirectory())
            Log.d(TAG, "createExcel: " + file.isDirectory());
        else
        {
            file.mkdir();
            Log.d(TAG, "createExcel: " + file.isDirectory());
        }


        File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report/branch_visit_reports");
        if(file2.isDirectory())
            Log.d(TAG, "createExcel: " + file2.isDirectory());
        else
        {
            file2.mkdir();
            Log.d(TAG, "createExcel: " + file2.isDirectory());
        }

        File file3 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report/group_initiatives");
        if(file3.isDirectory())
            Log.d(TAG, "createExcel: " + file3.isDirectory());
        else
        {
            file3.mkdir();
            Log.d(TAG, "createExcel: " + file3.isDirectory());
        }


        File[] files = file2.listFiles();
        File[] files1 = file3.listFiles();

        if(files.length >= files1.length) {

            for (int i = 0; i < files1.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
                data.put(String.valueOf(i+1), new Object[] {i+1,files[i].getName(),files1[i].getName()});
            }

            for(int i=files1.length;i<files.length;i++)
                data.put(String.valueOf(i+1), new Object[] {i+1,files[i].getName(),""});
        }
        else
        {
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
                data.put(String.valueOf(i+1), new Object[] {i+1,files[i].getName(),files1[i].getName()});
            }

            for(int i=files.length;i<files1.length;i++)
                data.put(String.valueOf(i+1), new Object[] {i+1,"",files1[i].getName()});
        }


        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 1;
        for (String key : keyset)
        {
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr)
            {
                Cell cell = row.createCell(cellnum++);
                if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Integer)
                    cell.setCellValue((Integer)obj);
            }
        }

        for (int i = 1; i < headers.length; i++) {
            sheet.setColumnWidth(i,10000);
        }

        sheet.createFreezePane(1, 1);

        try
        {
            //Write the workbook in file system
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report");
            if(f.isDirectory())
                Log.d("Main", "writetopdf: " + f.isDirectory());
            else {
                f.mkdir();
                Log.d("Main", "writetopdf: " + f.isDirectory());
            }


            File f1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf_report/" + "Reports Data" + ".xlsx");


            FileOutputStream out = new FileOutputStream(f1);

            workbook.write(out);
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
