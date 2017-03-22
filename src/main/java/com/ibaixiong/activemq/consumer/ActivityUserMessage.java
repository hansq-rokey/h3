package com.ibaixiong.activemq.consumer;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

import com.ibaixiong.service.ActivityUserMessageService;


@Component
public class ActivityUserMessage implements MessageListener{
	
	@Resource
	private ActivityUserMessageService activityUserMessageService;
	@Override
	public void onMessage(Message message) {
		try {
			String phone=((TextMessage)message).getText();
			activityUserMessageService.activityUserMessageHandle(phone);
			System.out.println("queue.activityUser接收到消息:"+((TextMessage)message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
