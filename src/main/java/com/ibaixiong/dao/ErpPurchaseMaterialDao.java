package com.ibaixiong.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ibaixiong.entity.ErpPurchaseMaterial;

public interface ErpPurchaseMaterialDao {
    int deleteByPrimaryKey(Long id);

    int insert(ErpPurchaseMaterial record);

    int insertSelective(ErpPurchaseMaterial record);

    ErpPurchaseMaterial selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ErpPurchaseMaterial record);

    int updateByPrimaryKey(ErpPurchaseMaterial record);
    
    List<ErpPurchaseMaterial> getList();
    
    List<ErpPurchaseMaterial> queryPurchaseMaterials(@Param("formatId")Long formatId);
}