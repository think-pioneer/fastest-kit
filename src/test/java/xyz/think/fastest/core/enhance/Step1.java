package xyz.think.fastest.core.enhance;

import org.testng.step.Step;
import xyz.think.fastest.core.annotations.Component;

import java.util.concurrent.TimeUnit;

@Component
public class Step1 implements Step {
    @Override
    public boolean recovery() {
        try{
            TimeUnit.SECONDS.sleep(1);
        }catch (Exception e){}
        System.out.println("execute step.");
        return true;
    }
}
