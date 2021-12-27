package org.testng.listener;

import xyz.thinktest.fastest.core.internal.enhance.EnhanceFactory;
import xyz.thinktest.fastest.logger.FastLogger;
import xyz.thinktest.fastest.logger.FastLoggerFactory;
import xyz.thinktest.fastest.utils.reflects.FieldHelper;
import org.testng.ITestResult;
import org.testng.annotations.Recovery;
import org.testng.step.Step;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Date: 2020/12/19
 */
class TestFinish{
    private static final FastLogger logger = FastLoggerFactory.getLogger(TestFinish.class);

    public void runStepRecovery(ITestResult result){
        try {
            Object testInstance = result.getInstance();
            Recovery recovery = testInstance.getClass().getAnnotation(Recovery.class);
            if(Objects.nonNull(recovery)){
                Field[] fields = testInstance.getClass().getDeclaredFields();
                for (Field field : fields) {
                    Object object = FieldHelper.getInstance(testInstance, field).get();
                    if (object instanceof Step) {
                        Step step = (Step) object;
                        org.testng.step.Recovery recovery1 = (org.testng.step.Recovery) EnhanceFactory.origin(recovery.executor());
                        recovery1.recovery(step);
                    }
                }
            }
        }catch (Exception e){
            logger.error("run Step recovery method error:",e);
        }
    }
}
