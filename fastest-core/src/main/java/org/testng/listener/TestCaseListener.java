package org.testng.listener;

import lombok.SneakyThrows;
import org.fastest.core.internal.Initialization;
import org.fastest.core.internal.enhance.ShutdownHook;
import org.fastest.core.internal.enhance.fieldhelper.Target;
import org.fastest.core.internal.enhance.fieldhelper.TestNGProcess;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;

import java.util.Objects;

/**
 * @Date: 2021/10/30
 */
public class TestCaseListener implements ITestListener {
    @SneakyThrows
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
