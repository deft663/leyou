package com.leyou.goods.listener;

import com.leyou.goods.service.GoodsHtmlService;
import com.leyou.goods.service.GoodsService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author zhang
 * @date 2019年04月23日 11:05
 */
@Component
public class GoodsListener {
    private Logger logger= LoggerFactory.getLogger(GoodsListener.class);
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.create.web.queue", durable = "true"),
            exchange = @Exchange(
                    value = "leyou.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
    //public void process1(@Payload String message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel)
    public void listenCreate(Long id, Message message, Channel channel) throws Exception {
        if (id == null) {
            return;
        }
        Map<String, Object> map = goodsService.loadModel(id);
        // 创建页面
        goodsHtmlService.createHtml(map);
        //手动ack确保消息被消费了
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        logger.info("手动ack成功");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.delete.web.queue", durable = "true"),
            exchange = @Exchange(
                    value = "leyou.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = "item.delete"))
    public void listenDelete(Long id) {
        if (id == null) {
            return;
        }
        // 创建页面
        goodsHtmlService.deleteHtml(id);
    }
}
