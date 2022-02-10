package xyz.thinktest.fastestapi.core.enhance.joinpoint.method;

import xyz.thinktest.fastestapi.core.enhance.Processable;

/**
 * @Date: 2021/11/28
 * annotation process interface
 */
public interface MethodProcessable extends Processable {

    void process(JoinPoint joinPoint);
}
