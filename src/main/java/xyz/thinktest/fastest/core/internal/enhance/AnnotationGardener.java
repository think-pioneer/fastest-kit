package xyz.thinktest.fastest.core.internal.enhance;

import java.lang.annotation.Annotation;

/**
 * @Date: 2021/12/5
 */
public class AnnotationGardener {
    /**
     * 字段上的注解
     */
    private final Annotation annotation;
    /**
     * 字段注解的hook注解
     */
    private final Annotation hookAnnotation;

    public AnnotationGardener(Annotation annotation, Annotation hookAnnotation){
        this.annotation = annotation;
        this.hookAnnotation = hookAnnotation;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Annotation getHookAnnotation() {
        return hookAnnotation;
    }

    @Override
    public String toString() {
        return "AnnotationGardener{" +
                "annotation=" + annotation +
                ", hockAnnotation=" + hookAnnotation +
                '}';
    }
}
