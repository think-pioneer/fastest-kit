package org.testng.step;

import org.fastest.logger.FastLogger;
import org.fastest.logger.FastLoggerFactory;

/**
 * @Date: 2021/10/30
 */
public class RecoveryStep implements Recovery{
    private static final FastLogger logger = FastLoggerFactory.getLogger(RecoveryStep.class);

    public boolean recovery(Step step, long timeOutMilli){
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
        return recovery(step, 60 * 1000);
    }
}
