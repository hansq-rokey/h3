/**
 * ibaixiong.com Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.ibaixiong.activemq.consumer;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

import com.ibaixiong.service.MallOrderService;

/**
 * 订单跨区域利润处理
 * @author yaoweiguo
 * @email  280435353@qq.com
 * @date   2016年8月18日
 * @since  1.0.0
 */
@Component
public class MallOrderReceiver implements MessageListener{

	@Resource
	MallOrderService mallOrderService;
	
	@Override
	public void onMessage(Message message) {
		try {
			String orderNumber=((TextMessage)message).getText();
			mallOrderService.orderHandle(orderNumber);
			System.out.println("queue.order接收到消息:"+((TextMessage)message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
