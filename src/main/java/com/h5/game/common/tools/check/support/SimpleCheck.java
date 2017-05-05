package com.h5.game.common.tools.check.support;

import com.h5.game.common.tools.BaseUtil;
import com.h5.game.common.tools.check.exception.CardNumCountErrorException;
import com.h5.game.common.tools.check.exception.CardPatternErrorException;
import com.h5.game.common.tools.check.model.CheckResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PenitenceTK on 2016/9/23.
 */
public class SimpleCheck {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    public CheckResult doCheck(String card, Date birthDate) throws CardNumCountErrorException, ParseException, CardPatternErrorException {
        return doCheck(card,birthDate,false);
    }

    public CheckResult doCheck(String card, Date birthDate, boolean userRegex) throws ParseException, CardNumCountErrorException, CardPatternErrorException {
        CheckResult checkResult = new CheckResult();
        Date cardDate = CardDateSupport.paresCardDate(card,userRegex);
        checkResult.setBirthDate(birthDate);
        checkResult.setCard(card);
        checkResult.setCardDate(cardDate);
        return checkResult;
    }
    public static boolean idCardValidate(String card){
        if(card.length() != 15 && card.length() != 18) return false; //先检测位数是否正确
        String regex = "\\d{17}[\\d|x|X]|\\d{15}";
        return BaseUtil.doRegexMatch(regex,card); //根据正则表达式进行校验
    }
}
