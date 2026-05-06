package com.bytehonor.sdk.repository.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * <pre>
 * 数据模型与数据库表映射。
 * </pre>
 *
 * name/value 用于声明表名；primary 用于声明主键列名。
 *
 * @author lijianqiang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SqlTable {

    @AliasFor("name")
    String value() default "";

    String name() default "";

    String primary() default "id";

}
