package org.testng.step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Date: 2021/10/30
 */
public class RecoveryStep {
    private static final Logger logger = LoggerFactory.getLogger(RecoveryStep.class);

    public static void recovery(Step step, long timeOutMilli){
        long startTime = System.currentTimeMillis();
        while (true) {
            long endTime = System.currentTimeMillis();
            if((endTime - startTime) > timeOutMilli){
                logger.warn("the test data is recovery for more than {} millisecond, the amount of data may be too large", timeOutMilli);
                break;
            }
            Recovery recovery = step::recovery;
            if(!recovery.recovery()){
                return;
            }
        }
    }

    public static void recovery(Step step){
        recovery(step, 60 * 1000);
    }
}
