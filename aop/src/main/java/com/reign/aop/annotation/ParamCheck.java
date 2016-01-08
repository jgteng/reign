package com.reign.aop.annotation;

import java.lang.annotation.*;

/**
 * Created by ji on 15-10-16.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamCheck {

    /**
     * 是否验证 默认为验证true
     * @return
     */
    public boolean check() default true;

    /**
     * 设定需要校验的参数
     * $0表示第1个参数
     * $1表示第2个参数
     * $1[a,b] 表示第2个参数的a、b属性
     * 例子：
     *
     * @return
     * @ParamCheck(include={"$0","$1[id,name]"}) public void check(UserBean user,DataBean data)
     * <p/>
     * 以上例子被解释为：第1个参数进行校验，第二个参数的id,name属性校验
     */
    public String [] include() default {};

}
