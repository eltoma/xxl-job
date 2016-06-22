package com.xxl.job.core.handler.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation for job handler
 *
 * @author 2016-5-17 21:06:49
 */
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JobHander {

    /**
     * job name defult beanName
     *
     * @return
     */
    String value() default "";

}
