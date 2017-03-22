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

import com.ibaixiong.service.ErpBomService;

/**
 * 订单清单处理
 * @author yaoweiguo
 * @email  280435353@qq.com
 * @date   2016年8月18日
 * @since  1.0.0
 */
@Component
public class MallOrderBom implements MessageListener{

	@Resource
 ErpBomService erpBomService;
	
	@Override
	public void onMessage(Message message) {
		try {
			String orderNumber=((TextMessage)message).getText();
			erpBomService.add(orderNumber);
			System.out.println("queue.bom接收到消息:"+((TextMessage)message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
