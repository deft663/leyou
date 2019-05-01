package com.leyou.cart.service;

import com.leyou.auth.entity.UserInfo;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.util.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhang
 * @date 2019年04月26日 16:29
 */
@Service
public class CartService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    static final String KEY_PREFIX = "ly:cart:uid:";

    static final Logger logger = LoggerFactory.getLogger(CartService.class);
    /**
     * - 先查询之前的购物车数据
     * - 判断要添加的商品是否存在
         - 存在：则直接修改数量后写回Redis
         - 不存在：新建一条数据，然后写入Redis
     * @param cart 前台提交的购物车数据
     */
    public void add(Cart cart) {
        UserInfo user = LoginInterceptor.getLoginUser();
        // Redis的key
        String key = KEY_PREFIX + user.getId();
        // 获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        // 查询是否存在
        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        Boolean aBoolean = hashOps.hasKey(skuId.toString());
        if(aBoolean){// 存在，获取购物车数据
            String json = hashOps.get(skuId.toString()).toString();
            cart= JsonUtils.parse(json, Cart.class);
            cart.setNum(cart.getNum()+num);
        }else {//不存在 直接存
            cart.setUserId(user.getId());
            // 其它商品信息， 需要查询商品服务
            Sku sku = this.goodsClient.querySkuById(cart.getSkuId());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" :
                    StringUtils.split(sku.getImages(), ",")[0]);
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
        }
        hashOps.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }

    public List<Cart> getCart() {
        UserInfo user = LoginInterceptor.getLoginUser();
        String key=KEY_PREFIX+user.getId();
        BoundHashOperations<String, Object, Object> ops = this.redisTemplate.boundHashOps(key);
        List<Cart> list = ops.entries().entrySet().stream().map(e -> {
            Cart cart = JsonUtils.parse(e.getValue().toString(), Cart.class);
            return cart;
        }).collect(Collectors.toList());
        return list;
    }

    public void updateCartNum(Cart cart) {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        // 获取购物车
        String json = hashOps.get(cart.getSkuId().toString()).toString();
        Cart c = JsonUtils.parse(json, Cart.class);
        c.setNum(cart.getNum());
        // 写入购物车
        hashOps.put(cart.getSkuId().toString(), JsonUtils.serialize(c));
    }

    public void delCart(Long skuId) {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        // 获取购物车
        hashOps.delete(skuId.toString());
    }

    public void mergeCart(List<Cart> carts) {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        // 获取购物车
       carts.forEach(e->{
           Boolean exits = hashOps.hasKey(e.getSkuId().toString());
           if(exits){//redis里面已经有了这个商品 操作数量即可
               String json= hashOps.get(e.getSkuId().toString()).toString();
               Cart cart = JsonUtils.parse(json, Cart.class);
               cart.setNum(cart.getNum()+e.getNum());
               hashOps.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
           }else {
               e.setUserId(user.getId());
               hashOps.put(e.getSkuId().toString(),JsonUtils.serialize(e));
           }
       });
    }
}
