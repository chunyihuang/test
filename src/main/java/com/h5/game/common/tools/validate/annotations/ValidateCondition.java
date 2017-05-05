package com.h5.game.common.tools.validate.annotations;

import java.lang.annotation.*;

/**
 * 定义校验进行的条件
 * <ul>
 *     <li>
 *         condition -- 需要比较的属性
 *     </li>
 *     <li>
 *         compareValue -- 需要比较的值
 *     </li>
 * </ul>
 * Created by 黄春怡 on 2017/4/1.
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateCondition {
    String[] condition();
    String[] compareValue();
}
