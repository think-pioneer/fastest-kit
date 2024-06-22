package xyz.think.fastest.core.enhance;

import org.testng.step.RecoveryExecutorStep;
import org.testng.step.Step;
import xyz.think.fastest.core.annotations.Component;

@Component
public class RecoveryExecutor1 extends RecoveryExecutorStep {

    @Override
    public boolean execute(long timeOut, boolean forceStop, Step... steps) {

        return super.execute(timeOut, forceStop, steps);
    }
}
