package xyz.think.fastest.core.enhance;

/**
 * @author: aruba
 * @date: 2022-02-10
 */
public class Init2 implements Initialize{
    @Override
    public int order() {
        return 1;
    }

    @Override
    public void executor() {
        System.out.println("init2");
        MyObj.INSTANCE.setFlag(MyObj.INSTANCE.getFlag() + 1);
    }
}
