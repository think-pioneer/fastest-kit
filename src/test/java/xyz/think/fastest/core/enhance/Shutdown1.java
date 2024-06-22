package xyz.think.fastest.core.enhance;

import xyz.think.fastest.core.annotations.Component;

@Component
public class Shutdown1 implements Shutdown{
    @Override
    public int order() {
        return 0;
    }

    @Override
    public void executor() {
        System.out.println("shutdown2");
    }
}
