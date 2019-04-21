package com.leyou.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE,ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
    int age();
}
