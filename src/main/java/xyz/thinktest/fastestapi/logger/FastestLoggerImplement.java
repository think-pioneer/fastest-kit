package xyz.thinktest.fastestapi.logger;

import xyz.thinktest.fastestapi.utils.ObjectUtil;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * @Date: 2022/1/2
 */
class FastestLoggerImplement implements FastestLogger {
    private LogLevel loggerLevel = LogLevel.DEBUG;
    private String charset = "UTF-8";  // 暂且没用，但是当需要序列化时是可能用到的；
    // TODO 也可以直接用LinkedQueue，然后手动通过ReentrantLock来实现并发时的数据安全（synchronized也可）
    //private BlockingQueue<LogRecord> queue = new LinkedBlockingQueue<LogRecord>();  // 可以理解为支持并发的LinkedList
    // TODO 想了一下既然是要学习原理干脆就实现的更底层一点
    private final Queue<LogRecord> records = new LinkedList<>();
    // TODO 用于记录生产了多少条日志，可供外部获取
    private final AtomicLong produceCount = new AtomicLong(0);
    // TODO 用于记录消费了多少条日志
    private final AtomicLong consumeCount = new AtomicLong(0);
    private final ExecutorService appenderPool = new ThreadPoolExecutor(1, LogLevel.values().length
            , 180000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), new ThreadPoolExecutor.CallerRunsPolicy());
    public static final ReentrantReadWriteLock LOCK=new ReentrantReadWriteLock();

    protected FastestLoggerImplement(){
        // TODO 日志记录的Consumer
        Thread consumer = new LogDaemon(this);
        consumer.setDaemon(true);
        consumer.start();
    }

    @Override
    public void trace(String format, Object... objects) {
        log(buildLogRecord(LogLevel.TRACE, format, objects));
    }

    @Override
    public void trace(String msg) {
        log(buildLogRecord(LogLevel.TRACE, msg));
    }

    @Override
    public void trace(Throwable exception) {
        log(buildLogRecord(LogLevel.TRACE, null, exception));
    }

    public void debug(String format, Object... objects){
        log(buildLogRecord(LogLevel.DEBUG, format, objects));
    }

    public void debug(String msg){
        log(buildLogRecord(LogLevel.DEBUG, msg));
    }

    public void debug(Throwable e){
        log(buildLogRecord(LogLevel.DEBUG, null, e));
    }

    public void info(String format, Object... objects){
        log(buildLogRecord(LogLevel.INFO, format, objects));
    }

    public void info(String msg){
        log(buildLogRecord(LogLevel.INFO, msg));
    }

    public void info(Throwable e){
        log(buildLogRecord(LogLevel.INFO, null, e));
    }

    public void error(String format, Object... objects){
        log(buildLogRecord(LogLevel.ERROR, format, objects));
    }

    public void error(String msg){
        log(buildLogRecord(LogLevel.ERROR, msg));
    }

    public void error(Throwable e){
        log(buildLogRecord(LogLevel.ERROR, null, e));
    }

    @Override
    public void warn(String format, Object... objects) {
        log(buildLogRecord(LogLevel.WARN, format, objects));
    }

    @Override
    public void warn(String msg) {
        log(buildLogRecord(LogLevel.WARN, msg));
    }

    @Override
    public void warn(Throwable exception) {
        log(buildLogRecord(LogLevel.WARN, null, exception));
    }

    private LogRecord buildLogRecord(LogLevel logLevel, String format, Object... objects){
        Date curr = generateCurrDate();
        //没有为占位符提供参数，原样输出
        if(objects.length == 0){
            return new LogRecord(logLevel, format, null, curr, getTargetStackTraceElement());
        }else {
            //判断占位符参数的最后一个参数是不是异常对象
            Object exception = objects[objects.length-1];
            exception = Objects.isNull(exception) ? "" : exception;
            if(Throwable.class.isAssignableFrom(exception.getClass())){
                Throwable e = (Throwable)exception;
                if(Objects.isNull(format)){
                    return new LogRecord(logLevel, null, buildExceptionWriter(e), curr, getTargetStackTraceElement());
                }else{
                    String msg;
                    if(objects.length > 1){
                        Object[] notThrowables = Arrays.copyOfRange(objects, 0, objects.length - 2);
                        msg = ObjectUtil.format(format,notThrowables);
                    }else {
                        msg = ObjectUtil.format(format,((Throwable) exception).getMessage());
                    }
                    return new LogRecord(logLevel, msg, buildExceptionWriter(e),curr, getTargetStackTraceElement());

                }
            } else {
                if(Objects.isNull(format)){
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i < objects.length; i++){
                        sb.append("{}").append(" ");
                    }
                    format = sb.toString();
                }
                String msg = ObjectUtil.format(format,objects);
                return new LogRecord(logLevel, msg, null, curr, getTargetStackTraceElement());
            }
        }
    }

    private StringWriter buildExceptionWriter(Throwable exception){
        StringWriter sw = new StringWriter();
        PrintWriter pw  = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw;
    }

    private StackTraceElement getTargetStackTraceElement(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return stackTraceElements[4];
    }

    /**
     * 给生产者（即调用log的方法都可以理解为生产者在生产日志对象）提供用于生产日志记录的接口
     * @param record
     */
    private void log(LogRecord record){
        synchronized (this.records){
            this.records.offer(record);
            this.produceCount.incrementAndGet();
            this.records.notify();
        }
    }

    public void notifyAppender(final Map<LogLevel, BufferedWriter> appenderMap, final LogRecord record) {
        record.level.println(record.toString());
        for (Map.Entry<LogLevel, BufferedWriter> entry : appenderMap.entrySet()) {
            if(entry.getKey().id <= record.level.id){
                BufferedWriter _bw = entry.getValue();
                appenderPool.submit(new AppenderRunnable(_bw, record.toString()));
            }
        }
    }

    /**
     * 用于产生当前时间的模块，防止因为并发而导致LogRecord的timestamp根实际情况不符
     */
    private final Lock currDateLock = new ReentrantLock();  // 直接用synchronized亦可
    private Date generateCurrDate(){
        currDateLock.lock();
        Date result = new Date();
        currDateLock.unlock();
        return result;
    }

    public LogLevel getLoggerLevel() {
        return loggerLevel;
    }

    public void setLoggerLevel(LogLevel loggerLevel) {
        this.loggerLevel = loggerLevel;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public AtomicLong getProduceCount() {
        return produceCount;
    }

    public AtomicLong getConsumeCount() {
        return consumeCount;
    }

    public Queue<LogRecord> getRecords(){
        return this.records;
    }
}
