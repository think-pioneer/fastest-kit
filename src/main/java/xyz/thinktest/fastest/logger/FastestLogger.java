package xyz.thinktest.fastest.logger;

import xyz.thinktest.fastest.utils.ObjectUtil;
import xyz.thinktest.fastest.utils.files.FileUtil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @Date: 2022/1/2
 */
public class FastestLogger implements FastLog{
    private LogLevel loggerLevel = LogLevel.DEBUG;
    private String charset = "UTF-8";  // 暂且没用，但是当需要序列化时是可能用到的；
    // TODO 也可以直接用LinkedQueue，然后手动通过ReentrantLock来实现并发时的数据安全（synchronized也可）
    //private BlockingQueue<LogRecord> queue = new LinkedBlockingQueue<LogRecord>();  // 可以理解为支持并发的LinkedList
    // TODO 想了一下既然是要学习原理干脆就实现的更底层一点
    private final Queue<LogRecord> records = new LinkedList<LogRecord>();
    // TODO 用于记录生产了多少条日志，可供外部获取
    private final AtomicLong produceCount = new AtomicLong(0);
    // TODO 用于记录消费了多少条日志
    private final AtomicLong consumeCount = new AtomicLong(0);
    // TODO 日志记录的Consumer
    private Thread consumer = new LogDaemon();

    public FastestLogger(){
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
        if(objects.length == 0){
            return new LogRecord(logLevel, format, null, curr, getTargetStackTraceElement());
        }else {
            Object exception = objects[objects.length-1];
            if(Throwable.class.isAssignableFrom(exception.getClass())){
                Throwable e = (Throwable)exception;
                if(Objects.isNull(format)){
                    return new LogRecord(logLevel, null, buildExceptionWriter(e), curr, getTargetStackTraceElement());
                }else{
                    Object[] notThrowables = Arrays.copyOfRange(objects, 0, objects.length-2);
                    String msg = ObjectUtil.format(format,notThrowables);
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

    private PrintWriter buildExceptionWriter(Throwable exception){
        StringWriter sw = new StringWriter();
        PrintWriter pw  = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return pw;
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
        // ReentrantLock可以替代synchronized，不过当前场景下synchronized已经足够
        synchronized (this.records){  // TODO 如果用的是LinkedBlockingQueue是不需要这个的
            this.records.offer(record);
            this.produceCount.incrementAndGet();
            this.records.notify();  // TODO 只有一个线程会records.wait()，因此notify()足够
        }
    }

    // TODO 类似Redis的那个单线程，用于读取命令对象，而这里则是用于读取LogRecord并通过appender将数据写到相应位置
    private class LogDaemon extends Thread{
        private volatile boolean valid = true;
        private final Map<LogLevel, BufferedWriter> appenderMap = FastestAppender.INSTANCE.getWriterList();
        private final ExecutorService threadPool = new ThreadPoolExecutor(1, 3
                , 180000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024));

        @Override
        public void run() {
            while(this.valid){
                // TODO 根据最少知道原则，在这里不要去想整体里是否存在打断此线程的地方，你就认为此线程是可能被外界打断的即可，因此需要做一定处理
                try {
                    synchronized (FastestLogger.this.records) {
                        if (FastestLogger.this.records.size() <= 0) {
                            FastestLogger.this.records.wait();
                        }
                        final LogRecord firstRecord = FastestLogger.this.records.poll();
                        FastestLogger.this.consumeCount.incrementAndGet();
                        threadPool.execute(() -> {
                            FastestLogger.this.notifyAppender(appenderMap, firstRecord);});
                    }
                }catch (InterruptedException ex){
                    this.valid = false;
                    ex.printStackTrace();
                }catch (Throwable t){
                    t.printStackTrace();
                }
            }
        }
    }

    private void notifyAppender(final Map<LogLevel, BufferedWriter> appenderMap, final LogRecord record) {
        try {
            PrintWriter writer = new PrintWriter(record.level == LogLevel.ERROR ? System.err : System.out);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(record.toString());
            bw.newLine();
            bw.flush();
            // TODO 这种是同步的方式，如果是异步的方式可以将每个appender的执行都由一个Runnable对象包装，然后submit给线程池（或者中间加个中间件）
            for (Map.Entry<LogLevel, BufferedWriter> entry : appenderMap.entrySet()) {
                if(entry.getKey().id <= record.level.id){
                    BufferedWriter _bw = entry.getValue();
                    _bw.write(record.toString());
                    _bw.newLine();
                }
            }
            for(Map.Entry<LogLevel, BufferedWriter> entry : appenderMap.entrySet()){
                entry.getValue().flush();
            }
        }catch (IOException e){
            e.printStackTrace();
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

    // 生产者生产的数据对象
    public static class LogRecord{
        private final LogLevel level;
        private final String msg;
        private final String exception;
        private final String targetType;
        private final String targetMethod;
        private final Integer targetLine;
        private Date timestamp;
        private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        private SimpleDateFormat dateFormat = DEFAULT_DATE_FORMAT;

        // TODO 最好用这个，不然高并发下timestamp容易出现顺序不准确的情况。
        public LogRecord(LogLevel level, String msg, PrintWriter exceptionPs, Date timestamp, StackTraceElement stackTraceElement){
            this.level = level;
            this.msg = Objects.isNull(msg) ? "" : msg;
            this.exception = Objects.isNull(exceptionPs) ? "" : "\n"+exceptionPs.toString();
            this.timestamp = timestamp;
            this.targetType = stackTraceElement.getClassName();
            this.targetMethod = stackTraceElement.getMethodName();
            this.targetLine = stackTraceElement.getLineNumber();
        }

        @Override
        public String toString(){
            return String.format("%s %s %s %s %s - %s %s", dateFormat.format(timestamp), level, targetType, targetMethod, targetLine, msg, exception);
        }

        public LogLevel getLevel() {
            return level;
        }

        public String getMsg() {
            return msg;
        }

        public void setDateFormat(SimpleDateFormat dateFormat) {
            this.dateFormat = dateFormat;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }
    }

    public enum LogLevel{  // TODO 内部enum默认就是static
        INFO(3, "INFO"),
        DEBUG(2, "DEBUG"),
        ERROR(5, "ERROR"),
        TRACE(1, "TRACE"),
        WARN(4, "WARN");
        protected final int id;
        protected final String type;
        LogLevel(int id, String type){
            this.id = id;
            this.type = type;
        }
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
}
