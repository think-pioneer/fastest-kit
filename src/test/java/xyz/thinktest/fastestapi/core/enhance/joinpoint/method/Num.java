package xyz.thinktest.fastestapi.core.enhance.joinpoint.method;

/**
 * @author: aruba
 * @date: 2022-02-18
 */
public class Num {
    private int num;
    public Num(int num){
        this.num = num;
    }

    public void add(int num){
        this.num = this.num + num;
    }

    public int getValue(){
        return this.num;
    }
}
