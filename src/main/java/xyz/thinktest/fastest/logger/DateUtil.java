package xyz.thinktest.fastest.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Date: 2022/1/2
 */
enum DateUtil {
    INSTANCE;
    private final String date;
    DateUtil(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
        this.date =  format.format(new Date());
    }

    public String getDate(){
        return this.date;
    }
}
