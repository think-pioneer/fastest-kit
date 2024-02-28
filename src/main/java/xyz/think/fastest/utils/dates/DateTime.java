package xyz.think.fastest.utils.dates;

import xyz.think.fastest.common.exceptions.DateException;
import xyz.think.fastest.utils.string.StringUtils;

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
        this.date = parse(date, format);
        this.format = format;
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(this.date);
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
    public String toString() {
        return "DateTime{" +
                "date=" + date.toString() +
                ", calendar=" + calendar.toString() +
                ", format='" + format + '\'' +
                '}';
    }

    public String string(){
        return format(this.date, format);
    }

    public String string(String format){
        return format(this.date, format);
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

    public static Date parse(String date, String format){
        try{
            SimpleDateFormat sf = new SimpleDateFormat(format);
            return sf.parse(date);
        }catch (ParseException e){
            throw new DateException(StringUtils.format("Invalid date:\"{0}\" or format:\"{1}\"", date, format));
        }
    }

    public static String format(Date date, String format){
        try {
            SimpleDateFormat sf = new SimpleDateFormat(format);
            return sf.format(date);
        }catch (Exception e){
            throw new DateException(StringUtils.format("Invalid date:\"{0}\" or format:\"{1}\"", date.toString(), format));
        }
    }
}
