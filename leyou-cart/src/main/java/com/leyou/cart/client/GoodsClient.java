package com.leyou.cart.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhang
 * @date 2019年04月26日 16:43
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
