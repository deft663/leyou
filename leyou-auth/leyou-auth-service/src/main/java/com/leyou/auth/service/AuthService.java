package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import com.leyou.common.util.CookieUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author zhang
 * @date 2019年04月24日 14:20
 */
@Service
public class AuthService {
    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties jwtProperties;
    public String authentication(String username, String password) {
       User user = this.userClient.queryUser(username, password);
       if(user==null) {
           return null;
       }
        try {
            // 如果有查询结果，则生成token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            return token;
       } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
