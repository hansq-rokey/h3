package com.ibaixiong.dao;

import org.apache.ibatis.annotations.Param;

import com.ibaixiong.entity.SsssInfo;

public interface SsssInfoDao {
	int deleteByPrimaryKey(Long id);

	int insert(SsssInfo record);

	int insertSelective(SsssInfo record);

	SsssInfo selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(SsssInfo record);

	int updateByPrimaryKey(SsssInfo record);

	SsssInfo getByUserId(Long userId);

	int reduceMoney(@Param("ssssId") Long ssssId, @Param("money") Float money);
}