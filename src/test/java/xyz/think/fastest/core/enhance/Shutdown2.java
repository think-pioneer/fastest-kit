package xyz.think.fastest.core.enhance;

import xyz.think.fastest.core.annotations.Component;

@Component
public class Shutdown2 implements Shutdown{
    @Override
    public int order() {
        return 1;
    }

    @Override
    public void executor() {
        System.out.println("shutdown2");
    }
}
