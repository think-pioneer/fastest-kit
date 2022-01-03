package xyz.thinktest.fastest.logger;

public class FastestLogger extends LogInterfaceImplements {

    // TODO 日志记录的Consumer
    public FastestLogger(){
        super();
        Thread consumer = new LogDaemon(this);
        consumer.setDaemon(true);
        consumer.start();
    }
}
