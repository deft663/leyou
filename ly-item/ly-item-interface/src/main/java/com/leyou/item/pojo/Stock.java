package com.leyou.item.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_stock")
public class Stock {
    @Id
    private Long skuId;
    private Integer seckill_stock;//可秒杀库存
    private Integer seckill_total;//秒杀总数量
    private Integer stock;//库存数量

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getSeckill_stock() {
        return seckill_stock;
    }

    public void setSeckill_stock(Integer seckill_stock) {
        this.seckill_stock = seckill_stock;
    }

    public Integer getSeckill_total() {
        return seckill_total;
    }

    public void setSeckill_total(Integer seckill_total) {
        this.seckill_total = seckill_total;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
