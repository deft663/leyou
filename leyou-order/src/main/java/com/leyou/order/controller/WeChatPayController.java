package com.leyou.order.controller;

import com.github.wxpay.sdk.WXPayUtil;
import com.leyou.order.service.OrderService;
import org.apache.http.client.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhang
 * @date 2019年05月01日 10:39
 */
@RestController
public class WeChatPayController {
    @Autowired
    private OrderService orderService;
    @PostMapping("WeChatPayNotify")
    public String weChatPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletInputStream inputStream = request.getInputStream();
        StringBuffer sb = new StringBuffer();
        int len = -1;
        byte[] buffer = new byte[1024];

        while((len = inputStream.read(buffer)) != -1){
            sb.append(new String(buffer,0,len));
        }
        inputStream.close();
        Map<String, String> map = WXPayUtil.xmlToMap(sb.toString());
        System.out.print("回调信息---->   ");
        map.entrySet().stream().forEach(e->{
            System.out.print(e.getKey()+"--"+e.getValue()+" ");
        });
        Map<String,String> return_data = orderService.payResultHandler(map);

        return WXPayUtil.mapToXml(return_data);
    }
}
