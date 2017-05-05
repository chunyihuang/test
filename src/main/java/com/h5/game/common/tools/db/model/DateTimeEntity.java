package com.h5.game.common.tools.db.model;

import com.h5.game.common.tools.BaseUtil;

import java.util.Date;


public class DateTimeEntity {
    private String fieldName = "";
    private Date startDate;
    private Date endDate;

    public boolean isFull(){
        return !BaseUtil.isEmpty(fieldName) && startDate != null & endDate != null;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
