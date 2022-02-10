package xyz.thinktest.fastestapi.core.enhance;

/**
 * @author: aruba
 * @date: 2022-02-10
 */
public class Init1 implements Initialize{
    @Override
    public void preHook() {
        MyObj.INSTANCE.setFlag(MyObj.INSTANCE.getFlag() + 1);
    }
}
