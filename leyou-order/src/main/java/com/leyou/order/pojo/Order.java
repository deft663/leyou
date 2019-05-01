package com.leyou.order.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @author zhang
 * @date 2019年04月28日 15:36
 */
@Table(name="tb_order")
public class Order {
    @Id
    @JsonSerialize(using=ToStringSerializer.class)
    private Long order_id; //订单id
    private Long total_pay;//总金额，单位为分
    private Long actual_pay; //实付金额。单位:分。如:20007，表示:200元7分
    private String promotion_ids;//促销活动id
    private Integer payment_type;//支付类型，1、在线支付，2、货到付款
    private Long post_fee;//邮费。单位:分。如:20007，表示:200元7分
    private Date create_time;//订单创建时间
    private String shipping_name;//物流名称
    private String shipping_code;//物流单号
    private Long user_id; //用户id
    private String buyer_message;//买家留言
    private String buyer_nick;//买家昵称
    private Boolean buyer_rate;//买家是否已经评价,0未评价，1已评价
    private String receiver_state;//收获地址（省）
    private String receiver_city;//收获地址（市）
    private String receiver_district;//收获地址（区/县）
    private String receiver_address;//收获地址（街道、住址等详细地址）
    private String receiver_mobile;//收货人手机
    private String receiver_zip;//收货人邮编
    private String receiver;//收货人
    private Integer invoice_type;//发票类型(0无发票1普通发票，2电子发票，3增值税发票)
    private Integer source_type;//订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端

    @Transient
    private List<OrderDetail> orderDetails;
    @Transient
    private OrderStatus orderStatus;

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public Long getTotal_pay() {
        return total_pay;
    }

    public void setTotal_pay(Long total_pay) {
        this.total_pay = total_pay;
    }

    public Long getActual_pay() {
        return actual_pay;
    }

    public void setActual_pay(Long actual_pay) {
        this.actual_pay = actual_pay;
    }

    public String getPromotion_ids() {
        return promotion_ids;
    }

    public void setPromotion_ids(String promotion_ids) {
        this.promotion_ids = promotion_ids;
    }

    public Integer getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(Integer payment_type) {
        this.payment_type = payment_type;
    }

    public Long getPost_fee() {
        return post_fee;
    }

    public void setPost_fee(Long post_fee) {
        this.post_fee = post_fee;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getShipping_name() {
        return shipping_name;
    }

    public void setShipping_name(String shipping_name) {
        this.shipping_name = shipping_name;
    }

    public String getShipping_code() {
        return shipping_code;
    }

    public void setShipping_code(String shipping_code) {
        this.shipping_code = shipping_code;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getBuyer_message() {
        return buyer_message;
    }

    public void setBuyer_message(String buyer_message) {
        this.buyer_message = buyer_message;
    }

    public String getBuyer_nick() {
        return buyer_nick;
    }

    public void setBuyer_nick(String buyer_nick) {
        this.buyer_nick = buyer_nick;
    }

    public Boolean getBuyer_rate() {
        return buyer_rate;
    }

    public void setBuyer_rate(Boolean buyer_rate) {
        this.buyer_rate = buyer_rate;
    }

    public String getReceiver_state() {
        return receiver_state;
    }

    public void setReceiver_state(String receiver_state) {
        this.receiver_state = receiver_state;
    }

    public String getReceiver_city() {
        return receiver_city;
    }

    public void setReceiver_city(String receiver_city) {
        this.receiver_city = receiver_city;
    }

    public String getReceiver_district() {
        return receiver_district;
    }

    public void setReceiver_district(String receiver_district) {
        this.receiver_district = receiver_district;
    }

    public String getReceiver_address() {
        return receiver_address;
    }

    public void setReceiver_address(String receiver_address) {
        this.receiver_address = receiver_address;
    }

    public String getReceiver_mobile() {
        return receiver_mobile;
    }

    public void setReceiver_mobile(String receiver_mobile) {
        this.receiver_mobile = receiver_mobile;
    }

    public String getReceiver_zip() {
        return receiver_zip;
    }

    public void setReceiver_zip(String receiver_zip) {
        this.receiver_zip = receiver_zip;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Integer getInvoice_type() {
        return invoice_type;
    }

    public void setInvoice_type(Integer invoice_type) {
        this.invoice_type = invoice_type;
    }

    public Integer getSource_type() {
        return source_type;
    }

    public void setSource_type(Integer source_type) {
        this.source_type = source_type;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
