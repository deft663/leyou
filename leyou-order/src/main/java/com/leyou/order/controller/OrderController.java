package com.leyou.order.controller;

import com.github.wxpay.sdk.WXPay;
import com.leyou.order.dto.OrderDto;
import com.leyou.order.pojo.Order;
import com.leyou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhang
 * @date 2019年04月28日 16:39
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> addOrder(@RequestBody OrderDto orderDto){
        Long orderId = this.orderService.addOrder(orderDto);
        return ResponseEntity.ok(orderId);
    }
    @GetMapping("{id}")
    public ResponseEntity<Order> queryOrder(@PathVariable("id")Long id){
        Order order= this.orderService.queryOrder(id);
        return ResponseEntity.ok(order);
    }
    @GetMapping("url/{id}")
    public ResponseEntity<String> generateUrl(@PathVariable("id")Long id){
        String url=this.orderService.generateOrderUrl(id);
        System.out.println("生成了付款二维码的地址------>"+url);
        return ResponseEntity.ok(url);
    }
    @GetMapping("state/{id}")
    public ResponseEntity<Integer> queryOrderStatus(@PathVariable("id") Long id){
        Integer status=this.orderService.queryOrderStatus(id);
        return ResponseEntity.ok(status);
    }
}
