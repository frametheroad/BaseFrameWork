package com.frame.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface RequestMapping {

    String name() default "";

    @AliasFor("path")
//    String[] value() default {};
    String value() default "";
    @AliasFor("value")
    String[] path() default {};

    RequestMethod[] method() default {RequestMethod.GET};

    String[] params() default {};

    String[] headers() default {"content-type:application/JSO"};

    String[] consumes() default {};

    String[] produces() default {};

}
