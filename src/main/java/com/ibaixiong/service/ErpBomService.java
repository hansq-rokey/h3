package com.ibaixiong.service;

import java.util.List;

import com.ibaixiong.entity.ErpBom;

public interface ErpBomService {

	List<ErpBom> getListByOrderNumber(String orderNumber);
	
	void add(String orderNumber);
}
