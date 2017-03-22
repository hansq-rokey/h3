package com.ibaixiong.dao;

import com.ibaixiong.entity.SsssCityMerchant;

public interface SsssCityMerchantDao {
    int deleteByPrimaryKey(Long id);

    int insert(SsssCityMerchant record);

    int insertSelective(SsssCityMerchant record);

    SsssCityMerchant selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SsssCityMerchant record);

    int updateByPrimaryKey(SsssCityMerchant record);
    
    SsssCityMerchant getByUserId(Long userId);
}