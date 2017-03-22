package com.ibaixiong.dao;

import java.util.List;

import com.ibaixiong.entity.ErpBom;

public interface ErpBomDao {
	
    int deleteByPrimaryKey(Long id);

    int insert(ErpBom record);

    int insertSelective(ErpBom record);

    ErpBom selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ErpBom record);

    int updateByPrimaryKey(ErpBom record);
    
    List<ErpBom> getListByOrderNumber(String orderNumber);
}