package com.h5.game.common.tools.check.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public class CheckResult {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private String card;

    private Date cardDate;

    private Date birthDate;

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public Date getCardDate() {
        return cardDate;
    }

    public void setCardDate(Date cardDate) {
        this.cardDate = cardDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public boolean getResult(){
        return dateFormat.format(birthDate).equals(dateFormat.format(cardDate));
    }
}
