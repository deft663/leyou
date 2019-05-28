package com.leyou.cart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhang
 * @date 2019年05月28日 17:20
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisConfigurationProperties {
    private Integer maxRedirects;
    private Integer timeout;
    private List<String> nodes = new ArrayList<>();

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public Integer getMaxRedirects() {
        return maxRedirects;
    }

    public void setMaxRedirects(Integer maxRedirects) {
        this.maxRedirects = maxRedirects;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
