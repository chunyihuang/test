package com.h5.game.common.tools.check.support;

import com.h5.game.common.tools.check.exception.CardNumCountErrorException;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public class CardConvert {

    /**
     * 根据15位的身份证号码获得18位身份证号码
     * @param fifteenIDCard 15位的身份证号码
     * @return 升级后的18位身份证号码
     * @throws CardNumCountErrorException 如果不是15位的身份证号码，则抛出异常
     */
    public static String getEighteenIDCard(String fifteenIDCard) throws CardNumCountErrorException {
        if(fifteenIDCard != null && fifteenIDCard.length() == 15){
            StringBuilder sb = new StringBuilder();
            sb.append(fifteenIDCard.substring(0, 6))
                    .append("19")
                    .append(fifteenIDCard.substring(6));
            sb.append(getVerifyCode(sb.toString()));
            return sb.toString();
        } else {
            throw new CardNumCountErrorException("不是15位的身份证");
        }
    }

    /**
     * 获取校验码
     * @param idCardNumber 不带校验位的身份证号码（17位）
     * @return 校验码
     * @throws CardNumCountErrorException 如果身份证没有加上19，则抛出异常
     */
    public static char getVerifyCode(String idCardNumber) throws CardNumCountErrorException {
        if(idCardNumber == null || idCardNumber.length() < 17) {
            throw new CardNumCountErrorException("不合法的身份证号码");
        }
        char[] Ai = idCardNumber.toUpperCase().toCharArray();
        int[] Wi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] verifyCode = {'1','0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        int S = 0;
        int Y;
        for(int i = 0; i < Wi.length; i++){
            S += (Ai[i] - '0') * Wi[i];
        }
        Y = S % 11;
        return verifyCode[Y];
    }

    /**
     * 校验18位的身份证号码的校验位是否正确
     * @param idCardNumber 18位的身份证号码
     * @return
     * @throws CardNumCountErrorException
     */
    public static boolean verify(String idCardNumber) throws CardNumCountErrorException {
        if(idCardNumber == null || idCardNumber.length() != 18) {
            throw new CardNumCountErrorException("不是18位的身份证号码");
        }
        return getVerifyCode(idCardNumber) == idCardNumber.charAt(idCardNumber.length() - 1);
    }
}
