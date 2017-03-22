package com.ibaixiong.dao;

import java.util.List;

import com.ibaixiong.entity.DdUserMerchant;

public interface DdUserMerchantDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DdUserMerchant record);

    int insertSelective(DdUserMerchant record);

    DdUserMerchant selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DdUserMerchant record);

    int updateByPrimaryKey(DdUserMerchant record);
    
    int deleteByMerchantId(Long merchantId);
    
    List<DdUserMerchant> queryByMerchantId(Long merchantId);
}