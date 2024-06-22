package org.testng.listener;

import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.step.RecoveryExecutor;
import org.testng.step.Step;
import xyz.think.fastest.common.exceptions.FastestBasicException;
import xyz.think.fastest.core.internal.enhance.EnhanceFactory;
import xyz.think.fastest.core.internal.tool.AnnotationTool;
import xyz.think.fastest.logger.FastestLogger;
import xyz.think.fastest.logger.FastestLoggerFactory;
import xyz.think.fastest.utils.reflects.FieldHelper;
import xyz.think.fastest.utils.reflects.ReflectUtil;
import xyz.think.fastest.utils.string.StringUtils;

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
            org.testng.annotations.Recovery recoveryAnn = iTestNGMethod.getConstructorOrMethod().getMethod().getDeclaredAnnotation(org.testng.annotations.Recovery.class);
            if(Objects.isNull(recoveryAnn)){
                recoveryAnn = testInstance.getClass().getDeclaredAnnotation(org.testng.annotations.Recovery.class);
            }
            if(Objects.nonNull(recoveryAnn) && recoveryAnn.recovery() && AnnotationTool.hasComponentAnnotation(recoveryAnn.executor())){
                Field[] fields = testInstance.getClass().getDeclaredFields();
                Map<String, Class<?>> fieldTypeMap = Arrays.stream(fields).filter(field -> AnnotationTool.hasComponentAnnotation(field.getType())).collect(Collectors.toMap(Field::getName, Field::getType));
                Map<String, FieldHelper> fieldMap = Arrays.stream(fields).filter(field -> AnnotationTool.hasComponentAnnotation(field.getType())).filter(field -> !ReflectUtil.isFinal(field)).collect(Collectors.toMap(Field::getName, field -> FieldHelper.getInstance(testInstance, field)));
                Class<?>[] stepTypes = recoveryAnn.stepType();
                Map<String, Step> stepMap;
                if(stepTypes.length == 1 && stepTypes[0].equals(Step.class)){
                    logger.warn("{} all steps under the test class will be executed", testInstance.getClass());
                    //获得所有的step
                    stepMap = fieldMap.entrySet().stream()
                            .filter(o -> o.getValue().get() instanceof Step)
                            .filter(o -> AnnotationTool.hasComponentAnnotation(o.getValue().get().getClass()))
                            .collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().get()));

                } else {
                    //获得指定的step
                    stepMap = Arrays.stream(stepTypes)
                            .filter(AnnotationTool::hasComponentAnnotation)
                            .collect(Collectors.
                                    toMap(Class::getName, step ->
                                            FieldHelper.getInstance(testInstance, fieldTypeMap.entrySet().stream()
                                                    .filter(o -> o.getValue().equals(step))
                                                    .findFirst().orElseThrow(
                                                            () -> new FastestBasicException(StringUtils.format("not found step instance:{0}", step.getName())))
                                                    .getKey())
                                                    .get()));
                }
                RecoveryExecutor recoveryExecutor = (RecoveryExecutor) EnhanceFactory.origin(recoveryAnn.executor());
                recoveryExecutor.execute(recoveryAnn.timeout(), recoveryAnn.forceStop(), stepMap.values().toArray(new Step[0]));
            }
        }catch (Throwable e){
            logger.error("run Step recovery method error:",e);
            throw e;
        }
    }
}
