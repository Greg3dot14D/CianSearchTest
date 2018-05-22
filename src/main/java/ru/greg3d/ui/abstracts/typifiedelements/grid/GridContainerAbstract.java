package ru.greg3d.ui.abstracts.typifiedelements.grid;

import ru.greg3d.ui.abstracts.typifiedelements.cell.CellAbstract;
import ru.greg3d.ui.htmlelements.elements.HtmlElementExtended;
import ru.greg3d.ui.interfaces.ICheck;
import ru.greg3d.ui.interfaces.IGrid;
import ru.greg3d.ui.interfaces.IGridContainer;
import ru.greg3d.ui.interfaces.IGridable;
import ru.greg3d.helpers.model.BaseModel;
import ru.yandex.qatools.htmlelements.annotations.Name;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SBT-Konovalov-GV on 11.07.2017.
 */
public abstract class GridContainerAbstract extends HtmlElementExtended implements IGridContainer {
    public abstract IGrid getGrid();

    @Override
    public void setGrid(){
        Field[] fields = this.getClass().getDeclaredFields();

        for(Field f: fields) {
            f.setAccessible(true);
            try {
                if(f.get(this) instanceof CellAbstract){
                    ((IGridable)f.get(this)).setGrid(this.getGrid());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, Field> fieldMap;

    private Map<String, Field> getFieldMap(){
        if(this.fieldMap == null){
            this.fieldMap = new HashMap<>();
            for(Field f: this.getClass().getFields()){
                if(f.isAnnotationPresent(Name.class))
                    this.fieldMap.put(f.getAnnotation(Name.class).value(), f);
            }
        }
        return this.fieldMap;
    }



    public <T extends BaseModel> T deserializeRow(Class<T> clazz){
        try {
//            return deserializeRow(clazz.newInstance());
            return deserializeRowSplit(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(String.format("Can't create instance of class [%s]\n%s", clazz.getName(), e.getMessage()));
        }
    }

    public <T extends BaseModel> T deserializeRow(T o){
        Map<String, Field> m = o.getFieldMap();

        for(String key: m.keySet()){
            Field modelField = o.getFieldByAlias(key);
            try {
                CellAbstract c = (CellAbstract)getFieldByName(key).get(this);

                    try {
                        //Date date = new SimpleDateFormat(c.getDateFormat(), c.getLocale()).parse(c.getText());
                        //modelField.set(o,date);
                        String cellText = c.getText();
                        if(!"".equals(cellText) && modelField.getType().equals(Date.class))
                            modelField.set(o,new SimpleDateFormat(c.getDateFormat(), c.getLocale()).parse(cellText));
                        else if(!"".equals(cellText) && modelField.getType().equals(Float.class))
                            modelField.set(o, Float.valueOf(cellText));
                        else if(modelField.getType().equals(String.class))
                            modelField.set(o,(c).getText());
                        else if(modelField.getType().equals(Boolean.class))
                            modelField.set(o,((ICheck)c).isChecked());
                    } catch (ParseException e) {
                        //e.printStackTrace();
                        throw new RuntimeException(e.getMessage());
                    }
            //} catch (IllegalAccessException e) {
            } catch (IllegalAccessException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return o;
    }

    public <T extends BaseModel> T deserializeRowSplit(T o){
        Map<String, Field> m = o.getFieldMap();

        List<String> headers = getGrid().getHeaders().getStringList();

        String [] row = getGrid().getRows().getList().get(getGrid().getCurrentRowNum()).getText().split("\n");

        int split = 0;

        for(int i=0; i < headers.size(); i ++){
            String key = headers.get(i);

            Field modelField = o.getFieldByAlias(key);
            try {
                try {
                    if("".equals(key)){
                        if(modelField.getType().equals(Boolean.class)) {
                            CellAbstract c = (CellAbstract) getFieldByName(key).get(this);
                            modelField.set(o, ((ICheck)c).isChecked());
                        }
                        split --;
                    }
                    else{
                        String cellText = row[i + split];

                        if(!"".equals(cellText) && modelField.getType().equals(Date.class)) {
                            CellAbstract c = (CellAbstract)getFieldByName(key).get(this);
                            modelField.set(o, new SimpleDateFormat(c.getDateFormat(), c.getLocale()).parse(cellText));
                        }
                        else if(!"".equals(cellText) && modelField.getType().equals(Float.class))
                            modelField.set(o, Float.valueOf(cellText));
                        else if(modelField.getType().equals(String.class))
                            modelField.set(o, cellText);
                        else if(modelField.getType().equals(Boolean.class)) {
                            CellAbstract c = (CellAbstract) getFieldByName(key).get(this);
                                modelField.set(o, ((ICheck)c).isChecked());
                        }
                    }
                } catch (ParseException e) {
                    //e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                }
                //} catch (IllegalAccessException e) {
            } catch (IllegalAccessException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return o;
    }

//    private String getCellValueByName(String name){
//        List<WebElement> elementList = this.grid.getRow();
//        Map<String, Integer> headersMap = this.grid.getHeadersMap();
//
//        return elementList.get(headersMap.get(name)-1).getText();
//    }

    private Field getFieldByName(String name){
        return this.getFieldMap().get(name);
//        Field[] fields = this.getClass().getFields();
//        for(Field f: fields){
//            if(f.isAnnotationPresent(Name.class))
//                if(f.getAnnotation(Name.class).value().equals(name))
//                    return f;
//        }
//        throw new RuntimeException(String.format("No such field [%s]exception", name));
    }

    @Override
    public String toString(){
        return this.getGrid().toString();
    }
}
