/**
 * ibaixiong.com Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.ibaixiong.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ibaixiong.dao.ChannelAreaDao;
import com.ibaixiong.dao.MerchantProductProfitDao;
import com.ibaixiong.dao.MerchantProxyAreaDao;
import com.ibaixiong.dao.SsssCityMerchantDao;
import com.ibaixiong.dao.SsssInfoDao;
import com.ibaixiong.dao.SsssOrderDao;
import com.ibaixiong.dao.UserDao;
import com.ibaixiong.entity.ChannelArea;
import com.ibaixiong.entity.MerchantProductProfit;
import com.ibaixiong.entity.MerchantProxyArea;
import com.ibaixiong.entity.SsssCityMerchant;
import com.ibaixiong.entity.SsssOrder;
import com.ibaixiong.service.MallOrderService;
import com.papabear.commons.entity.enumentity.InvalidEnum;
import com.papabear.order.api.OrderService;
import com.papabear.order.entity.MallOrder;
import com.papabear.order.entity.MallOrderItem;
import com.papabear.order.entity.MallOrderRevicerInformation;
import com.papabear.product.api.CategoryQueryService;
import com.papabear.product.api.ProductQueryService;
import com.papabear.product.entity.MallBasicCategoryModelFormat;
import com.papabear.product.entity.MallProduct;

/**
 * @author yaoweiguo
 * @email 280435353@qq.com
 * @date 2016年8月18日
 * @since 1.0.0
 */
@Service
public class OrderServiceImpl implements MallOrderService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private SsssOrderDao ssssOrderDao;
	@Resource
	private SsssCityMerchantDao ssssCityMerchantDao;
	@Resource
	private SsssInfoDao ssssInfoDao;
	@Resource
	private UserDao userDao;
