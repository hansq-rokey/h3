/**
 * ibaixiong.com Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.ibaixiong.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ibaixiong.core.web.SmsUtils;
import com.ibaixiong.dao.DictCodeDao;
import com.ibaixiong.entity.DictCode;
import com.ibaixiong.entity.util.DictTypeEnum;
import com.ibaixiong.service.SourceOrderMessageService;
import com.papabear.order.api.OrderService;
import com.papabear.order.entity.MallOrder;
import com.papabear.order.entity.MallOrderRevicerInformation;
import com.shcm.bean.SendResultBean;

/**
 * @author Administrator
 *
 */
@Service
public class SourceOrderMessageServiceImpl implements SourceOrderMessageService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private SmsUtils smsUtils;
	@Resource
	private OrderService orderService;
	@Resource
	private DictCodeDao dictCodeDao;
	@Override
	public void sourceOrderMessageHandle(String orderNumber) {
		System.out.println(orderNumber);
		logger.info("订单编号：{}", orderNumber);
		MallOrder order = orderService.getMallOrder(orderNumber);
		System.out.println(order);
		MallOrderRevicerInformation revicerInformation = orderService.getRevicerByUserIdAndOrderNumber(order.getUserId(), orderNumber);
		logger.info("收货人手机号：{}", revicerInformation.getMobilePhone());
		
		List<DictCode> codes = dictCodeDao.queryDictCodeByDictType(DictTypeEnum.COD_SENDED_MESSAGE_CONTACTS.getDictType());
		String mobiles = "";
		if(codes!=null && codes.size()>0){
			for(DictCode code : codes){
				mobiles+=code.getDictCodeValue()+",";
			}
		}
		logger.info("货到付款负责人手机号：{}", mobiles);
		Date date = revicerInformation.getCreateDateTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(date);
		String msg = "《货到付款》订单："+orderNumber+"已于"+time+"下单了，收货人电话："+revicerInformation.getMobilePhone()+",请及时跟进！";
		List<SendResultBean> results = smsUtils.sendOnce(mobiles,msg);
		for(SendResultBean result:results){
			System.out.println(
					result.getResult()+","+
					result.getCorpId()+","+
					result.getErrMsg()+","+
					result.getMsgId()+","+
					result.getRemain()+","+
					result.getTotal());
		}
	}
}

