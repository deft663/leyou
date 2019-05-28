package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import java.util.List;

/**
 * @author zhang
 * @date 2019年04月26日 16:20
 */
@Controller
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping
    public ResponseEntity addToCart(@RequestBody Cart cart){
        System.out.println(cart);
        this.cartService.add(cart);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<List<Cart>> getCart(){
        return ResponseEntity.ok(cartService.getCart());
    }

    /**
     * 修改购物车数量
     * @return
     */
    @PutMapping
    public ResponseEntity Cart(Cart cart){
        this.cartService.updateCartNum(cart);

        return ResponseEntity.ok().build();
    }
    @DeleteMapping("{skuId}")
    public ResponseEntity delCart(@PathVariable(name = "skuId") Long skuId){
        this.cartService.delCart(skuId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("merge")
    public ResponseEntity mergeCart(@RequestBody List<Cart> carts){
        this.cartService.mergeCart(carts);
        return ResponseEntity.ok().build();
    }
}
