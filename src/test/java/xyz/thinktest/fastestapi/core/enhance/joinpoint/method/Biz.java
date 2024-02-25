package xyz.thinktest.fastestapi.core.enhance.joinpoint.method;

import xyz.thinktest.fastestapi.core.annotations.Component;
import xyz.thinktest.fastestapi.core.annotations.Value;

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
