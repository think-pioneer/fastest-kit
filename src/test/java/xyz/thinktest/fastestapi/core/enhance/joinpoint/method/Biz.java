package xyz.thinktest.fastestapi.core.enhance.joinpoint.method;

import xyz.thinktest.fastestapi.core.annotations.Component;

/**
 * @author: aruba
 * @date: 2022-02-18
 */
@Component
public class Biz {
    @LogPrint
    public void biz(Num num){
        num.add(1);
    }
}
