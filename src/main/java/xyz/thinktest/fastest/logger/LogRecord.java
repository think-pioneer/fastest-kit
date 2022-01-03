package xyz.thinktest.fastest.logger;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

// 生产者生产的数据对象
class LogRecord {
    public final LogLevel level;
    public final String msg;
    public final String exception;
    public final String targetType;
    public final String targetMethod;
    public final Integer targetLine;
    public final Date timestamp;
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    public final SimpleDateFormat dateFormat = DEFAULT_DATE_FORMAT;

    // TODO 最好用这个，不然高并发下timestamp容易出现顺序不准确的情况。
    public LogRecord(LogLevel level, String msg, PrintWriter exceptionPs, Date timestamp, StackTraceElement stackTraceElement) {
        this.level = level;
        this.msg = Objects.isNull(msg) ? "" : msg;
        this.exception = Objects.isNull(exceptionPs) ? "" : "\n" + exceptionPs.toString();
        this.timestamp = timestamp;
        this.targetType = stackTraceElement.getClassName();
        this.targetMethod = stackTraceElement.getMethodName();
        this.targetLine = stackTraceElement.getLineNumber();
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s - %s %s", dateFormat.format(timestamp), level, targetType, targetMethod, targetLine, msg, exception);
    }
}
