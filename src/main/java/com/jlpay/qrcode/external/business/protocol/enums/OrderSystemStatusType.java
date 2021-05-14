package com.jlpay.qrcode.external.business.protocol.enums;

/**
 * @Description: 订单系统状态枚举
 * @author: lvlinyang
 * @date: 2021/3/31 21:04
 */
public enum OrderSystemStatusType {

    /**初始订单*/
    NEW("0","0", "初始"),

    /**待确认*/
    WAIT("1","1", "待确认"),

    /**交易成功*/
    SUCCESS("2","2", "交易成功"),

    /**交易失败*/
    FAIL("3","3", "交易失败"),

    /**已撤销*/
    CANCEL("4","3", "已撤销"),

    /**已退款*/
    REFUND("5",null, "已退款"),

    PRE_AUTH_COMPLETED("", null, "担保交易完成");

    /**0-新建订单，1-等待确认，2-成功，3-失败,4-已撤销/已冲正，5-已退款*/
    private String outStatus;
    /**交易状态  0-初始 1-待确认 2-成功 3-失败 */
    private String orderSystemStatus;

    /**
     * 中文描述
     */
    private String cnDesc;

    OrderSystemStatusType(String outStatus, String orderSystemStatus, String cnDesc) {
        this.outStatus = outStatus;
        this.orderSystemStatus = orderSystemStatus;
        this.cnDesc = cnDesc;
    }

    public String getOutStatus() {
        return outStatus;
    }

    public String getOrderSystemStatus() {
        return orderSystemStatus;
    }

    public String getCnDesc() {
        return cnDesc;
    }

    public static String convertStatus(String outStatus){
        for (OrderSystemStatusType statusType : values()) {
            if (statusType.getOutStatus().equals(outStatus)){
                return statusType.getOrderSystemStatus();
            }
        }

        return null;
    }
}
