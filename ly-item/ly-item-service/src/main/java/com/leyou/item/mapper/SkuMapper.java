package com.leyou.item.mapper;

import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface SkuMapper extends Mapper<Sku>,InsertListMapper<Sku> {
    @Update("update tb_stock set stock=stock-#{num} where sku_id= #{skuId} and stock>=#{num}")
    Long updateStock(@Param("skuId") Long skuId,@Param("num") Integer num);
}
