package com.reign.aop.advisor.impl;

import com.reign.aop.advisor.AopAdvisor;
import com.reign.aop.annotation.ParamCheck;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ji on 15-10-20.
 */
public class ParamCheckAdvisor implements AopAdvisor {

    public void invoke(MethodInvocation invocation, Annotation annotation) throws Throwable {
        if (annotation == null) {
            return;
        }

        ParamCheck paramCheck = (ParamCheck) annotation;

        if (paramCheck.check()) {
            this.doParamCheck(invocation, paramCheck);
        }
    }

    private void doParamCheck(MethodInvocation invocation, ParamCheck paramCheck) {
        String[] includes = paramCheck.include();

        if (includes == null || includes.length <= 0) {
            return;
        }

        String pattern = "\\$([1-9]\\d*|0)(\\[([a-zA-Z0-9]{1,}(,[a-zA-Z0-9]{1,})*)*\\])*";
        Pattern pat = Pattern.compile(pattern);

        Object[] argubments = invocation.getArguments();

        for (String include : includes) {
            Matcher m = pat.matcher(include);
            if (!m.matches()) {
                throw new RuntimeException("【ParamCheck】include属性校验不合法："+include);
            }

        }
    }
}
