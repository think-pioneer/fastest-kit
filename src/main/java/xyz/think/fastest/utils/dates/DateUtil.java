package xyz.think.fastest.utils.dates;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 日期处理工具类，将日期对象转换为DateTime对象
 */
public enum DateUtil {
    MILLISECOND(Calendar.MILLISECOND){
        @Override
        public void sleep(int duration) {
            try{
                TimeUnit.MILLISECONDS.sleep(duration);
            }catch (InterruptedException ignored){}
        }

        @Override
        public DateTime start() {
            return DateTime.newInstance(this.date);
        }

        @Override
        public DateTime start(Date date) {
            return DateTime.newInstance(date);
        }

        @Override
        public DateTime start(String date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public DateTime start(Date date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public DateTime end() {
            return DateTime.newInstance(this.date);
        }

        @Override
        public DateTime end(Date date) {
            return DateTime.newInstance(date);
        }

        @Override
        public DateTime end(String date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public DateTime end(Date date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public long diff(Date before, Date after) {
            return after.getTime() - before.getTime();
        }

        @Override
        public long diff(String before, String after, String format) {
            return diff(DateTime.parse(before, format), DateTime.parse(after, format));
        }

        @Override
        public boolean isBetween(Date left, Date right, Date value) {
            return right.getTime() > value.getTime() && left.getTime() < value.getTime();
        }

        @Override
        public boolean isBetween(String left, String right, String value, String format) {
            return isBetween(DateTime.parse(left, format), DateTime.parse(right, format), DateTime.parse(value, format));
        }
    },
    SECOND(Calendar.SECOND) {
        @Override
        public void sleep(int duration) {
            try{
                TimeUnit.SECONDS.sleep(duration);
            }catch (InterruptedException ignored){}
        }

        @Override
        public DateTime start() {
            return DateTime.newInstance(this.date);
        }

        @Override
        public DateTime start(Date date) {
            return DateTime.newInstance(date);
        }

        @Override
        public DateTime start(String date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public DateTime start(Date date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public DateTime end() {
            return DateTime.newInstance(this.date);
        }

        @Override
        public DateTime end(Date date) {
            return DateTime.newInstance(date);
        }

        @Override
        public DateTime end(String date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public DateTime end(Date date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public long diff(Date before, Date after) {
            long result = after.getTime() - before.getTime();
            return result / 1000;
        }

        @Override
        public long diff(String before, String after, String format) {
            return diff(DateTime.parse(before, format), DateTime.parse(after, format));
        }

        @Override
        public boolean isBetween(Date left, Date right, Date value) {
            return right.getTime() / 1000 > value.getTime() / 1000 && left.getTime() / 1000 < value.getTime() / 1000;
        }

        @Override
        public boolean isBetween(String left, String right, String value, String format) {
            return isBetween(DateTime.parse(left, format), DateTime.parse(right, format), DateTime.parse(value, format));
        }
    },
    MINUTE(Calendar.MINUTE) {
        @Override
        public void sleep(int duration) {
            try{
                TimeUnit.MINUTES.sleep(duration);
            }catch (InterruptedException ignored){}
        }

        @Override
        public DateTime start() {
            return DateTime.newInstance(this.date);
        }

        @Override
        public DateTime start(Date date) {
            return DateTime.newInstance(date);
        }

        @Override
        public DateTime start(String date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public DateTime start(Date date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public DateTime end() {
            return DateTime.newInstance(this.date);
        }

        @Override
        public DateTime end(Date date) {
            return DateTime.newInstance(date);
        }

        @Override
        public DateTime end(String date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public DateTime end(Date date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public long diff(Date before, Date after) {
            long result = after.getTime() - before.getTime();
            return result / (1000 * 60);
        }

        @Override
        public long diff(String before, String after, String format) {
            return diff(DateTime.parse(before, format), DateTime.parse(after, format));
        }

        @Override
        public boolean isBetween(Date left, Date right, Date value) {
            long leftTime = left.getTime() / (1000 * 60);
            long rightTime = right.getTime() / (1000 * 60);
            long valueTime = value.getTime() / (1000 * 60);
            return rightTime > valueTime && leftTime < valueTime;
        }

        @Override
        public boolean isBetween(String left, String right, String value, String format) {
            return isBetween(DateTime.parse(left, format), DateTime.parse(right, format), DateTime.parse(value, format));
        }
    },
    HOUR(Calendar.HOUR_OF_DAY) {
        @Override
        public void sleep(int duration) {
            try{
                TimeUnit.HOURS.sleep(duration);
            }catch (InterruptedException ignored){}
        }

        @Override
        public DateTime start() {
            return DateTime.newInstance(this.date);
        }

        @Override
        public DateTime start(Date date) {
            return DateTime.newInstance(date);
        }

        @Override
        public DateTime start(String date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public DateTime start(Date date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public DateTime end() {
            return DateTime.newInstance(this.date);
        }

        @Override
        public DateTime end(Date date) {
            return DateTime.newInstance(date);
        }

        @Override
        public DateTime end(String date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public DateTime end(Date date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public long diff(Date before, Date after) {
            long result = after.getTime() - before.getTime();
            return result / (1000 * 60 *60);
        }

        @Override
        public long diff(String before, String after, String format) {
            return diff(DateTime.parse(before, format), DateTime.parse(after, format));
        }

        @Override
        public boolean isBetween(Date left, Date right, Date value) {
            long leftTime = left.getTime() / (1000 * 60 * 60);
            long rightTime = right.getTime() / (1000 * 60 * 60);
            long valueTime = value.getTime() / (1000 * 60 * 60);
            return rightTime > valueTime && leftTime < valueTime;
        }

        @Override
        public boolean isBetween(String left, String right, String value, String format) {
            return isBetween(DateTime.parse(left, format), DateTime.parse(right, format), DateTime.parse(value, format));
        }
    },
    DAY(Calendar.DAY_OF_YEAR){
        @Override
        public void sleep(int duration) {
            try{
                TimeUnit.DAYS.sleep(duration);
            }catch (InterruptedException ignored){}
        }

        @Override
        public DateTime start() {
            return start(this.date);
        }

        @Override
        public DateTime start(Date date) {
            return start(date, "yyyy-MM-dd 00:00:00");
        }

        @Override
        public DateTime start(String date, String format) {
            return start(DateTime.newInstance(date, format).toDate(), "yyyy-MM-dd 00:00:00");
        }

        @Override
        public DateTime start(Date date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public DateTime end() {
            return end(this.date);
        }

        @Override
        public DateTime end(Date date) {
            return end(date, "yyyy-MM-dd 23:59:59");
        }

        @Override
        public DateTime end(String date, String format) {
            return end(DateTime.newInstance(date, format).toDate(), "yyyy-MM-dd 23:59:59");
        }

        @Override
        public DateTime end(Date date, String format) {
            return DateTime.newInstance(date, format);
        }

        @Override
        public long diff(Date before, Date after) {
            LocalDate beforeDate = before.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate afterDate = after.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Period p = Period.between(beforeDate, afterDate);
            return p.getDays();
        }

        @Override
        public long diff(String before, String after, String format) {
            return diff(DateTime.parse(before, format), DateTime.parse(after, format));
        }

        @Override
        public boolean isBetween(Date left, Date right, Date value) {
            long rightTime = left.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfYear();
            long valueTime = right.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfYear();
            long leftTime = right.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfYear();
            return rightTime > valueTime && leftTime < valueTime;
        }

        @Override
        public boolean isBetween(String left, String right, String value, String format) {
            return isBetween(DateTime.parse(left, format), DateTime.parse(right, format), DateTime.parse(value, format));
        }
    },
    WEEK(Calendar.WEEK_OF_YEAR) {
        @Override
        public void sleep(int duration) {
            try{
                TimeUnit.DAYS.sleep(duration * 7L);
            }catch (InterruptedException ignored){}
        }

        @Override
        public DateTime start() {
            return start(this.date);
        }

        @Override
        public DateTime start(Date date) {
            return start(date, "yyyy-MM-dd 00:00:00");
        }

        @Override
        public DateTime start(String date, String format) {
            return start(DateTime.newInstance(date, format).toDate(), "yyyy-MM-dd 00:00:00");
        }

        @Override
        public DateTime start(Date date, String format) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            return DateTime.newInstance(calendar, format);
        }

        @Override
        public DateTime end() {
            return end(this.date);
        }

        @Override
        public DateTime end(Date date) {
            return end(date, "yyyy-MM-dd 23:59:59");
        }

        @Override
        public DateTime end(String date, String format) {
            return end(DateTime.newInstance(date, format).toDate(), "yyyy-MM-dd 23:59:59");
        }

        @Override
        public DateTime end(Date date, String format) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            calendar.add(Calendar.DAY_OF_WEEK, 6);
            return DateTime.newInstance(calendar, format);
        }

        @Override
        public long diff(Date before, Date after) {
            LocalDate beforeDate = before.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate afterDate = after.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Period p = Period.between(beforeDate, afterDate);
            return p.getDays() / 7;
        }

        @Override
        public long diff(String before, String after, String format) {
            return diff(DateTime.parse(before, format), DateTime.parse(after, format));
        }

        @Override
        public boolean isBetween(Date left, Date right, Date value) {
            long rightTime = left.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfYear() / 7;
            long valueTime = right.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfYear() / 7;
            long leftTime = right.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfYear() / 7;
            return rightTime > valueTime && leftTime < valueTime;
        }

        @Override
        public boolean isBetween(String left, String right, String value, String format) {
            return isBetween(DateTime.parse(left, format), DateTime.parse(right, format), DateTime.parse(value, format));
        }
    },
    MONTH(Calendar.MONTH) {
        @Override
        public void sleep(int duration) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, 1);
            calendar.roll(Calendar.DATE, -1);
            int maxDate = calendar.get(Calendar.DATE);
            try{
                TimeUnit.DAYS.sleep((long) duration * maxDate);
            }catch (InterruptedException ignored){}
        }

        @Override
        public DateTime start() {
            return start(this.date);
        }

        @Override
        public DateTime start(Date date) {
            return start(date, "yyyy-MM-dd 00:00:00");
        }

        @Override
        public DateTime start(String date, String format) {
            return start(DateTime.newInstance(date, format).toDate(), "yyyy-MM-dd 00:00:00");
        }

        @Override
        public DateTime start(Date date, String format) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            return DateTime.newInstance(calendar, format);
        }

        @Override
        public DateTime end() {
            return end(this.date);
        }

        @Override
        public DateTime end(Date date) {
            return end(date, "yyyy-MM-dd 23:59:59");
        }

        @Override
        public DateTime end(String date, String format) {
            return end(DateTime.newInstance(date, format).toDate(), "yyyy-MM-dd 23:59:59");
        }

        @Override
        public DateTime end(Date date, String format) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            return DateTime.newInstance(calendar, format);
        }

        @Override
        public long diff(Date before, Date after) {
            LocalDate beforeDate = before.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate afterDate = after.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Period p = Period.between(beforeDate, afterDate);
            return p.getMonths();
        }

        @Override
        public long diff(String before, String after, String format) {
            return diff(DateTime.parse(before, format), DateTime.parse(after, format));
        }

        @Override
        public boolean isBetween(Date left, Date right, Date value) {
            long rightTime = left.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
            long valueTime = right.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
            long leftTime = right.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
            return rightTime > valueTime && leftTime < valueTime;
        }

        @Override
        public boolean isBetween(String left, String right, String value, String format) {
            return isBetween(DateTime.parse(left, format), DateTime.parse(right, format), DateTime.parse(value, format));
        }
    },
    YEAR(Calendar.YEAR) {
        @Override
        public void sleep(int duration) {
            long yearDay = LocalDate.now().lengthOfYear();
            try{
                TimeUnit.DAYS.sleep(duration * yearDay);
            }catch (InterruptedException ignored){}
        }

        @Override
        public DateTime start() {
            return start(this.date);
        }

        @Override
        public DateTime start(Date date) {
            return start(date, "yyyy-MM-dd 00:00:00");
        }

        @Override
        public DateTime start(String date, String format) {
            return start(DateTime.newInstance(date, format).toDate(), "yyyy-MM-dd 00:00:00");
        }

        @Override
        public DateTime start(Date date, String format) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -calendar.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            return DateTime.newInstance(calendar, format);
        }

        @Override
        public DateTime end() {
            return end(this.date);
        }

        @Override
        public DateTime end(Date date) {
            return end(date, "yyyy-MM-dd 23:59:59");
        }

        @Override
        public DateTime end(String date, String format) {
            return end(DateTime.newInstance(date, format).toDate(), "yyyy-MM-dd 23:59:59");
        }

        @Override
        public DateTime end(Date date, String format) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 12-calendar.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            return DateTime.newInstance(calendar, format);
        }

        @Override
        public long diff(Date before, Date after) {
            LocalDate beforeDate = before.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate afterDate = after.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Period p = Period.between(beforeDate, afterDate);
            return p.getYears();
        }

        @Override
        public long diff(String before, String after, String format) {
            return diff(DateTime.parse(before, format), DateTime.parse(after, format));
        }

        @Override
        public boolean isBetween(Date left, Date right, Date value) {
            long rightTime = left.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
            long valueTime = right.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
            long leftTime = right.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
            return rightTime > valueTime && leftTime < valueTime;
        }

        @Override
        public boolean isBetween(String left, String right, String value, String format) {
            return isBetween(DateTime.parse(left, format), DateTime.parse(right, format), DateTime.parse(value, format));
        }
    };

    /**
     * 部分常用的日期格式常量
     */
    public static final String FORMAT_A = "yyyy-MM-dd";
    public static final String FORMAT_B = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_C = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String FORMAT_D = "yyyy_MM_dd_HH_mm_ss";
    public static final String FORMAT_E = "yyyy_MM_dd_HH_mm_ss_SSS";
    public static final String FORMAT_F = "yyyy/MM/dd HH:mm";
    public static final String FORMAT_G = "yyyy/MM/dd HH:mm:ss";
    public static final String FORMAT_H = "yyyy年MM月dd日 HH时mm分ss秒 E";
    public static final String FORMAT_I = "yyyy/MM/dd E";

    protected final int type;
    protected Date date;
    DateUtil(int type){
        this.type = type;
        this.date = new Date();
    }

    /**
     * 线程睡眠
     * @param duration 睡眠持续时间
     */
    public abstract void sleep(int duration);

    /**
     * 当前时间类型的开始时间
     * MILLISECOND、SECOND、MINUTE、HOUR从宏观角度不具备开始时间和结束时间，未做处理
     * 以下类型的开始时间的时分秒都是：00:00:00
     * DAY：00:00:00
     * WEEK: 周一为一周的开始日期
     * MONTH: 每月1号
     * YEAR:每年1月1号
     * @return DateTime
     */
    /**
     * 开始日期，以当前日期为标准。即当取周、月、年等类型的第一天时标准日期
     * 例如：取2022-01-10，取日期所在月的第一天
     */
    public abstract DateTime start();

    /**
     * 指定日期和格式
     */
    public abstract DateTime start(Date date);

    /**
     * 指定字符类型的日期
     * @param date 字符类型的日期
     * @param format 作为输入日期的日期格式
     */
    public abstract DateTime start(String date, String format);

    /**
     * 指定日期，并指定返回的日期格式
     * @param date 日期
     * @param format 待返回日期的格式
     */
    public abstract DateTime start(Date date, String format);

    /**
     * 当前时间类型的开始时间
     * MILLISECOND、SECOND、MINUTE、HOUR从宏观角度不具备开始时间和结束时间，未做处理
     * 以下类型的结束时间的时分秒都是：23:59:59
     * DAY：23:59:59
     * WEEK: 周一为一周的开始日期
     * MONTH: 每月1号
     * YEAR:每年1月1号
     * @return DateTime
     */
    /**
     * 结束日期，以当前日期为标准。即当取周、月、年等类型的最后一天时标准日期
     * 例如：取2022-01-10，取日期所在月的最后一天
     */
    public abstract DateTime end();

    /**
     * 自定义日期标准
     */
    public abstract DateTime end(Date date);

    /**
     * 指定字符类型的日期
     * @param date 字符类型的日期
     * @param format 作为输入日期的日期格式
     */
    public abstract DateTime end(String date, String format);
    /**
     * 指定日期，并指定返回的日期格式
     * @param date 日期
     * @param format 待返回日期的格式
     */
    public abstract DateTime end(Date date, String format);

    /**
     * 计算两个日期的时间差
     */
    public abstract long diff(Date before, Date after);

    /**
     * 计算两个日期(字符串类型)的时间差
     */
    public abstract long diff(String before, String after, String format);

    /**
     * 计算value是否在left和right之间(闭区间)
     */
    public abstract boolean isBetween(Date left, Date right, Date value);

    /**
     * (字符串类型)计算value是否在left和right之间(闭区间)
     */
    public abstract boolean isBetween(String left, String right, String value, String format);


    public DateTime toDateTime(String format){
        return DateTime.newInstance(this.date, format);
    }

    public DateTime toDateTime(){
        return DateTime.newInstance(this.date);
    }

    /**
     * 对时间做加法或减法
     * @param date 起始时间
     * @param diff 差异时间，正加负减
     * @return
     */
    public DateUtil calculate(Date date, int diff){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(this.type, diff);
        this.date = calendar.getTime();
        return this;
    }

    public DateUtil calculate(int diff){
        return calculate(new Date(), diff);
    }

    public DateUtil calculate(String stringDate, String format, int diff){
        return calculate(DateTime.parse(stringDate, format), diff);
    }
}
