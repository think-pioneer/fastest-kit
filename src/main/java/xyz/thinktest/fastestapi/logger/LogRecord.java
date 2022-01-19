package xyz.thinktest.fastestapi.logger;

import xyz.thinktest.fastestapi.utils.dates.DateTime;

import java.io.StringWriter;
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

    // TODO 最好用这个，不然高并发下timestamp容易出现顺序不准确的情况。
    public LogRecord(LogLevel level, String msg, StringWriter exceptionPs, Date timestamp, StackTraceElement stackTraceElement) {
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
        return String.format("%s %s %s %s %s - %s %s", DateTime.format(timestamp, "yyyy-MM-dd HH:mm:ss:SSS"), level, targetType, targetMethod, targetLine, msg, exception);
    }
}
