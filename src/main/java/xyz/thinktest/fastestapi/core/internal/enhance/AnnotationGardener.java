package xyz.thinktest.fastestapi.core.internal.enhance;

import xyz.thinktest.fastestapi.core.enhance.Processable;

import java.lang.annotation.Annotation;

/**
 * @Date: 2021/12/5
 */
public class AnnotationGardener implements Comparable<AnnotationGardener>{
    /**
     * 字段上的注解
     */
    private final Annotation annotation;
    /**
     * 字段注解的hook注解
     */
    private final Processable process;

    private final Integer index;

    public AnnotationGardener(Annotation annotation, Processable process, int index){
        this.annotation = annotation;
        this.process = process;
        this.index = index;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Processable getProcess() {
        return this.process;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "AnnotationGardener{" +
                "annotation=" + annotation +
                ", process=" + process +
                ", index=" + index +
                '}';
    }

    @Override
    public int compareTo(AnnotationGardener annotationGardener){
        return this.index.compareTo(annotationGardener.index);
    }
}
