package com.bytehonor.sdk.repository.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * 字段列映射注解。
 * <p>
 * 用于声明模型字段与数据库列名的对应关系，支持通过 {@code ignore=true}
 * 跳过当前字段的持久化读写。
 * </p>
 *
 * @author lijianqiang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface SqlColumn {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean ignore() default false;
}
