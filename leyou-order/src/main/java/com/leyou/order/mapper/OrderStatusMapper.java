package com.leyou.order.mapper;

import com.leyou.order.pojo.OrderStatus;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface OrderStatusMapper extends Mapper<OrderStatus>,InsertListMapper<OrderStatus> {
}
