package ru.greg3d.ciansearchtest.helpers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.greg3d.helpers.model.BaseModel;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class XlsxHelper {

    private File file;
    private XSSFWorkbook excelBook;

    private Map<String, Integer> filtersHeadersMap;

    private int currentRowNum = 0;

    public boolean haveNextRow(){
        return this.currentSheet.getLastRowNum() > currentRowNum;
    }

    public boolean nextRow(){
        if(haveNextRow()){
            this.currentRowNum ++;
            return true;
        }
        return false;
    }

    private XlsxHelper(File file){
        this.file = file;
    }

    public static XlsxHelper getInstance(File file) throws IOException {
        XlsxHelper helper = new XlsxHelper(file);
        helper.excelBook = new XSSFWorkbook(new FileInputStream(file));
        helper.currentSheet = helper.excelBook.getSheetAt(0);
        helper.getHeadersMap();
        helper.currentRowNum = 0;
        return helper;
    }

    public static XlsxHelper newInstance(File file) throws IOException {
        XlsxHelper helper = new XlsxHelper(file);
        helper.excelBook = new XSSFWorkbook();
        helper.file = file;
        helper.currentSheet = helper.excelBook.createSheet("filters");
        return helper;
    }

    public void addSheet(String sheetName){
        this.currentSheet = this.excelBook.createSheet(sheetName);
        this.filtersHeadersMap = null;
    }

    public Map<String, Integer> getHeadersMap(){
        if(filtersHeadersMap != null)
            return filtersHeadersMap;

        filtersHeadersMap = new HashMap<>();
        Iterator<Cell> cells = currentSheet.getRow(0).cellIterator();
        while(cells.hasNext()){
            Cell cell = cells.next();
            filtersHeadersMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }
        return filtersHeadersMap;
    }


    public <T extends BaseModel> T fillModel(Class<T> clazz){
        try {
            return fillModel(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T extends BaseModel> T fillModel(T filter){
        for (String key : getHeadersMap().keySet()) {
            Cell cell = currentSheet.getRow(currentRowNum).getCell(getHeadersMap().get(key));
            try {
                CellType type = cell.getCellTypeEnum();
                if (type.equals(CellType.NUMERIC))
                    filter.setValueByAlias(key, cell.getNumericCellValue());
                else if (type.equals(CellType.STRING))
                    filter.setValueByAlias(key, cell.getStringCellValue());
            }catch(NullPointerException e){}
        }
        return filter;
    }

    private XSSFSheet currentSheet;

    public void addRow(){
        addRow(new BaseModel());
    }

    public <T extends BaseModel> void addRow(T model){
        int lastRowNum = currentSheet.getLastRowNum();

        XSSFRow row = currentSheet.createRow(lastRowNum + 1);

        try {
            for (String key : getHeadersMap().keySet()) {
                Field modelField = model.getFieldByAlias(key);
                try {
                    if (modelField.getType().equals(Double.class))
                        row.createCell(getHeadersMap().get(key), CellType.NUMERIC)
                                .setCellValue(Double.valueOf(modelField.get(model).toString()));
                    else if (modelField.getType().equals(String.class))
                        row.createCell(getHeadersMap().get(key), CellType.STRING)
                                //.setCellValue(Optional.ofNullable(modelField.get(model)).orElse("").toString());
                                .setCellValue(Optional.ofNullable(modelField.get(model)).orElse("").toString());
                }catch (NullPointerException e) {
                    // TODO - рассмотреть обработку null-ов
                    System.out.println(String.format("Der Null [%s] ", key, modelField ));
                }
            }
        }catch (IllegalAccessException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T extends BaseModel> void addHeaders(Class<T> clazz) throws IOException {
        try {
            addHeaders(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public <T extends BaseModel> void addHeaders(T model) throws IOException {
        int lastRowNum = currentSheet.getLastRowNum();

        XSSFRow row = currentSheet.createRow(lastRowNum);

        int index = 0;
        for (String fieldName : model.getFieldMap().keySet()) {
            row.createCell(index ++, CellType.STRING)
                    .setCellValue(fieldName);
        }
    }

    public void write() throws IOException {
        excelBook.write(new FileOutputStream(this.file));
    }
}
