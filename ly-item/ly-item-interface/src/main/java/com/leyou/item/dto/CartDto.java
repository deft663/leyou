package com.leyou.item.dto;

/**
 * @author zhang
 * @date 2019年04月29日 09:31
 */
public class CartDto {
    private Long skuId;
    private Integer num;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
