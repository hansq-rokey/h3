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
import com.ibaixiong.dao.ActivityUserDao;
import com.ibaixiong.dao.DictCodeDao;
import com.ibaixiong.entity.ActivityUser;
import com.ibaixiong.entity.DictCode;
import com.ibaixiong.entity.util.DictTypeEnum;
import com.ibaixiong.service.ActivityUserMessageService;
import com.shcm.bean.SendResultBean;

@Service
public class ActivityUserMessageServiceImpl implements ActivityUserMessageService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private SmsUtils smsUtils;
	@Resource
	private DictCodeDao dictCodeDao;
	@Resource
	private ActivityUserDao activityDao;
	@Override
	public void activityUserMessageHandle(String phone) {
		logger.info("电话号：{}", phone);
		ActivityUser user = activityDao.selectByPhone(phone);
		List<DictCode> codes = dictCodeDao.queryDictCodeByDictType(DictTypeEnum.COD_SENDED_MESSAGE_CONTACTS.getDictType());
		String mobiles = "";
		if(codes!=null && codes.size()>0){
			for(DictCode code : codes){
				mobiles+=code.getDictCodeValue()+",";
			}
		}
		Date date = user.getCreateDateTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(date);
		String msg = "《活动报名》用户:"+user.getName()+",于"+time+"报名,手机号"+user.getPhone()+",请及时跟进！";
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

