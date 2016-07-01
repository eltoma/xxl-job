package com.xxl.job.core.log.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation for log view
 * <p>
 * mark deal log view
 *
 * @author 2016-5-17 21:06:49
 */
@Component
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogView {

    /**
     * view param
     *
     * @return
     */
    String[] value() default {};

    /**
     * 模板类型
     *
     * @return
     */
    String type() default "FreeMarker";


}
