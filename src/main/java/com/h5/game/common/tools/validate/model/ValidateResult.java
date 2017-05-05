package com.h5.game.common.tools.validate.model;

/**
 * 校验结果类
 * <ul>
 *    <li>
 *        verify -- 验证结果 true表示通过 false表示失败
 *    </li>
 *    <li>
 *        failReason -- 验证失败原因 verify为false时有值
 *    </li>
 *    <li>
 *        failFieldName -- 验证失败属性 verify为false时有值
 *    </li>
 * </ul>

 * Created by 黄春怡 on 2017/4/1.
 */
public class ValidateResult {

    private boolean pass;

    private String failReason;

    private String failFieldName;

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getFailFieldName() {
        return failFieldName;
    }

    public void setFailFieldName(String failFieldName) {
        this.failFieldName = failFieldName;
    }

    public ValidateResult() {
    }

    public ValidateResult(boolean pass) {
        this.pass = pass;
    }

    public ValidateResult(boolean pass, String failReason) {
        this.pass = pass;
        this.failReason = failReason;
    }

    public ValidateResult(boolean pass, String failReason, String failFieldName) {
        this.pass = pass;
        this.failReason = failReason;
        this.failFieldName = failFieldName;
    }

    @Override
    public String toString() {
        return "ValidateResult{" +
                "pass=" + pass +
                ", failReason='" + failReason + '\'' +
                ", failFieldName='" + failFieldName + '\'' +
                '}';
    }
}
