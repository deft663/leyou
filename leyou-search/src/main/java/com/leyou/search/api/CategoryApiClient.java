package com.leyou.search.api;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhang
 * @date 2019年04月18日 09:07
 */
@FeignClient("item-service")
public interface CategoryApiClient extends CategoryApi {
}
