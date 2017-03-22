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
import com.ibaixiong.service.OrderMessageService;
import com.ibaixiong.service.SourceOrderMessageService;

@Component
public class SourceOrderMessage implements MessageListener{

	@Resource
	SourceOrderMessageService sourceOrderMessageService;
	
	@Override
	public void onMessage(Message message) {
		try {
			String orderNumber=((TextMessage)message).getText();
			sourceOrderMessageService.sourceOrderMessageHandle(orderNumber);
			System.out.println("queue.sourceOrder接收到消息:"+((TextMessage)message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
