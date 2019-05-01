package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhang
 * @date 2019年04月24日 14:41
 */
@FeignClient(value = "user-service")
public interface UserClient extends UserApi {
}
