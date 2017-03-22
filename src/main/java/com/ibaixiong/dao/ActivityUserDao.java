package com.ibaixiong.dao;

import com.ibaixiong.entity.ActivityUser;

public interface ActivityUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(ActivityUser record);

    int insertSelective(ActivityUser record);

    ActivityUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActivityUser record);

    int updateByPrimaryKey(ActivityUser record);
    
    ActivityUser selectByPhone(String phone);
}