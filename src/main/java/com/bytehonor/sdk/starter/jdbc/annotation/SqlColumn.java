package com.bytehonor.sdk.starter.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

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
