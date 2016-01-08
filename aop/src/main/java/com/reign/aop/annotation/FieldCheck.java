package com.reign.aop.annotation;

import java.lang.annotation.*;

/**
 * Created by ji on 15-10-20.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldCheck {

    int LENGTH_MAX_DEFAULT = Integer.MAX_VALUE;
    int LENGTH_MIN_DEFAULT = -1;
    String REGEX_DEFAULT = "";


    /**
     * 不为空，即不等于NULL
     *
     * @return
     */
    boolean NotNull() default false;

    /**
     * 不为空且不能为空字符串
     *
     * @return
     */
    boolean NotEmpty() default false;

    /**
     * 不为数字类型
     *
     * @return
     */
    boolean NotNumeric() default false;

    /**
     * 字符串最大长度
     *
     * @return
     */
    int LengthMax() default LENGTH_MAX_DEFAULT;

    /**
     * 字符串最小长度
     *
     * @return
     */
    int LengthMin() default LENGTH_MIN_DEFAULT;

    /**
     * 正则表达式
     *
     * @return
     */
    String Regex() default "";

}
