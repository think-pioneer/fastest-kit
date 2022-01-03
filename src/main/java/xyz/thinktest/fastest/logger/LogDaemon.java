package xyz.thinktest.fastest.logger;

import java.io.BufferedWriter;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// TODO 类似Redis的那个单线程，用于读取命令对象，而这里则是用于读取LogRecord并通过appender将数据写到相应位置
class LogDaemon extends Thread {
    private final LogInterfaceImplements logInterfaceImplements;
    private volatile boolean valid = true;
    private final Map<LogLevel, BufferedWriter> appenderMap = LogAppender.INSTANCE.getBufferWriterMap();
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 50
            , 180000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(4096), new ThreadPoolExecutor.CallerRunsPolicy());

    public LogDaemon(LogInterfaceImplements logInterfaceImplements) {
        this.logInterfaceImplements = logInterfaceImplements;
    }

    @Override
    public void run() {
        while (this.valid) {
            // TODO 根据最少知道原则，在这里不要去想整体里是否存在打断此线程的地方，你就认为此线程是可能被外界打断的即可，因此需要做一定处理
            try {
                synchronized (logInterfaceImplements.getRecords()) {
                    if (logInterfaceImplements.getRecords().size() <= 0) {
                        logInterfaceImplements.getRecords().wait();
                    }
                    final LogRecord firstRecord = logInterfaceImplements.getRecords().poll();
                    logInterfaceImplements.getConsumeCount().incrementAndGet();
                    if(threadPool.getActiveCount() < 50) {
                        threadPool.execute(() -> logInterfaceImplements.notifyAppender(appenderMap, firstRecord));
                    }
                }
            } catch (InterruptedException ex) {
                this.valid = false;
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } catch (Throwable t) {
                System.err.println(t.getMessage());
                t.printStackTrace();
            }
        }
    }
}
