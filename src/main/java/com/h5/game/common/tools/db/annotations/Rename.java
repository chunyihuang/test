package com.h5.game.common.tools.db.annotations;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface Rename {
    String value() default "";
}
