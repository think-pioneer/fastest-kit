package xyz.think.fastest.core.enhance.joinpoint.method;

import xyz.think.fastest.core.enhance.Processable;

/**
 * @Date: 2021/11/28
 * annotation process interface
 */
public interface MethodProcessable extends Processable {

    void process(JoinPoint joinPoint);
}
