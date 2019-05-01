package com.leyou.order.config;

import com.github.wxpay.sdk.PayConfig;
import com.github.wxpay.sdk.WXPay;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhang
 * @date 2019年04月30日 16:04
 */
@Configuration
public class WXPayConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "leyou.pay")
    public PayConfig getConfig(){
        return new PayConfig();
    }
    @Bean
    public WXPay getWXPay(PayConfig payConfig) throws Exception {
        return new WXPay(payConfig,payConfig.getNotifyUrl());
    }
}
