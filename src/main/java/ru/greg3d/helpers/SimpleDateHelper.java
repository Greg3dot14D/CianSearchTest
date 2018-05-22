package ru.greg3d.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by SBT-Konovalov-GV on 08.07.2016.
 *
 * Хелпер - позволяет корректировать дату относительно текущей и выводит значение в нужном текстовом формате
 */
public class SimpleDateHelper {

    private Calendar calendar;
    private String currentDateFormat;

    private static final String dateFormat = "dd.MM.yyyy";
    public static final String dateTimeFormat = "dd.MM.yyyy HH:mm:ss";

    private static final String oracleDateFormat = "MM/dd/yyyy";
    private static final String oracleDateTimeFormat = "MM/dd/yyyy HH:mm:ss";

    private String inputDateTimeValue = "";

    public SimpleDateHelper(String dateFormat){
        this.calendar = Calendar.getInstance();
        this.currentDateFormat = dateFormat;
    }

    public Calendar getCalendar(){
        return this.calendar;
    }

    public SimpleDateHelper setFormat(String format){
        this.currentDateFormat = format;
        return this;
    }

    public static SimpleDateHelper getInstance(String dateFormat){
        return new SimpleDateHelper(dateFormat);
    }

    public static SimpleDateHelper getInstance(){
        return new SimpleDateHelper(dateTimeFormat);
    }


