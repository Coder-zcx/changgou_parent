package com.changgou.pay.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.pay.service.WxPayService;
import com.changgou.util.ConvertUtils;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Coder-zcx
 * @Date: 2019/12/6 21:40
 */
@RestController
@RequestMapping("/wxpay")
public class WxPayController {

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @GetMapping("/nativePay")
    public Result nativePay(@RequestParam("orderId") String orderId, @RequestParam("money") Integer money) {
        Map map = wxPayService.nativePay(orderId, money);
        return new Result(true, StatusCode.OK, "", map);
    }


    @RequestMapping("/notify")
    public void notifyLogic(HttpServletResponse response, HttpServletRequest request) {
        System.out.println("支付成功回调。。。。");
        try {
            //输入流转换为xml字符串
            String xml = ConvertUtils.convertToString(request.getInputStream());
            System.out.println(xml);

            //给微信支付一个成功的响应
            //解析
            Map<String, String> map = WXPayUtil.xmlToMap(xml);
            //查询订单
            if ("SUCCESS".equals(map.get("result_code"))) {  //如果返回的结果是成功
                Map result = wxPayService.queryOrder(map.get("out_trade_no"));
                System.out.println("查询订单返回结果：" + result);

                //如果查询结果是成功发送到mq
                if ("SUCCESS".equals(result.get("result_code"))) {
                    Map m = new HashMap();
                    m.put("orderId", result.get("out_trade_no"));
                    m.put("transactionId", result.get("transaction_id"));
                    rabbitTemplate.convertAndSend("", "order_pay", JSON.toJSONString(m));
                    rabbitTemplate.convertAndSend("paynotify","",map.get("out_trade_no"));
                    //如果成功，给微信支付一个成功的响应
                    response.setContentType("text/xml");
                    String data = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
                    response.getWriter().write(data);
                }
            } else {
                System.out.println(map.get("err_code_des"));//错误信息描述
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
