package com.leyou.order.config;

import com.leyou.common.util.IdWorker;
import com.leyou.order.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhang
 * @date 2019年04月26日 15:42
 */
@Configuration
@EnableConfigurationProperties({JwtProperties.class,IdWorkerProperties.class})
public class MvcConfig implements WebMvcConfigurer{
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private IdWorkerProperties idWorkerProperties;
    @Bean
    public LoginInterceptor getLoginInterceptor(){
        return new LoginInterceptor(jwtProperties);
    }
    @Bean
    public IdWorker getIdWorker(){
        return  new IdWorker(idWorkerProperties.getWorkerId(),idWorkerProperties.getDatacenterId());
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLoginInterceptor()).addPathPatterns("/order/**");
    }
}
