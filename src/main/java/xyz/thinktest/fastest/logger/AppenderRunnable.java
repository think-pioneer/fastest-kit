package xyz.thinktest.fastest.logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * 执行日志写入的任务
 */
class AppenderRunnable implements Runnable {

    private final BufferedWriter bw;
    private final String msg;

    public AppenderRunnable(BufferedWriter bw, String msg){
        this.bw = bw;
        this.msg = msg;
    }

    @Override
    public void run() {
        try {
            if(Objects.nonNull(bw)) {
                bw.append(this.msg);
                bw.newLine();
                bw.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
