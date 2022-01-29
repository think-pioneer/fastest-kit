package xyz.thinktest.fastestapi.core.internal.tool;

import xyz.thinktest.fastestapi.core.internal.enhance.AnnotationGardener;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: aruba
 * @date: 2022-01-25
 */
public class MethodAnnotationProcessEntity implements Serializable {
    private static final long serialVersionUID = 3636090623704190498L;

    private final List<AnnotationGardener> beforeAnnotations;
    private final List<AnnotationGardener> afterAnnotations;
    private Method method;

    public MethodAnnotationProcessEntity(){
        this.beforeAnnotations = new ArrayList<>();
        this.afterAnnotations = new ArrayList<>();
    }

    public List<AnnotationGardener> getBeforeAnnotations() {
        return beforeAnnotations;
    }

    public List<AnnotationGardener> getAfterAnnotations() {
        return afterAnnotations;
    }

    public Method getMethod() {
        return method;
    }

    public void setBeforeAnnotations(List<AnnotationGardener> beforeAnnotations) {
        this.beforeAnnotations.addAll(beforeAnnotations);
    }

    public void setBeforeAnnotation(AnnotationGardener beforeAnnotation) {
        this.beforeAnnotations.add(beforeAnnotation);
    }

    public void setAfterAnnotations(List<AnnotationGardener> afterAnnotations) {
        this.afterAnnotations.addAll(afterAnnotations);
    }

    public void setAfterAnnotation(AnnotationGardener afterAnnotation) {
        this.afterAnnotations.add(afterAnnotation);
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "MethodAnnotationProcessEntity{" +
                "beforeAnnotations=" + beforeAnnotations +
                ", afterAnnotations=" + afterAnnotations +
                ", method=" + method +
                '}';
    }
}
