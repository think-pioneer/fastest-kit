package xyz.thinktest.fastest.utils.dates;

import xyz.thinktest.fastest.common.exceptions.FastestBasicException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Date: 2021/12/26
 */
public class DateUtil {
    public static final String FORMAT_A = "yyyy-MM-dd";
    public static final String FORMAT_B = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_C = "yyyy_MM_dd_HH_mm_ss";

    public static String dateToString(Date date, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static Date stringToDate(String date, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(date);
        }catch (ParseException e){
            throw new FastestBasicException("SimpleDateFormat error", e);
        }
    }
}