//	@Resource
//	private ChannelAreaDao channelAreaDao;
	@Resource
	private MerchantProxyAreaDao proxyAreaDao;
	// dubbo
	@Resource
	private OrderService orderService;
	@Resource
	private ProductQueryService productQueryService;
	@Resource
	private CategoryQueryService categoryQueryService;
	@Resource
	private MerchantProductProfitDao merchantProductProfitDao;
	/**
	 * 获得订单信息
	 * 
	 * @author yaoweiguo
	 * @date 2016年8月18日
	 * @param orderNumber
	 * @return
	 */
	public MallOrder getMallOrder(String orderNumber) {
		MallOrder order = orderService.getMallOrder(orderNumber);

		return order;
	}

	/**
	 * 订单处理 1、跨区域订单处理
	 * 
	 * @param order
	 * @param user
	 * @param orderNumber
	 */
	public void orderHandle(String orderNumber) {
		MallOrder order = this.getMallOrder(orderNumber);
		if (order == null)
			return;
		Long userId = order.getUserId();
		List<MallOrderItem> orderItems = orderService.queryOrderItems(userId, orderNumber);
		logger.info("订单编号：{}", orderNumber);
		/*
		 * V1.1 最新版本根据张总安排: 1、删除利润计算； 2、删除首批提货款计算，直接放在B端商城下单计算；
		 * 3、保留跨区域利润计算，只适合C端用户，B端用户不考虑；
		 */
		if (order.getType() == null || order.getType().intValue() == 2)
			return;
		logger.info("进入普通用户下的订单");
		// 普通用户下的订单
		/*
		 * 1、根据订单发货地址查找是否有城市运营中心 2、如果有城市运营中心，需要分配利润，没有则不用动订单
		 */
		MallOrderRevicerInformation revicer = orderService.getRevicerByUserIdAndOrderNumber(userId, orderNumber);
		if (revicer == null) {
			return;
		}
		// 省
		String provinceCode = revicer.getProvinceCode();
		//市
		String cityCode = revicer.getCityCode();
		if (provinceCode != null && cityCode !=null) {
			MerchantProxyArea provinceArea = proxyAreaDao.selectAreaByCode(Long.parseLong(provinceCode));
			MerchantProxyArea cityArea = proxyAreaDao.selectAreaByCode(Long.parseLong(cityCode));
			if (provinceArea == null && cityArea == null) {
				return;
			}
			// 跨区域总利润
			float totalAreaMoney = 0f;
			SsssCityMerchant cityMerchant = null;
			if(provinceArea!=null){
				cityMerchant = ssssCityMerchantDao.selectByPrimaryKey(provinceArea.getMerchantId());
			}
			if(cityArea!=null){
				cityMerchant = ssssCityMerchantDao.selectByPrimaryKey(cityArea.getMerchantId());
			}
			if(cityMerchant==null){
				return;
			}
			if(cityMerchant.getLevel()!=1){
				return;
			}
			float areProfit = cityMerchant.getAreaProfit();
			// 区域利润=产品规格区域金额*代理商跨区域系数*数量
			for (MallOrderItem item : orderItems) {
				// 判断该产品是否参与跨区域利润计算
				MallProduct product = productQueryService.getProduct(item.getProductId());
				//根据产品Id和代理商Id判断该代理商是否分配了该产品的利润
				MerchantProductProfit profit = merchantProductProfitDao.getByIds(cityMerchant.getId(), product.getId());
				if(profit!=null){
					if (product.getIsSetAreaProfit() == null || product.getIsSetAreaProfit().intValue() == 0) {
						continue;
					}
					MallBasicCategoryModelFormat format = categoryQueryService.getCategoryModelFormat(item.getProductModelFormatId());
					if (format == null || format.getAreaMoney() == null) {
						continue;
					}
					// 该规格区域总利润
					Float areaMoney = format.getAreaMoney() * areProfit * item.getNum();
					totalAreaMoney += areaMoney;
				}
			}
			// 当区域利润为0，不处理
			if (totalAreaMoney > 0) {
				this.createSsssOrder(totalAreaMoney, null, cityMerchant.getId(), (byte) 4, order, (byte) 3);
				float freezedMoney = cityMerchant.getFreezedMoney();
				cityMerchant.setFreezedMoney(freezedMoney+totalAreaMoney);
				cityMerchant.setUpdateTime(new Date());
				ssssCityMerchantDao.updateByPrimaryKeySelective(cityMerchant);
			}

		}
	}

	/**
	 * 创建渠道商与订单的关联关系及利润的
	 * 
	 * @author yaoweiguo
	 * @date 2016年8月18日
	 * @param profit
	 *            利润（场景服务站利润、代销商利润、固定收益利润、跨地区利润）
	 * @param ssssId
	 *            场景服务站或代销商ID
	 * @param merchantId
	 *            城市运营中心ID
	 * @param orderType
	 *            1：店铺订单 2：邀请码订单 3：下级4S店订单 4:跨地区利润
	 * @param order
	 *            订单
	 * @param source
	 *            //订单来源：1、场景服务站 2、代销商 3、普通用户 4、电商
	 */
	private void createSsssOrder(Float profit, Long ssssId, Long merchantId, byte orderType, MallOrder order, Byte source) {
		SsssOrder ssssOrder = new SsssOrder();
		ssssOrder.setCreateDateTime(new Date());
		// ssssOrder.setStatus(Constant.Status.WAIT.getStatus());
		ssssOrder.setInvalid(InvalidEnum.FALSE.getInvalidValue());
		ssssOrder.setOrderNumber(order.getOrderNumber());
		ssssOrder.setOrderType(orderType);
		ssssOrder.setProfit(profit);
		ssssOrder.setSsssId(ssssId);
		ssssOrder.setMerchantId(merchantId);
		ssssOrder.setSource(source);
		ssssOrder.setOrderNum(order.getTotalNum().floatValue());
		ssssOrder.setOrderTotalMoney(order.getShouldPayMoney());
		// -1:代表订单利润未生效0：代表利润未转入正常余额。1：代表利润转入正常余额
		ssssOrder.setStatus((byte) 0);
		ssssOrderDao.insert(ssssOrder);
	}

}
