package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhang
 * @date 2019年04月24日 14:41
 */
public interface UserApi {
    @GetMapping("query")
    User queryUser(@RequestParam("username") String username, @RequestParam("password") String password);
}
