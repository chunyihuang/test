package com.h5.game.common.tools.validate.support;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public final class DefaultRegexString {

    /**
     * 电子邮箱
     */
    public static final String EMAIL_PATTERN = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

    /**
     * 手机号码,国内
     */
    public static final String MOBILE_PHONE_PATTERN = "0?(13|14|15|18)[0-9]{9}$";

    /**
     * 电话号码,国内,座机
     */
    public static final String PHONE_PATTERN = "[0-9-()（）]{7,18}$";

    /**
     * 身份证号码
     */
    public static final String IDENTITY_CARD_PATTERN = "\\d{17}[\\d|x|X]$|\\d{15}$";

    /**
     * 网站URL
     */
    public static final String URL_PATTERN = "^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+";

    /**
     * 正整数
     */
    public static final String POSITIVE_INTEGER_PATTERN = "^[1-9]\\d*$";
}
