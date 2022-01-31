package xyz.thinktest.fastestapi.core.internal.enhance;

import com.google.common.collect.Lists;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.collections4.CollectionUtils;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;
import xyz.thinktest.fastestapi.common.exceptions.CaptureException;
import xyz.thinktest.fastestapi.common.exceptions.EnhanceException;
import xyz.thinktest.fastestapi.core.annotations.Capture;
import xyz.thinktest.fastestapi.core.internal.enhance.methodhelper.MethodProcess;
import xyz.thinktest.fastestapi.core.internal.scanner.MethodAnnotationProcessCache;
import xyz.thinktest.fastestapi.core.internal.scanner.MethodAnnotationProcessEntity;
import xyz.thinktest.fastestapi.logger.FastestLogger;
import xyz.thinktest.fastestapi.logger.FastestLoggerFactory;
import xyz.thinktest.fastestapi.utils.ObjectUtil;
import xyz.thinktest.fastestapi.utils.reflects.ReflectUtil;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * @Date: 2021/3/29
 */

public class MethodHandler implements MethodEnhancer {
    private final MethodAnnotationProcessCache cache = MethodAnnotationProcessCache.INSTANCE;
    @Override
    public Object intercept(Object origin, Method method, Object[] args, MethodProxy methodProxy) {
        Capture capture = method.getAnnotation(Capture.class);
        try {
            Target target = new Target();
            target.setInstance(origin);
            return proxy(target, method, args, methodProxy);
        }catch (Throwable c){
            CaptureException cause = new CaptureException(c);
            Class<?> clazz = method.getDeclaringClass();
            FastestLogger logger = FastestLoggerFactory.getLogger(clazz.getSimpleName());
            if(Objects.nonNull(capture)){
                String message = capture.message();
                message = "".equals(message.trim()) ? cause.getMessage() : message;
                logger.error(message, cause);
                if(capture.isThrow() && capture.exception().isInstance(cause) ){
                    throw cause;
                }
            }else{
                logger.error(cause.getMessage(), cause);
                throw cause;
            }
            return null;
        }
    }

    private Object proxy(Target target, Method method, Object[] args, MethodProxy methodProxy){
        MethodAnnotationProcessEntity entity = cache.get(method);
        List<AnnotationGardener> beforeAnnotations = Lists.newArrayList();
        List<AnnotationGardener> afterAnnotations = Lists.newArrayList();
        if(Objects.nonNull(entity)){
            beforeAnnotations = entity.getBeforeAnnotations();
            afterAnnotations = entity.getAfterAnnotations();
        }

        if(CollectionUtils.isNotEmpty(beforeAnnotations)) {
            MethodProcess methodProcess = new MethodProcess(beforeAnnotations);
            methodProcess.process(target, method, args);
        }
        Object result = invoke(method, methodProxy, target.getInstance(), args);
        if(CollectionUtils.isNotEmpty(afterAnnotations)){
            MethodProcess methodProcess = new MethodProcess(afterAnnotations);
            methodProcess.process(target, method, args);
        }
        return result;
    }

    private Object invoke(Method method, MethodProxy methodProxy, Object object, Object[] args){
        try {
            if(ReflectUtil.isInterface(method) || ReflectUtil.isAbstract(method)){
                return null;
            }
            if(method.isDefault()){
                Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
                constructor.setAccessible(true);
                Class<?> declaringClass = method.getDeclaringClass();
                int allModes = MethodHandles.Lookup.PUBLIC | MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED | MethodHandles.Lookup.PACKAGE;
                return constructor.newInstance(declaringClass, allModes)
                        .unreflectSpecial(method, declaringClass)
                        .bindTo(object)
                        .invokeWithArguments(args);
            }
            return methodProxy.invokeSuper(object, args);
        }catch (Throwable cause){
            throw new EnhanceException(ObjectUtil.format("run method error: {}->{}",method.getDeclaringClass().getName(), method.getName()), cause);
        }
    }
}
