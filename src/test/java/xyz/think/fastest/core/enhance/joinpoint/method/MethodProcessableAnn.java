package xyz.think.fastest.core.enhance.joinpoint.method;

import xyz.think.fastest.core.annotations.Pointcut;

/**
 * @author: aruba
 * @date: 2022-02-18
 */
@Pointcut(annotation = LogPrint.class, before = true, after = true)
public class MethodProcessableAnn implements MethodProcessable{
    @Override
    public void process(JoinPoint joinPoint) {
        Num num = joinPoint.getArg(Num.class, 1);
        num.add(1);
    }
}
