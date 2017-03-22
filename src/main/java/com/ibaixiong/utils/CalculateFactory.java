/**
 * ibaixiong.com Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.ibaixiong.utils;

import org.springframework.stereotype.Component;

/**
 * 计算方式
 * @author yaoweiguo
 * @email 280435353@qq.com
 * @date 2016年7月20日
 * @since 1.0.0
 */
@Component
public class CalculateFactory {
	//每个发热膜的面积
	private float eachHeatingArea=1.4f;
	/**
	 * 耐高温墙纸胶(黄)
	 * @author yaoweiguo
	 * @date 2016年7月20日，2016年9月20日 
	 * @param groundArea
	 * @param coefficient
	 * @since V1.0
	 * @return
	 */
	public float getYellowWallpaperGlueArea(float groundArea, float coefficient) {

		return (float) Math.ceil((this.getHotwallPaperArea(groundArea) * coefficient) / 5);
	}
	
	/**
	 * 获取白色耐高温墙纸胶
	 * @author yaoweiguo
	 * @date 2016年9月20日 
	 * @param groundArea
	 * @param coefficient
	 * @since V1.1
	 * @return
	 */
	public float getWhiteWallpaperGlueArea(float wallArea) {

		return (float) Math.ceil(wallArea /5/2);
	}

	/**
	 * 获取一体膜平方面积
	 * @author yaoweiguo
	 * @date 2016年7月20日
	 * @param wallArea
	 * @param groundArea
	 * @param coefficient
	 * @return
	 */
	public float getHotwallPaperArea(float groundArea) {

		if(groundArea<=6){
			return eachHeatingArea;
		}else if(groundArea>6){
			Math.ceil(groundArea);
			return (float) (Math.ceil(groundArea/5)*eachHeatingArea);
		}
		return 0;
	}

	/**
	 * 获取发热墙布用量
	 * @author yaoweiguo
	 * @date 2016年7月20日
	 * @param wallArea
	 * @param coefficient
	 * @return
	 */
	public float getWallPaperArea(float wallArea, float coefficient) {

		return wallArea*coefficient;
	}
	
	/**
	 * 智能温控器个数
	 * @author yaoweiguo
	 * @date 2016年7月20日
	 * @param wallArea
	 * @param groundArea
	 * @param coefficient
	 * @return
	 */
	public float getMicrocontrollersNumber(float groundArea) {

		return (float) Math.ceil(this.getHotwallPaperArea(groundArea)/(5*eachHeatingArea));
	}
	
	/**
	 * 获取T型连接线数量
	 * @param groundArea	地面面积
	 * @return
	 */
	public float getTJonLine(float groundArea){
		
		
		return this.getHotwallPaperArea(groundArea)/eachHeatingArea;
	}
	
	/**
	 * 获取快速链接头数量
	 * @param groundArea	地面面积
	 * @return
	 */
	public float getQuickConnector(float groundArea){
		
		
		return (this.getHotwallPaperArea(groundArea)/eachHeatingArea)*2+6;
	}
	/**
	 * 计算普通的数量
	 * @author yaoweiguo
	 * @date 2016年7月20日
	 * @param wallArea
	 * @param groundArea
	 * @param coefficient
	 * @return
	 */
	public float getOtherMountingsNumber() {

		return 1;
	}
}
