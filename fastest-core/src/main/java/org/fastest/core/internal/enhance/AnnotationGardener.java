package org.fastest.core.internal.enhance;

import java.lang.annotation.Annotation;

/**
 * @Date: 2021/12/5
 */
public class AnnotationGardener {
    private final Annotation annotation;
    private final Annotation hockAnnotation;

    public AnnotationGardener(Annotation annotation, Annotation hockAnnotation){
        this.annotation = annotation;
        this.hockAnnotation = hockAnnotation;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Annotation getHockAnnotation() {
        return hockAnnotation;
    }

    @Override
    public String toString() {
        return "AnnotationGardener{" +
                "annotation=" + annotation +
                ", hockAnnotation=" + hockAnnotation +
                '}';
    }
}
