package com.ibaixiong.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ibaixiong.entity.MerchantProductProfit;

public interface MerchantProductProfitDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MerchantProductProfit record);

    int insertSelective(MerchantProductProfit record);

    MerchantProductProfit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MerchantProductProfit record);

    int updateByPrimaryKey(MerchantProductProfit record);
    
    int deleteByMerchantId(Long merchantId);
    
    List<MerchantProductProfit> queryList(Long merchantId);
    
    MerchantProductProfit getByIds(@Param("merchantId")Long merchantId,@Param("productId")Long productId);
}