package com.leyou.order.mapper;

import com.leyou.order.pojo.Order;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface OrderMapper extends Mapper<Order> ,SelectByIdListMapper<Order,Long>,InsertListMapper<Order>{
}
