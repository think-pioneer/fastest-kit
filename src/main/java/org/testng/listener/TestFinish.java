package org.testng.listener;

import org.testng.ITestNGMethod;
import org.testng.annotations.Test;
import org.testng.step.Recovery;
import xyz.thinktest.fastestapi.common.exceptions.FastestBasicException;
import xyz.thinktest.fastestapi.core.internal.enhance.EnhanceFactory;
import xyz.thinktest.fastestapi.logger.FastestLogger;
import xyz.thinktest.fastestapi.logger.FastestLoggerFactory;
import xyz.thinktest.fastestapi.utils.ObjectUtil;
import xyz.thinktest.fastestapi.utils.reflects.FieldHelper;
import org.testng.ITestResult;
import org.testng.step.Step;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Date: 2020/12/19
 */
class TestFinish{
    private static final FastestLogger logger = FastestLoggerFactory.getLogger(TestFinish.class);

    public void runStepRecovery(ITestResult result){
        try {
            Object testInstance = result.getInstance();
            ITestNGMethod iTestNGMethod = result.getMethod();
            Test test = iTestNGMethod.getConstructorOrMethod().getMethod().getDeclaredAnnotation(Test.class);
            if(Objects.nonNull(test) && test.recovery()){
                Field[] fields = testInstance.getClass().getDeclaredFields();
                Map<String, Class<?>> fieldTypeMap = Arrays.stream(fields).collect(Collectors.toMap(Field::getName, Field::getType));
                Map<String, Object> fieldMap = Arrays.stream(fields).collect(Collectors.toMap(Field::getName, field -> FieldHelper.getInstance(testInstance, field)));
                Class<?>[] stepTypes = test.stepType();
                Map<String, Step> stepMap;
                if(stepTypes.length == 1 && stepTypes[0].equals(Step.class)){
                    logger.warn("{} all steps under the test class will be executed", testInstance.getClass());
                    //获得所有的step
                    stepMap = fieldMap.entrySet().stream().filter(o -> o.getValue() instanceof Step).collect(Collectors.toMap(Map.Entry::getKey, o -> (Step) o.getValue()));
                } else {
                    //获得指定的step
                    stepMap = Arrays.stream(stepTypes)
                            .collect(Collectors.
                                    toMap(Class::getName, step ->
                                            (Step) FieldHelper.getInstance(testInstance, fieldTypeMap.entrySet().stream()
                                                    .filter(o -> o.getValue().equals(step))
                                                    .findFirst().orElseThrow(
                                                            () -> new FastestBasicException(ObjectUtil.format("not found field type:{}", step.getName())))
                                                    .getKey())
                                                    .get()));
                }
                for(Step step:stepMap.values()){
                    Recovery recovery = (Recovery) EnhanceFactory.origin(test.executor());
                    recovery.recovery(step);
                }
            }
        }catch (Throwable e){
            logger.error("run Step recovery method error:",e);
        }
    }
}
