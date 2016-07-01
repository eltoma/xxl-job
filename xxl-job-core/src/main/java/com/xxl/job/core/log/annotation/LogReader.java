package com.xxl.job.core.log.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation for log reader
 *
 * @author 2016-5-17 21:06:49
 */
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogReader {

    /**
     * job handler name
     *
     * @return
     */
    String forJobHandler();

}
