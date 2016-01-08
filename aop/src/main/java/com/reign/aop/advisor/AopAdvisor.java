package com.reign.aop.advisor;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;

/**
 * Created by ji on 15-10-20.
 */
public interface AopAdvisor {

    public void invoke(MethodInvocation invocation, Annotation annotation) throws Throwable;

}
