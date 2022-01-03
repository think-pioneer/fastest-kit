package xyz.thinktest.fastest.logger;

/**
 * @Date: 2022/1/2
 * 日志服务的接口
 */
interface LogInterface {

    /**
     * trace log
     * @param format 字符串的占位符
     * @param objects 字符串占位符需要打印的对象，如果其中包含exception对象，则需要把exception放在最后一位
     */
    void trace(String format, Object... objects);

    /**
     * trace log
     * @param msg log信息
     */
    void trace(String msg);

    /**
     * trace log
     * @param exception 异常对象
     */
    void trace(Throwable exception);

    /**
     * debug log
     * @param format 字符串的占位符
     * @param objects 字符串占位符需要打印的对象，如果其中包含exception对象，则需要把exception放在最后一位
     */
    void debug(String format, Object... objects);

    /**
     * debug log
     * @param msg log信息
     */
    void debug(String msg);

    /**
     * debug log
     * @param exception 异常对象
     */
    void debug(Throwable exception);

    /**
     * info log
     * @param format 字符串的占位符
     * @param objects 字符串占位符需要打印的对象，如果其中包含exception对象，则需要把exception放在最后一位
     */
    void info(String format, Object... objects);

    /**
     * info log
     * @param msg log信息
     */
    void info(String msg);

    /**
     * info log
     * @param exception 异常对象
     */
    void info(Throwable exception);

    /**
     * error log
     * @param format 字符串的占位符
     * @param objects 字符串占位符需要打印的对象，如果其中包含exception对象，则需要把exception放在最后一位
     */
    void error(String format, Object... objects);

    /**
     * error log
     * @param msg log信息
     */
    void error(String msg);

    /**
     * error log
     * @param exception 异常对象
     */
    void error(Throwable exception);

    /**
     * warn log
     * @param format 字符串的占位符
     * @param objects 字符串占位符需要打印的对象，如果其中包含exception对象，则需要把exception放在最后一位
     */
    void warn(String format, Object... objects);

    /**
     * warn log
     * @param msg log信息
     */
    void warn(String msg);

    /**
     * warn log
     * @param exception 异常对象
     */
    void warn(Throwable exception);
}
