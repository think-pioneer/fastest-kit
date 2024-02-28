package xyz.think.fastest.core.enhance.joinpoint.method;

import xyz.think.fastest.core.annotations.Component;
import xyz.think.fastest.core.annotations.Value;

/**
 * @author: aruba
 * @date: 2022-02-18
 */
@Component
public class Biz {
    @Value("k")
    String user;

    @LogPrint
    public void biz1(Num num){
        num.add(1);
    }

    public void biz2(Num num){
        num.add(1);
    }
}
