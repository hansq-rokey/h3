package com.ibaixiong.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ibaixiong.dao.ErpBomDao;
import com.ibaixiong.dao.ErpPurchaseMaterialDao;
import com.ibaixiong.dao.SsssCityMerchantDao;
import com.ibaixiong.dao.UserDao;
import com.ibaixiong.entity.ErpBom;
import com.ibaixiong.entity.ErpPurchaseMaterial;
import com.ibaixiong.service.ErpBomService;
import com.ibaixiong.utils.CalculateFactory;
import com.papabear.commons.entity.enumentity.Constant.Status;
import com.papabear.commons.entity.enumentity.InvalidEnum;
import com.papabear.commons.redis.RedisObjectUtils;
import com.papabear.order.api.OrderService;
import com.papabear.order.entity.MallOrder;
import com.papabear.order.entity.MallOrderItem;
import com.papabear.order.entity.MallOrderItemExtend;
import com.papabear.product.api.CategoryQueryService;
import com.papabear.product.api.ProductQueryService;
import com.papabear.product.entity.MallBasicCategoryModelFormat;

@Service
public class ErpBomServiceImpl implements ErpBomService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private SsssCityMerchantDao ssssCityMerchantDao;
	@Resource
	private UserDao userDao;
	@Resource
	ErpPurchaseMaterialDao purchaseMaterialDao;
	@Resource
	RedisObjectUtils redisObjectUtils;
	@Resource
	CalculateFactory calculateFactory;
	// dubbo
	@Resource
	private OrderService orderService;
	@Resource
	private ProductQueryService productQueryService;
	@Resource
	private CategoryQueryService categoryQueryService;

	@Resource
	private ErpBomDao erpBomDao;

	@Override
	public List<ErpBom> getListByOrderNumber(String orderNumber) {

		return erpBomDao.getListByOrderNumber(orderNumber);
	}

	/**
	 * TODO，面积不知道，需要修改模型
	 */
	@Override
	public void add(String orderNumber) {
		logger.info("进入订单创建bom清单");
		MallOrder order = orderService.getMallOrder(orderNumber);
		List<MallOrderItem> orderItems = orderService.queryOrderItems(order.getUserId(), orderNumber);
		Map<Long, ErpBom> bomMap = new HashMap<Long, ErpBom>();
		if (order == null || orderItems.size() == 0) {
			return;
		}
		for (MallOrderItem orderItem : orderItems) {
			Long formatId = orderItem.getProductModelFormatId();
			MallBasicCategoryModelFormat format=categoryQueryService.getCategoryModelFormatById(formatId);
			if(format.getIsExtProperties()==null||format.getIsExtProperties().intValue()==0){
				continue;
			}
			MallOrderItemExtend orderItemExtend=orderService.getOrderItemExtend(order.getUserId(), orderItem.getId());
			List<ErpPurchaseMaterial> list = redisObjectUtils.getMaterials(formatId.toString());
			if (list == null || list.size() == 0) {
				list = purchaseMaterialDao.queryPurchaseMaterials(formatId);
				redisObjectUtils.saveOrUpdateMaterialS(formatId.toString(), list);
			}
			if(orderItemExtend==null){
				continue;
			}

			for (ErpPurchaseMaterial pm : list) {
				float num = calculateModel(pm.getCalculateModel().intValue(), orderItem.getNum(), orderItemExtend.getGroundArea(), pm.getCoefficient());
				ErpBom erpBom = bomMap.get(pm.getId());
				if (erpBom == null) {
					ErpBom bom = this.createBom(pm.getModel(), num, orderNumber, pm.getId(), pm.getSerialNumber(), pm.getUnit(),pm.getName());
					bomMap.put(pm.getId(), bom);
				} else {
					erpBom.setNum(erpBom.getNum() + num);
				}
			}
		}
		Iterator<Entry<Long, ErpBom>> it=bomMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, ErpBom> entry=it.next();
			ErpBom bom=entry.getValue();
			erpBomDao.insertSelective(bom);
		}

	}

	ErpBom createBom(String materialModel, Float num, String orderNumber, Long purchaseMaterialId, String serialNumber, String unit,String materialName) {
		ErpBom bom = new ErpBom();
		bom.setBomNumber(null);
		bom.setCreateDateTime(new Date());
		bom.setInvalid(InvalidEnum.FALSE.getInvalidValue());
		bom.setMaterialModel(materialModel);
		bom.setMaterialName(materialName);
		bom.setNum(num);
		bom.setOrderNumber(orderNumber);
		bom.setPurchaseMaterialId(purchaseMaterialId);
		bom.setSerialNumber(serialNumber);
		bom.setStatus(Status.NORMAL.getStatus());
		bom.setUnit(unit);

		return bom;
	}

	public float calculateModel(int model, float wallArea, float groundArea, float coefficient) {

		if (model == 1) {
			return calculateFactory.getWallPaperArea(wallArea, coefficient);
		} else if (model == 2) {
			return calculateFactory.getHotwallPaperArea(groundArea);
		} else if (model == 3) {
			return calculateFactory.getYellowWallpaperGlueArea(groundArea, coefficient);
		} else if (model == 4) {
			return calculateFactory.getWhiteWallpaperGlueArea(wallArea);
		} else if (model == 5) {
			return calculateFactory.getMicrocontrollersNumber(groundArea);
		} else if (model == 6) {
			return calculateFactory.getTJonLine(groundArea);
		}else if (model == 7) {
			return calculateFactory.getQuickConnector(groundArea);
		}else if (model == 8) {
			return calculateFactory.getOtherMountingsNumber();
		}
		return 1f;
	}
}
