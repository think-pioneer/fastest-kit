package xyz.thinktest.fastestapi.core.internal.tool;

import xyz.thinktest.fastestapi.core.internal.enhance.AnnotationGardener;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author: aruba
 * @date: 2022-01-25
 */
public class MethodAnnotationProcessEntity implements Serializable {
    private static final long serialVersionUID = 3636090623704190498L;

    private final List<AnnotationGardener> beforeAnnotations;
    private final List<AnnotationGardener> afterAnnotations;
    private final Method method;

    public MethodAnnotationProcessEntity(List<AnnotationGardener> beforeAnnotations, List<AnnotationGardener> afterAnnotations, Method method) {
        this.beforeAnnotations = beforeAnnotations;
        this.afterAnnotations = afterAnnotations;
        this.method = method;
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

    @Override
    public String toString() {
        return "MethodAnnotationProcessEntity{" +
                "beforeAnnotations=" + beforeAnnotations +
                ", afterAnnotations=" + afterAnnotations +
                ", method=" + method +
                '}';
    }
}
