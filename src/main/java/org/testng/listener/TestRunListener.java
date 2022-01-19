package org.testng.listener;

import xyz.thinktest.fastestapi.core.internal.Initialization;
import xyz.thinktest.fastestapi.core.internal.enhance.ShutdownHook;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;
import xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper.TestNGProcess;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.util.Objects;

/**
 * @Date: 2020/10/30
 */
public class TestRunListener<T> implements ITestListener {
    private final TestFinish testFinish = new TestFinish();

    public void onTestSuccess(ITestResult result){
        testFinish.runStepRecovery(result);
    }

    public void onTestFailure(ITestResult result){
        testFinish.runStepRecovery(result);
    }

    @Override
    public void onStart(ITestContext context){
        Initialization.init();
        for(ITestNGMethod method:context.getAllTestMethods()){
            Class<?> caseClazz = method.getRealClass();
            if(Objects.nonNull(caseClazz)){
                Target<T> manger = new Target<>();
                manger.setInstance((T) method.getInstance());
                TestNGProcess<T> process = new TestNGProcess<>(caseClazz);
                process.process(manger);
            }
        }
    }

    @Override
    public void onFinish(ITestContext context){
        ShutdownHook.writeApiTempJson();
    }
}
