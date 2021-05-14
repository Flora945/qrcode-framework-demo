package com.jlpay.qrcode.external.business.protocol.enums;

public enum StatusType {
    //新外接码付接口与老外界码付接口Status字段转换
    WAIT("1", "W"),
    SUCC("2", "S"),
    FALI("3", "F"),
    CANCEL("4", "C"),
    REFUND("5", "R"),
    UNKOWN("", "");

    private String newStatus;
    private String oldStatus;

    private StatusType(String newStatus, String oldStatus){
        this.newStatus = newStatus;
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public static StatusType fromNewStatusType(String newStatus) {
        for(StatusType statusType: StatusType.values()) {
            if (statusType.newStatus.equals(newStatus)) {
                return statusType;
            }
        }
        return UNKOWN;
    }

    public static StatusType fromOldStatusType(String newStatus) {
        for(StatusType statusType: StatusType.values()) {
            if (statusType.oldStatus.equals(newStatus)) {
                return statusType;
            }
        }
        return UNKOWN;
    }
}
