package xyz.think.fastest.core.enhance;

import xyz.think.fastest.core.annotations.Component;

/**
 * @author: aruba
 * @date: 2022-02-10
 */
@Component
public class Init1 implements Initialize{
    @Override
    public int order() {
        return 0;
    }

    @Override
    public void executor() {
        System.out.println("init1");
        MyObj.INSTANCE.setFlag(MyObj.INSTANCE.getFlag() + 1);
    }
}
