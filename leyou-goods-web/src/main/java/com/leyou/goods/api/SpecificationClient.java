package com.leyou.goods.api;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhang
 * @date 2019年04月22日 11:28
 */
@FeignClient(value = "item-service")
public interface SpecificationClient extends SpecificationApi {
}
