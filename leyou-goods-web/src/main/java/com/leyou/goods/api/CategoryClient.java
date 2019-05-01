package com.leyou.goods.api;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhang
 * @date 2019年04月22日 11:27
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
