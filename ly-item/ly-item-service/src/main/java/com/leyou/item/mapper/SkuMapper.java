package com.leyou.item.mapper;

import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Stock;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface SkuMapper extends Mapper<Sku>,InsertListMapper<Stock> {
}
