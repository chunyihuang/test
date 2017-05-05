package com.h5.game.common.tools.check.support;

import com.h5.game.common.tools.check.exception.CardNumCountErrorException;
import com.h5.game.common.tools.check.exception.CardPatternErrorException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PenitenceTK on 2016/9/23.
 */
public class CardDateSupport {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    public static Date paresCardDate(String idCard,boolean useRegex) throws CardNumCountErrorException, CardPatternErrorException, ParseException {
        if(!SimpleCheck.idCardValidate(idCard)) throw new CardNumCountErrorException("身份证格式错误,请确保身份证位数为18或者15位");
        String card = "";
        if(idCard.length() == 15){
            card = CardConvert.getEighteenIDCard(idCard);
        }else{
            card = idCard;
        }
        if(useRegex){
            if(!SimpleCheck.idCardValidate(card)) throw new CardPatternErrorException("身份证输入错误,身份证正则校验失败");
        }else{
            if(!CardConvert.verify(card)) throw new CardPatternErrorException("身份证输入错误,身份证校验失败");
        }
        String cardDateStr = subDateStrByCard(card);
        return dateFormat.parse(cardDateStr);
    }

    public static Date noVerifyPareseCardDate(String idCard) throws CardNumCountErrorException, ParseException {
        if(!SimpleCheck.idCardValidate(idCard)) throw new CardNumCountErrorException("身份证格式错误,请确保身份证位数为18或者15位");
        String card = "";
        if(idCard.length() == 15){
            card = CardConvert.getEighteenIDCard(idCard);
        }else{
            card = idCard;
        }
        String cardDateStr = subDateStrByCard(card);
        return dateFormat.parse(cardDateStr);
    }

    public static String subDateStrByCard(String idCard){
        assert idCard != null;
        return idCard.substring(6,14);
    }
}
