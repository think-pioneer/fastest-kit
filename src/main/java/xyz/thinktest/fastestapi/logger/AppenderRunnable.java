package xyz.thinktest.fastestapi.logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 执行日志写入的任务
 */
class AppenderRunnable implements Runnable {

    private final BufferedWriter bw;
    private final String msg;
    public final ReentrantReadWriteLock LOCK=new ReentrantReadWriteLock();

    public AppenderRunnable(BufferedWriter bw, String msg){
        this.bw = bw;
        this.msg = msg;
    }

    @Override
    public void run() {
        try {
            if(Objects.nonNull(bw)) {
                LOCK.writeLock().lock();
                bw.write(this.msg);
                bw.newLine();
                bw.flush();

            }
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            LOCK.writeLock().unlock();
        }
    }
}
