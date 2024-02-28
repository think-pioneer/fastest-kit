package xyz.think.fastest.core.enhance;

/**
 * @author: aruba
 * @date: 2022-02-10
 */
public enum MyObj {
    INSTANCE;
    private int flag = 1;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
