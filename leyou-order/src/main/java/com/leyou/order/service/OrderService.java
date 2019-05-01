package com.leyou.order.service;

import com.github.wxpay.sdk.*;
import com.leyou.auth.entity.UserInfo;
import com.leyou.common.util.IdWorker;
import com.leyou.item.dto.CartDto;
import com.leyou.item.pojo.Sku;
import com.leyou.order.api.GoodsApiClient;
import com.leyou.order.dto.OrderDto;
import com.leyou.order.enums.OrderStatusEnum;
import com.leyou.order.interceptor.LoginInterceptor;
import com.leyou.order.mapper.OrderDetailMapper;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.mapper.OrderStatusMapper;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderDetail;
import com.leyou.order.pojo.OrderStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.spel.ast.OpDec;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhang
 * @date 2019年04月28日 16:39
 */
@Service
public class OrderService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private GoodsApiClient goodsApiClient;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private PayConfig config;
    @Autowired
    private WXPay wxPay;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final  String KEY_PREFIX = "ly:cart:uid:";
    private Logger logger= LoggerFactory.getLogger(OrderService.class);
    @Transactional
    public Long addOrder(OrderDto orderDto) {
        UserInfo user = LoginInterceptor.getLoginUser();
        //1.订单生成
        Order order=new Order();
        //1.1生成订单id
        long orderId = idWorker.nextId();
        order.setOrder_id(orderId);
        order.setBuyer_nick(user.getUsername());
        order.setBuyer_rate(false);
        order.setUser_id(user.getId());
        order.setCreate_time(new Date());
        order.setPayment_type(orderDto.getPayType());
        order.setInvoice_type(0);
        order.setPost_fee(0L);
        order.setSource_type(2);
        //收货信息写死
        order.setReceiver("张三");
        order.setReceiver_address("天堂路 3号楼 ");
        order.setReceiver_city("北京市");
        order.setReceiver_district("朝阳区");
        order.setReceiver_mobile("13600000000");
        order.setPost_fee(0L);
        order.setReceiver_zip("10010");
        Map<Long, Integer> map = orderDto.getCarts().stream().collect(Collectors.toMap(CartDto::getSkuId, CartDto::getNum));
        Long totalMoney=0L;
        Iterator<Map.Entry<Long, Integer>> iterator = map.entrySet().iterator();
        List<Sku> skus=new ArrayList<>();
        while (iterator.hasNext()){
            Map.Entry<Long, Integer> e = iterator.next();
            Long skuId = e.getKey();
            Integer num = e.getValue();
            Sku sku=this.goodsApiClient.querySkuById(skuId);
            skus.add(sku);
            Long price = sku.getPrice()*num;
            totalMoney+=price;
            //2.订单详情表
            OrderDetail detail=new OrderDetail();
            detail.setImage(StringUtils.isNotBlank(sku.getImages())?sku.getImages().split(",")[0]:"");
            detail.setNum(num);
            detail.setOrder_id(orderId);
            detail.setOwn_spec(sku.getOwnSpec());
            detail.setPrice(sku.getPrice());
            detail.setSku_id(sku.getId());
            detail.setTitle(sku.getTitle());
            orderDetailMapper.insert(detail);
        }
        order.setTotal_pay(totalMoney);
        order.setActual_pay(totalMoney+order.getPost_fee());
        orderMapper.insert(order);
        //3.订单状态表
        OrderStatus orderStatus=new OrderStatus();
        orderStatus.setOrder_id(orderId);
        orderStatus.setStatus(OrderStatusEnum.UN_PAY.getCode());
        orderStatus.setCreate_time(new Date());
        orderStatusMapper.insert(orderStatus);
        //4.减库存
        this.goodsApiClient.reduceStock(orderDto.getCarts());
        //5.清掉redis里面对应的商品
        String key=KEY_PREFIX+user.getId();
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);
        skus.stream().forEach(sku->{
            ops.delete(sku.getId().toString());
           /* if(ops.hasKey(sku.getId().toString())){
            }*/
        });
        return orderId;
    }

    public Order queryOrder(Long id) {
        Order order = this.orderMapper.selectByPrimaryKey(id);
        OrderStatus orderStatus = this.orderStatusMapper.selectByPrimaryKey(id);
        order.setOrderStatus(orderStatus);
        OrderDetail detail=new OrderDetail();
        detail.setOrder_id(id);
        List<OrderDetail> details = this.orderDetailMapper.select(detail);
        order.setOrderDetails(details);
        return order;
    }

    public String generateOrderUrl(Long id) {
        Order order= this.queryOrder(id);
        Map mapData=new HashMap();
        mapData.put("body",order.getOrderDetails().get(0).getTitle().substring(0,10));
        mapData.put("out_trade_no",""+order.getOrder_id());
        mapData.put("total_fee","1"/*order.getActual_pay()*/);
        mapData.put("spbill_create_ip","127.0.0.1");
        mapData.put("trade_type","NATIVE");
        try {
            Map resultMap = wxPay.unifiedOrder(mapData);
            if("SUCCESS".equals(resultMap.get("return_code"))&&"SUCCESS".equals(resultMap.get("result_code"))){
                //此字段是通信标识return_code
                //此字段是交易标识result_code
                return resultMap.get("code_url").toString();
            }
        } catch (Exception e) {
            logger.error("下单失败了");
            e.printStackTrace();
        }
        return null;
    }
    @Transactional
    public Map<String,String> payResultHandler(Map<String, String> data) {
        Map map=new HashMap();
        map.put("return_code", "SUCCESS");
        map.put("return_msg", "OK");
        if("SUCCESS".equals(data.get("return_code"))&&"SUCCESS".equals(data.get("result_code"))){
            //业务结果
            //1.商户系统对于支付结果通知的内容一定要做签名验证,
            SignIsValid(data);
            //2. 并校验返回的订单金额是否与商户侧的订单金额一致
            String total_fee = data.get("total_fee");
           if( StringUtils.isEmpty(total_fee)){
               throw new RuntimeException("订单金额异常");
           }
            Long money = Long.valueOf(total_fee);
            String out_trade_no = data.get("out_trade_no");
            Order order = this.queryOrder(Long.valueOf(out_trade_no));
            if(order.getOrderStatus().getStatus()==OrderStatusEnum.PAYED.getCode()){
                logger.info("此订单已经支付过了");
                return data;
            }
            if(/*order.getActual_pay()*/1!=money){
                throw new RuntimeException("订单金额异常");
            }
            //修改订单状态
            OrderStatus status= order.getOrderStatus();
            status.setStatus(OrderStatusEnum.PAYED.getCode());
            status.setPayment_time(new Date());
            orderStatusMapper.updateByPrimaryKey(status);
        }else{
            throw new RuntimeException("订单支付失败");
        }
        return map;
    }

    private void SignIsValid(Map<String, String> return_data) {
        try {
            String sign1 = WXPayUtil.generateSignature(return_data, this.config.getKey(), WXPayConstants.SignType.MD5);
            String sign2 = WXPayUtil.generateSignature(return_data, this.config.getKey(), WXPayConstants.SignType.HMACSHA256);
            String sign = return_data.get("sign");
            if(!StringUtils.equals(sign1,sign)&&!StringUtils.equals(sign2,sign)){
                throw new RuntimeException("订单签名异常");
            }
        } catch (Exception e) {
            logger.error("订单签名异常");
            e.printStackTrace();
            throw new RuntimeException("订单签名异常");
        }

    }

    public Integer queryOrderStatus(Long id) {
        Map queryMap=new HashMap();
        queryMap.put("out_trade_no",""+id);
        try {
            Map map = wxPay.orderQuery(queryMap);
            if("SUCCESS".equals(map.get("return_code"))&&"SUCCESS".equals(map.get("result_code"))){
                if("SUCCESS".equals(map.get("trade_state"))){//SUCCESS—支付成功
                    return 1;
                }else{
                    return 2;
                }
            }
        } catch (Exception e) {
           logger.info("订单查询失败、、、");
           return 2;
        }
        return 2;
    }
}
