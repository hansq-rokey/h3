/**
 * ibaixiong.com Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.ibaixiong.service.impl;

import java.io.IOException;
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
import com.ibaixiong.service.OrderMessageService;
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
public class OrderMessageServiceImpl implements OrderMessageService {

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
		System.out.println(orderNumber);
		logger.info("订单编号：{}", orderNumber);
		MallOrder order = orderService.getMallOrder(orderNumber);
		SsssCityMerchant cityMerchant = ssssCityMerchantDao.getByUserId(order.getUserId());
		List<DdUserMerchant> lists = userMerchantDao.queryByMerchantId(cityMerchant.getId()); 
		String mobiles = "";
		if(lists!=null && lists.size()>0){
			for(DdUserMerchant userMerchant : lists){
				DdUser ddUser = ddUserDao.selectByPrimaryKey(userMerchant.getUserId());
				mobiles += ddUser.getMobile()+",";
			}
		}
		logger.info("业务员手机号：{}", mobiles);
		String msg = "你的客户（"+cityMerchant.getLinkMan()+"）下单啦！订单号："+orderNumber+"，请立即处理！";
		List<SendResultBean> results = smsUtils.sendOnce(mobiles,msg);
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

