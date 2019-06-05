package com.leyou.item.config;

import com.rabbitmq.client.ConfirmListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.PipedReader;

/**
 * @author zhang
 * @date 2019年06月05日 16:17
 */
@Configuration
public class RabbitConfig {
    private Logger logger= LoggerFactory.getLogger(RabbitConfig.class);
    @Resource
    private RabbitTemplate rabbitTemplate;
    @PostConstruct
    public void initRabbitTemplate() {
        // 设置生产者消息确认
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if(ack){
                    //此处可在缓存里面删除发送成功的数据  失败的可以再次处理
                    logger.info("消息成功发送到exchange:"+correlationData.getId());
                }else{
                    logger.debug("消息id:"+correlationData.getId()+"发送失败，原因"+cause);
                }
            }
        });
    }
    /*@Bean
    public RabbitTemplate rabbitTemplate(){
        *//*rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if(ack){
                    //此处可在缓存里面删除发送成功的数据  失败的可以再次处理
                    logger.info("消息成功发送到exchange:"+correlationData.getId());
                }else{
                    logger.debug("消息id:"+correlationData.getId()+"发送失败，原因"+cause);
                }
            }
        });*//*
        return rabbitTemplate;
    }*/
}
