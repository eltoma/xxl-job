package com.xxl.job.core.handler.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation for template job handler
 * <p>
 * 模板job执行会创建一个线程池，以运行当前的handler
 *
 * @author 2016-5-17 21:06:49
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JobHanderRepository {


    /**
     * max thread
     * <p>
     * 线程池中最多线程数
     *
     * @return
     */
    int max() default 1;

    /**
     * min thread
     * <p>
     * 线程池中最少存活的线程数
     *
     * @return
     */
    int min() default 1;

}
