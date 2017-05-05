package com.h5.game.common.tools.validate.annotations;

import java.lang.annotation.*;


/**
 * Created by 黄春怡 on 2017/4/1.
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateBody {
    boolean NullPointPass() default false;
    String value() default "";
    String[] urlPath() default {};
}
