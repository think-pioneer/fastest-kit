package org.fastest.core.internal.enhance;

import lombok.Getter;

import java.lang.annotation.Annotation;

/**
 * @Date: 2021/12/5
 */
@Getter
public class AnnotationGardener {
    private final Annotation annotation;
    private final Annotation hockAnnotation;

    public AnnotationGardener(Annotation annotation, Annotation hockAnnotation){
        this.annotation = annotation;
        this.hockAnnotation = hockAnnotation;
    }
}
