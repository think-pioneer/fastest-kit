package xyz.thinktest.fastest.utils.dates;

import xyz.thinktest.fastest.common.exceptions.DateException;
import xyz.thinktest.fastest.utils.ObjectUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTime {
    private final Date date;
    private final Calendar calendar;
    private final String format;
    public DateTime(Date date, String format){
        this.date = date;
        this.format = format;
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(date);
    }

    private DateTime(Date date){
        this(date, DateUtil.FORMAT_B);
    }

    private DateTime(){
        this(new Date());
    }

    private DateTime(String format){
        this(new Date(), format);
    }

    private DateTime(String date, String format){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            this.date = dateFormat.parse(date);
            this.format = format;
            this.calendar = Calendar.getInstance();
            this.calendar.setTime(this.date);
        }catch (ParseException e){
            throw new DateException(ObjectUtil.format("Invalid date:\"{}\" or format:\"{}\""));
        }
    }

    private DateTime (Calendar calendar, String format){
        this.calendar = calendar;
        this.date = calendar.getTime();
        this.format = format;
    }

    private DateTime (Calendar calendar){
        this(calendar, DateUtil.FORMAT_B);
    }

    @Override
    public String toString(){
        return toString(format);
    }

    public String toString(String format){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(this.date);
        }catch (Exception e){
            throw new DateException(ObjectUtil.format("Invalid date:\"{}\" or format:\"{}\""));
        }
    }

    public Calendar toCalender(){
        return this.calendar;
    }

    public Date toDate(){
        return this.date;
    }

    public long toTimeStamp(){
        return this.date.getTime();
    }

    public static DateTime newInstance(){
        return new DateTime();
    }

    public static DateTime newInstance(Date date){
        return new DateTime(date);
    }

    public static DateTime newInstance(Date date, String format){
        return new DateTime(date, format);
    }

    public static DateTime newInstance(String format){
        return new DateTime(format);
    }

    public static DateTime newInstance(String date, String format){
        return new DateTime(date, format);
    }

    public static DateTime newInstance(Calendar calendar){
        return new DateTime(calendar);
    }

    public static DateTime newInstance(Calendar calendar, String format){
        return new DateTime(calendar, format);
    }
}