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
import com.ibaixiong.service.MerchantMessageService;
import com.ibaixiong.service.OrderMessageService;

@Component
public class MerchantMessage implements MessageListener{

	@Resource
	MerchantMessageService merchantMessageService;
	
	@Override
	public void onMessage(Message message) {
		try {
			String orderNumber=((TextMessage)message).getText();
			merchantMessageService.orderMessageHandle(orderNumber);
			System.out.println("queue.merchant接收到消息:"+((TextMessage)message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
