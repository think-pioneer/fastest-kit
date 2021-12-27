package xyz.thinktest.fastest.utils.dates;

import xyz.thinktest.fastest.common.exceptions.FastestBasicException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Date: 2020/10/24
 */
public enum  TimeUtil {
    DAY(TimeType.DAYS),
    HOUR(TimeType.HOURS),
    MINUTE(TimeType.MINUTES),
    SECOND(TimeType.SECONDS),
    MILLIS(TimeType.MILLIS);

    private final long radix;
    TimeUtil(long radix){
        this.radix = radix;
    }
    /**
     * 大多数场景使用sleep时并不会触发异常，也不需要check exception
     * @param timeout 时长
     */
    public void sleep(Long timeout){
        try {
            Thread.sleep(timeout * radix);
        }catch (InterruptedException ignore){}
    }

    /**
     * 计算两个日期间的时间差。非自然时间。
     * 例如：
     * 开始时间：2020-01-01 23：59：59
     * 结束时间：2020-01-02 00：00：01
     * 结果为0，如果按照自然时间则为1
     * @param start 开始时间，Date
     * @param end 结束时间， Date
     * @return 时间差
     */
    public long diff(Date start, Date end){
        return Math.max(Math.abs(end.getTime() - start.getTime()), 0) / radix;
    }

    public long diff(String start, String end, String format){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date from = dateFormat.parse(start);
            Date to = dateFormat.parse(end);
            return diff(from, to);
        }catch (ParseException e){
            throw new FastestBasicException("string date parse Date object fail ", e);
        }
    }

    static class TimeType {
        public static final long MILLIS = 1L;
        public static final long SECONDS = MILLIS * 1000L;
        public static final long MINUTES = SECONDS * 60;
        public static final long HOURS = MINUTES * 60;
        public static final long DAYS = HOURS * 24;
    }
}

