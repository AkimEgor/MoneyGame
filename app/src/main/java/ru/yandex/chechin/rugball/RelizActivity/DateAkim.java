package ru.yandex.chechin.rugball.RelizActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateAkim extends Date{

    private Date farmatData;
    public Date begining(){

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(farmatData);
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date begining = calendar.getTime();
        return begining;
    }
    public Date end(){
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(farmatData);
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date end = calendar.getTime();
        return end;

    }
    public DateAkim (Date date){
        super(date.getTime());
        farmatData = date;
    }

public String printInFormat (){
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM");
    return dateFormat.format(farmatData);
}
    public String printInFormatDay (){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return dateFormat.format(farmatData);
    }




}
