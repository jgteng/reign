package com.reign.aop.interceptor;


import com.reign.aop.advisor.AopAdvisor;
import com.reign.aop.advisor.impl.ParamCheckAdvisor;
import com.reign.aop.annotation.ParamCheck;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by ji on 15-10-16.
 */
public class ParamCheckInterceptor implements MethodInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParamCheckInterceptor.class);

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        LOGGER.debug("执行参数校验");
        Method method = methodInvocation.getMethod();
        Annotation[] annotations = method.getDeclaredAnnotations();
        if (annotations == null || annotations.length <= 0) {
            return methodInvocation.proceed();
        }

        return this.checkParam(annotations,methodInvocation);
    }

    private Object checkParam(Annotation[] annotations, MethodInvocation methodInvocation) throws Throwable {
        for (Annotation annotation : annotations) {
            if (annotation != null) {
                AopAdvisor aopAdvisor = null;
                if (annotation instanceof ParamCheck) {
                    aopAdvisor = new ParamCheckAdvisor();
                }
                if (aopAdvisor != null) {
                    aopAdvisor.invoke(methodInvocation, annotation);
                }
            }
        }
        return methodInvocation.proceed();
    }
}
