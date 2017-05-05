package com.h5.game.common.tools.validate.annotations;

import java.lang.annotation.*;

/**
 * 请求参数校验,控制器方法参数Bean对象中,或者放在对象内部的对象属性中,可以进行校验 <br>
 * Created by 黄春怡 on 2017/4/1.
 */
@Target({ElementType.TYPE, ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestValidate {
}