    public SimpleDateHelper setDateTime(String dateTime){
        try {
            this.calendar.setTime(new SimpleDateFormat(this.currentDateFormat).parse(dateTime));
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
        return this;
    }

    public SimpleDateHelper setDateTime(Date date){
        this.calendar.setTime(date);
        this.setDateTime(toString());
        return this;
    }

    public SimpleDateHelper setDateTime(long timeInMillis){
        this.calendar.setTimeInMillis(timeInMillis);
        this.setDateTime(toString());
        return this;
    }

    public static SimpleDateHelper getCurrentDate(String dateFormat){
        return new SimpleDateHelper(dateFormat);
    }


    // создаем объект, которые возвращает дату в формате "dd.MM.yyyy HH:mm:ss"
    public static SimpleDateHelper getCurrentDateTime(){
        return new SimpleDateHelper(dateTimeFormat);
    }

    // создаем объект, которые возвращает дату в формате "dd.MM.yyyy"
    public static SimpleDateHelper getCurrentDate(){
        return new SimpleDateHelper(dateFormat);
    }

    // new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Date.parse("07/12/2016 14:36:08")));
    // приводим строковую дату к нужному нам формату
    public static String parseToDate(String dateTime){
        if(dateTime.contains("/"))
            return (new SimpleDateFormat(dateFormat).format(Date.parse(dateTime)));
        else
            return dateTime.substring(0,10);
    }

    // приводим строковую дату к нужному нам формату
    public static String parseToDateTime(String dateTime){
        if(dateTime.contains("/"))
            return (new SimpleDateFormat(dateTimeFormat).format(Date.parse(dateTime)));
        else
            return dateTime;
    }

    // приводим строковую дату к нужному нам формату
    public static String parseToDateFormat(String dateTime){
        //return String.format("%s/%s/%s",dateTime.substring(3,5),dateTime.substring(0,2), dateTime.substring(6));
        return new SimpleDateHelper(dateFormat).setDateTime(dateTime).setFormat(oracleDateFormat).toString();
    }

    // переводит дату/время объекта в строковый формат
    public String toString(){
        //return new SimpleDateFormat(this.currentDateFormat).format(this.calendar.getTime()).substring(0,this.currentDateFormat.length());
        return new SimpleDateFormat(this.currentDateFormat).format(this.calendar.getTime());
    }

    public Date toDate(){
        return this.getCalendar().getTime();
    }

    public long toMillis(){
        return this.getCalendar().getTimeInMillis();
    }

    // cal.add(Calendar.DAY_OF_YEAR, 30);
    // добавить к текущей дате день/дни
    public SimpleDateHelper addDay(){
        return addDay(1);
    }

    public SimpleDateHelper addDay(int increment){
        calendar.add(Calendar.DAY_OF_YEAR, increment);
        return this;
    }

    public SimpleDateHelper addMonth(){
        return addMonth(1);
    }

    public SimpleDateHelper addMonth(int increment){
        calendar.add(Calendar.MONTH, increment);
        return this;
    }

    // добавить к текущему времени час/ы
    public SimpleDateHelper addHour(){return addHour(1);}

    public SimpleDateHelper addHour(int increment){
        calendar.add(Calendar.HOUR_OF_DAY, increment);
        return this;
    }

    // добавить к текущей дате год/ы
    public SimpleDateHelper addYear(){
        return addYear(1);
    }

    /**
     * Метод по добавлению года к значению типа Calendar
     * @param increment - число, на сколько увеличить
     * @return
     */
    public SimpleDateHelper addYear(int increment){
        calendar.add(Calendar.YEAR, increment);
        return this;
    }

    // добавить к текущей дате минуту/ы
    public SimpleDateHelper addMinute(){
        return addMinute(1);
    }

    /**
     * Метод по добавлению минут к значению типа Calendar
     * @param increment - число, на сколько увеличить
     * @return
     */
    public SimpleDateHelper addMinute(int increment){
        calendar.add(Calendar.MINUTE, increment);
        return this;
    }

    // добавить к текущей дате минуту/ы
    public SimpleDateHelper addSecond(){
        return addSecond(1);
    }

    /**
     * Метод по добавлению секунд к значению типа Calendar
     * @param increment - число, на сколько увеличить
     * @return
     */
    public SimpleDateHelper addSecond(int increment){
        calendar.add(Calendar.SECOND, increment);
        return this;
    }

    //сравнение двух стринговых дат по условию МЕНЬШЕ погрешности
    public static Boolean compareToDateLess(String timeStamp, String dateTime, int condition){
        if (timeStamp.compareTo(dateTime)<condition)
            return true;
        return false;
    }

    /**сравнение двух стринговых дат по условию БОЛЬШЕ погрешности
     *
     * @param timeStamp
     * @param dateTime
     * @param condition
     * @return
     */
    public static Boolean compareToDateMore(String timeStamp, String dateTime, int condition){
        if (timeStamp.compareTo(dateTime)>condition)
            return true;
        return false;
    }

    private static SimpleDateHelper DateTimeOfPattern(String dateTime, String dateFormatPattern){
        SimpleDateHelper sd = new SimpleDateHelper(dateFormatPattern);
        try {
            sd.calendar.setTime(new SimpleDateFormat(dateFormatPattern).parse(dateTime));
            sd.inputDateTimeValue = dateTime;
        }catch(Exception e){}

        return sd;
    }

    // проверяем валидность формата только для формата типа dateTimeFormat и dateFormat
    //  если формат другой, всегда возвращаем false
    public boolean formatIsValid(){
        String pattern = "";
        switch (currentDateFormat){
            case
                dateTimeFormat :
                    pattern = "[0-3][0-9].[0-1][0-9].[1-2][0-9][0-9][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9]";
                    break;
            case
                dateFormat :
                    pattern = "[0-3][0-9].[0-1][0-9].[1-2][0-9][0-9][0-9]";
                    break;
            default :
                  return false;
        }
        return this.inputDateTimeValue.matches(pattern);
    }

    public static SimpleDateHelper DateOf(String dateTime){
        return DateTimeOfPattern(dateTime, dateFormat);
    }

    public static SimpleDateHelper DateTimeOf(String dateTime){
        return DateTimeOfPattern(dateTime, dateTimeFormat);
    }

    public int compareTo(String dateTime){
        Calendar compCalendar = Calendar.getInstance();
        try {
            compCalendar.setTime(new SimpleDateFormat(currentDateFormat).parse(dateTime));
            //System.out.println("up 1 -> " + new SimpleDateFormat(currentDateFormat).format(upCalendar.getTime()));
        } catch (Exception e) {return -10;}

        // если calendar укладывается в диапазон
        if (calendar.equals(compCalendar))
            return 0;

        return calendar.compareTo(compCalendar);
    }

    /**
     * Метод для проверки попадения значения типа String в диапазон <ОТ - ДО>
     * @param lo - нижняя граница задаваемого диапазона
     * @param up - верхняя граница диапазона
     * @return  - вернет 0 в случае попадания в диапазон, 1 - если позже, -1 если раньше
     */
    public int between(String lo, String up) {
        return between(
                new SimpleDateHelper(this.currentDateFormat).setDateTime(lo),
                new SimpleDateHelper(this.currentDateFormat).setDateTime(up)
        );
    }

    /**
     * Метод для проверки попадения значения типа Date в диапазон <ОТ - ДО>
     * @param lo - нижняя граница задаваемого диапазона
     * @param up - верхняя граница диапазона
     * @return  - вернет 0 в случае попадания в диапазон, 1 - если позже, -1 если раньше
     */
    public int between(Date lo, Date up) {
        return between(
                new SimpleDateHelper(this.currentDateFormat).setDateTime(lo),
                new SimpleDateHelper(this.currentDateFormat).setDateTime(up)
        );
    }

    /**
     *
     * @param lo - нижняя граница задаваемого диапазона
     * @param up - верхняя граница диапазона
     * @return  - вернет 0 в случае попадания в диапазон, 1 - если позже, -1 если раньше
     */
    public int between(SimpleDateHelper lo, SimpleDateHelper up) {
        Calendar loCalendar = lo.getCalendar();
        Calendar upCalendar = up.getCalendar();

        // если calendar укладывается в диапазон
        if ((loCalendar.compareTo(calendar)) < 0 && (upCalendar.compareTo(calendar) > 0) ||
                loCalendar.equals(calendar) || upCalendar.equals(calendar))
            return 0;

        // если calendar ниже up
        if (loCalendar.compareTo(calendar) > 0)
            return -1;

        // если calendar выше up
        if (upCalendar.compareTo(calendar) < 0)
            return 1;

        return -10;
    }

}