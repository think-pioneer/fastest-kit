package org.testng.listener;

import org.fastest.core.internal.Initialization;
import org.fastest.core.internal.enhance.ShutdownHook;
import org.fastest.core.internal.enhance.fieldhelper.Target;
import org.fastest.core.internal.enhance.fieldhelper.TestNGProcess;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.util.Objects;

/**
 * @Date: 2020/10/30
 */
public class TestCaseListener implements ITestListener {
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
                Target manger = new Target();
                manger.setInstance(method.getInstance());
                TestNGProcess process = new TestNGProcess(caseClazz);
                process.process(manger);
            }
        }
    }

    @Override
    public void onFinish(ITestContext context){
        ShutdownHook.writeApiTempJson();
    }
}
