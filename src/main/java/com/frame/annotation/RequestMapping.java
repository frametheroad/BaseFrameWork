package com.frame.annotation;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
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

    String[] headers() default {"content-type:application/JSON;charset=UTF-8"};

    String[] consumes() default {};

    String[] produces() default {};

}
