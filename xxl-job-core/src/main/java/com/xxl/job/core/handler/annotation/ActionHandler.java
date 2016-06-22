package com.xxl.job.core.handler.annotation;

import com.xxl.job.core.constant.ActionEnum;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation for action handler
 *
 * @author 2016-5-17 21:06:49
 */
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionHandler {

    ActionEnum[] value();

}
