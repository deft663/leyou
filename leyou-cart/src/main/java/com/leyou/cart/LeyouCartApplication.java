package com.leyou.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author zhang
 * @date 2019年04月26日 08:44
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LeyouCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeyouCartApplication.class, args);
    }
}
