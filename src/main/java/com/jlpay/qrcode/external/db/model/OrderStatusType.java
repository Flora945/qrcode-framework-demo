package com.jlpay.qrcode.external.db.model;


import com.jlpay.utils.exception.BusiException;

public enum  OrderStatusType {

    /**初始订单*/
    NEW("0","0", "初始"),

    /**待确认*/
    WAIT("1","1", "1", "待确认"),

    /**交易成功*/
    SUCCESS("2","s", "2", "交易成功"),

    /**交易失败*/
    FAIL("3","f", "交易失败"),

    /**已撤销*/
    CANCEL("4","c", "已撤销"),

    /**已退款*/
    REFUND("5","r", "已退款"),

    PRE_AUTH_COMPLETED("", null, "担保交易完成"),
    ;

    /**0-新建订单，1-等待确认，2-成功，3-失败,4-已撤销/已冲正，5-已退款*/
    private String outStatus;
    /**交易状态  0-初始订单；1-待确认订单，s-交易成功，f-交易失败，c-交易已撤销，r-交易已退款*/
    private String enginStatus;

    /**
     * 担保交易状态, 1-受理成功 2-支付成功 其他失败
     */
    private String guaranteeStatus = "";

    /**
     * 中文描述
     */
    private String cnDesc;

    OrderStatusType(String outStatus, String enginStatus, String guaranteeStatus, String cnDesc) {
        this.outStatus = outStatus;
        this.enginStatus = enginStatus;
        this.guaranteeStatus = guaranteeStatus;
        this.cnDesc = cnDesc;
    }

    OrderStatusType(String outStatus, String enginStatus, String cnDesc){
        this.outStatus = outStatus;
        this.enginStatus = enginStatus;
        this.cnDesc = cnDesc;
    }

    public static OrderStatusType getStatusType(String enginStatus){
        OrderStatusType[] types = OrderStatusType.values();
        for(OrderStatusType type:types){
            if(type.getEnginStatus().equals(enginStatus)){
                return type;
            }
        }
        throw new BusiException(enginStatus+"找不到相对应的订单状态[STATUS.MAP.ERROR]");
    }

    public static boolean isFinalStatus(OrderStatusType status) {
        switch (status) {
            case SUCCESS:
            case FAIL:
            case CANCEL:
            case REFUND:
                return true;
            default:
                return false;
        }
    }

    /**
     * 通过担保交易状态找到对应的交易状态枚举
     *
     * @param guaranteeStatus 担保交易状态
     * @return 状态枚举
     */
    public static OrderStatusType fromGuaranteeStatus(String guaranteeStatus) {
        for (OrderStatusType type : values()) {
            if (type.guaranteeStatus.equals(guaranteeStatus)) {
                return type;
            }
        }
        return FAIL;
    }

    public static OrderStatusType getOutStautsType(String outStatus){
        OrderStatusType[] types = OrderStatusType.values();
        for(OrderStatusType type:types){
            if(type.outStatus.equals(outStatus)){
                return type;
            }
        }
        throw new BusiException(outStatus+"找不到相对应的状态[STATUS.MAP.ERROR]");
    }
    public String getOutStatus() {
        return outStatus;
    }

    public String getEnginStatus() {
        return enginStatus;
    }

    public String getGuaranteeStatus() {
        return guaranteeStatus;
    }

    public String getCnDesc() {
        return cnDesc;
    }
}
