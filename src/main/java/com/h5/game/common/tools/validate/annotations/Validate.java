package com.h5.game.common.tools.validate.annotations;

import com.h5.game.common.tools.validate.support.DefaultRegexString;
import com.h5.game.common.tools.validate.type.ValidateType;

import java.lang.annotation.*;

/**
 * 请求参数校验,放在控制器的Integer或者String类型等对象中可以进行校验
 * <ul>
 *     <li>
 *         type -- 校验方式,目前有3中,非空校验,长度校验,和自定义正则表达式校验 {@link ValidateType}
 *     </li>
 *     <li>
 *         maxLength 和 minLength -- 当type为Length校验方式的时候需要,定义最大最小长度
 *     </li>
 *     <li>
 *         regex -- 当type为Custom的时候需要,定义校验的正则表达式,默认正则表达式请看{@link DefaultRegexString}
 *     </li>
 * </ul>
 * Created by 黄春怡 on 2017/4/1.
 */
@Target({ElementType.TYPE, ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validate {
    /**
     * 配置校验方式
     * @return 返回校验方式
     */
    ValidateType type() default ValidateType.Required;

    /**
     * 配置最大长度 --- ValidateType.Length
     * @return
     */
    int maxLength() default 255;

    /**
     * 配置最小长度   --- ValidateType.Length
     * @return
     */
    int minLength() default 0;

    /**
     * 配置自定义正则表达式校验  -- ValidateType.Custom
     * @return
     */
    String regex() default "";

    /**
     * 定义校验失败时候返回的名称
     */
    String name() default "";

    /**
     * 定义校验失败时候返回的原因
     */
    String reason() default"";

    /**
     * 需要校验的url路径
     */
    String[] urlPath() default{};
}
