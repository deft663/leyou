package com.leyou.cart.config;

import com.leyou.cart.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.rmi.registry.Registry;

/**
 * @author zhang
 * @date 2019年04月26日 15:42
 */
@Configuration
@EnableConfigurationProperties({JwtProperties.class})
public class MvcConfig implements WebMvcConfigurer{
    @Autowired
    private JwtProperties jwtProperties;
    @Bean
    public LoginInterceptor getLoginInterceptor(){
        return new LoginInterceptor(jwtProperties);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLoginInterceptor()).addPathPatterns("/**");
    }
}
