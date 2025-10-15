package com.bytehonor.sdk.repository.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 数据模型映射表
 * </pre>
 * 
 * @author lijianqiang
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SqlTableLeftJoin {

    Class<?> main();

    Class<?> sub();

    String on() default "";

}
