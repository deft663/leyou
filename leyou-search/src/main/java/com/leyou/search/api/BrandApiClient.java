package com.leyou.search.api;

import com.leyou.item.api.BrandApi;
import com.leyou.item.pojo.Brand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author zhang
 * @date 2019年04月18日 09:21
 */
@FeignClient(name = "item-service")
public interface BrandApiClient extends BrandApi {
}
