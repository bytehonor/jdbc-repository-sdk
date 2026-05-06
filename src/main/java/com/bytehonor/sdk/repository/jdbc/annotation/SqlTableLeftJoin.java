package com.bytehonor.sdk.repository.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 左关联查询模型映射定义。
 * </pre>
 *
 * 通过 main/sub 指定主表与从表模型，on 指定关联列。
 *
 * @author lijianqiang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SqlTableLeftJoin {

    Class<?> main();

    Class<?> sub();

    String on() default "";

}
