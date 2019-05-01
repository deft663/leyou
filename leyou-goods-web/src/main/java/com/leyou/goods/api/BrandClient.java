package com.leyou.goods.api;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhang
 * @date 2019年04月22日 11:26
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi{
}
