package com.leyou.order.enums;

public enum  OrderStatusEnum {//状态：1、未付款 2、已付款,未发货 3、已发货,未确认 4、交易成功 5、交易关闭 6、已评价
    UN_PAY(1,"未付款"),
    PAYED(2,"已付款,未发货"),
    UNCONFIRM(3,"已发货,未确认"),
    ORDER_SUCCESS(4,"交易成功"),
    ORDER_CLOSE(5,"交易关闭"),
    EVALUATED(6,"已评价") ;
    private Integer code;
    private String message;

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
