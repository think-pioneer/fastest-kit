package org.testng.listener;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;
import xyz.thinktest.fastestapi.core.internal.configuration.SystemConfig;
import xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper.TestNGProcess;
import xyz.thinktest.fastestapi.core.internal.initialization.InitializationActuator;
import xyz.thinktest.fastestapi.core.internal.shutdown.ShutdownActuator;

import java.util.Objects;

/**
 * @Date: 2020/10/30
 */
public class TestRunListener implements ITestListener {
    private final TestFinish testFinish = new TestFinish();

    public void onTestSuccess(ITestResult result){
        testFinish.runStepRecovery(result);
    }

    public void onTestFailure(ITestResult result){
        testFinish.runStepRecovery(result);
    }

    @Override
    public void onStart(ITestContext context){
        SystemConfig.config();
        InitializationActuator.init();
        for(ITestNGMethod method:context.getAllTestMethods()){
            Class<?> caseClazz = method.getRealClass();
            // 将测试类托管给本框架
            if(Objects.nonNull(caseClazz)){
                Target manger = new Target();
                manger.setInstance(method.getInstance());
                TestNGProcess process = new TestNGProcess(caseClazz);
                process.process(manger);
            }
        }
    }

    @Override
    public void onFinish(ITestContext context){
        ShutdownActuator.execute();
    }
}
