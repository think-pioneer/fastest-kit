package xyz.thinktest.fastestapi.core.enhance;

/**
 * @author: aruba
 * @date: 2022-02-10
 */
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
