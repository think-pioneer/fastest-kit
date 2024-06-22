package org.testng.step;

import xyz.think.fastest.core.annotations.Component;
import xyz.think.fastest.logger.FastestLogger;
import xyz.think.fastest.logger.FastestLoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2021/10/30
 */
@Component
public class RecoveryExecutorStep extends AbstractRecoveryExecutor {
    private static final FastestLogger logger = FastestLoggerFactory.getLogger(RecoveryExecutorStep.class);

    public boolean execute(long timeOutMilli, boolean forceStop, Step... steps){
        List<Boolean> result = new ArrayList<>();
        Worker worker = new Worker(result, steps);
        worker.setDaemon(true);
        worker.start();

        long startTime = System.currentTimeMillis();
        while (true) {
            long endTime = System.currentTimeMillis();
            if((endTime - startTime) > timeOutMilli && worker.isAlive()){
                logger.warn("the test data is recovery for more than {} millisecond, the amount of data may be too large", timeOutMilli);
                if (!worker.isInterrupted()){
                    if (forceStop){
                        worker.stop();
                    }else {
                        worker.interrupt();
                    }
                }
                if (result.size() == steps.length){
                    return result.stream().allMatch(r -> r.equals(true));
                }
                return false;
            }
            if(!worker.isAlive()){
                return result.stream().allMatch(r -> r.equals(true));
            }
        }
    }

    static class Worker extends Thread{
        private final List<Boolean> result;
        private final Step[] steps;
        public Worker(List<Boolean> result, Step... steps){
            this.result = result;
            this.steps = steps;
        }

        public void run(){
            for (Step step: steps) {
                if (this.isInterrupted()){
                    return;
                }
                Restorer restorer = step::recovery;
                result.add(restorer.recovery());
            }
        }
    }
}
