package xyz.thinktest.fastestapi.core.enhance.joinpoint.field;

import xyz.thinktest.fastestapi.core.enhance.Processable;

/**
 * @Date: 2021/10/28
 */
public interface FieldProcessable extends Processable {

    /**
     * enhance feature process method of annotation
    */

    void process(JoinPoint joinPoint);
}
