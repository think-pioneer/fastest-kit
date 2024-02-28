package org.testng.step;

import xyz.think.fastest.logger.FastestLogger;
import xyz.think.fastest.logger.FastestLoggerFactory;

/**
 * @Date: 2021/10/30
 */
public class RecoveryStep extends AbstractRecovery{
    private static final FastestLogger logger = FastestLoggerFactory.getLogger(RecoveryStep.class);

    public boolean recovery(long timeOutMilli, Step step){
        long startTime = System.currentTimeMillis();
        while (true) {
            long endTime = System.currentTimeMillis();
            if((endTime - startTime) > timeOutMilli){
                logger.warn("the test data is recovery for more than {} millisecond, the amount of data may be too large", timeOutMilli);
                return false;
            }
            Restorer restorer = step::recovery;
            if(!restorer.recovery()){
                return true;
            }
        }
    }

    public boolean recovery(Step step){
        return recovery(60 * 1000, step);
    }
}
