/**
 * ibaixiong.com Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.ibaixiong.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ibaixiong.core.web.SmsUtils;
import com.ibaixiong.dao.DdUserDao;
import com.ibaixiong.dao.DdUserMerchantDao;
import com.ibaixiong.dao.SsssCityMerchantDao;
import com.ibaixiong.dao.UserDao;
import com.ibaixiong.entity.DdUser;
import com.ibaixiong.entity.DdUserMerchant;
import com.ibaixiong.entity.SsssCityMerchant;
import com.ibaixiong.service.MerchantMessageService;
import com.ibaixiong.service.OrderMessageService;
import com.ibaixiong.service.OrderReadyService;
import com.papabear.order.api.OrderService;
import com.papabear.order.entity.MallOrder;
import com.shcm.bean.SendResultBean;

/**
 * @author yaoweiguo
 * @email 280435353@qq.com
 * @date 2016年8月18日
 * @since 1.0.0
 */
@Service
public class MerchantMessageServiceImpl implements MerchantMessageService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private SsssCityMerchantDao ssssCityMerchantDao;
	@Resource
	private DdUserMerchantDao userMerchantDao;
	@Resource
	private DdUserDao ddUserDao;
	@Resource
	private UserDao userDao;
	@Resource
	private SmsUtils smsUtils;
	@Resource
	private OrderService orderService;

	@Override
	public void orderMessageHandle(String orderNumber) {
		MallOrder order = orderService.getMallOrder(orderNumber);
		logger.info("订单编号：{}", orderNumber);
		SsssCityMerchant cityMerchant = ssssCityMerchantDao.getByUserId(order.getUserId());
		String mobile = cityMerchant.getLinkTel();
		logger.info("业务员手机号：{}", mobile);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(date);
		String msg = "您的"+orderNumber+"订单已于"+time+"发货，请注意查收！";
		List<SendResultBean> results = smsUtils.sendOnce(mobile,msg);
		for(SendResultBean result:results){
			System.out.println(
					result.getResult()+","+
					result.getCorpId()+","+
					result.getErrMsg()+","+
					result.getMsgId()+","+
					result.getRemain()+","+result.getTotal());
		}
	}
}

