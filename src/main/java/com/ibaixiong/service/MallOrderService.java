/**
 * ibaixiong.com Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.ibaixiong.service;


/**
 * 
 * @author yaoweiguo
 * @email  280435353@qq.com
 * @date   2016年8月17日
 * @since  1.0.0
 */
public interface MallOrderService {
	
	/**
	 * 订单处理
	 *  1、利润计算 2、首批提货款计算：3、跨区域订单处理
	 * @author yaoweiguo
	 * @date 2016年8月18日
	 * @param orderNumber
	 */
	void orderHandle(String orderNumber);
	
	
}
