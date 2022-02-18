package xyz.thinktest.fastestapi.core.enhance.joinpoint.method;

import xyz.thinktest.fastestapi.core.annotations.Pointcut;

/**
 * @author: aruba
 * @date: 2022-02-18
 */
@Pointcut(annotation = LogPrint.class)
public class MethodProcessableAnn implements MethodProcessable{
    @Override
    public void process(JoinPoint joinPoint) {
        Num num = joinPoint.getArg(Num.class, 1);
        num.add(1);
    }
}
