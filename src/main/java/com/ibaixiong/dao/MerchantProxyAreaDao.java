package com.ibaixiong.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ibaixiong.entity.MerchantProxyArea;

public interface MerchantProxyAreaDao {
    int deleteByPrimaryKey(Long id);

    int insert(MerchantProxyArea record);

    int insertSelective(MerchantProxyArea record);

    MerchantProxyArea selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MerchantProxyArea record);

    int updateByPrimaryKey(MerchantProxyArea record);
    
    MerchantProxyArea selectAreaByCode(Long areaCode);
    
    int deleteAreaByCityIdAndCode(@Param("merchantId")Long merchantId,@Param("areaCode")Long areaCode);
    
    List<MerchantProxyArea> queryAreaByMerchantId(Long merchantId);
    
    int deleteAreaByCityId(Long merchantId);
}