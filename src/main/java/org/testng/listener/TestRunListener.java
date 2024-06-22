package org.testng.listener;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import xyz.think.fastest.core.enhance.joinpoint.Target;
import xyz.think.fastest.core.internal.configuration.SystemConfig;
import xyz.think.fastest.core.internal.enhance.fieldhelper.TestNGExecutor;
import xyz.think.fastest.core.internal.initialization.InitializationActuator;
import xyz.think.fastest.core.internal.shutdown.ShutdownActuator;

import java.util.Objects;

/**
 * fastest framework starter
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
        ShutdownActuator.register();
        for(ITestNGMethod method:context.getAllTestMethods()){
            Class<?> caseClazz = method.getRealClass();
            // 将测试类托管给本框架
            if(Objects.nonNull(caseClazz)){
                Target manger = new Target();
                manger.setInstance(method.getInstance());
                TestNGExecutor executor = new TestNGExecutor(caseClazz);
                executor.execute(manger);
            }
        }
    }
}
