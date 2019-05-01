package com.leyou.order.api;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface GoodsApiClient extends GoodsApi {
}
