package com.leyou.cart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhang
 * @date 2019年05月28日 17:22
 */
@Configuration
public class RedisConfig {
    @Autowired
    private RedisConfigurationProperties redisConfigurationProperties;

    @Bean
    public JedisCluster jedisCluster() {
        Set<HostAndPort> nodeSet = new HashSet<>();
        for(String node :redisConfigurationProperties.getNodes()) {
            String[] split = node.split(":");
            nodeSet.add(new HostAndPort(split[0],Integer.valueOf(split[1])));
        }
        return new JedisCluster(nodeSet,redisConfigurationProperties.getTimeout(),redisConfigurationProperties.getMaxRedirects());
    }
}
