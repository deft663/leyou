package com.leyou.order.dto;


import com.leyou.item.dto.CartDto;

import java.util.List;

/**
 * @author zhang
 * @date 2019年04月28日 15:36
 */
public class OrderDto {
    private Integer addressId;
    private List<CartDto> carts;
    private Integer payType;

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public List<CartDto> getCarts() {
        return carts;
    }

    public void setCarts(List<CartDto> carts) {
        this.carts = carts;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }
}
