package com.ibaixiong.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ibaixiong.entity.DdUser;

public interface DdUserDao {
    int deleteByPrimaryKey(String id);

    int insert(DdUser record);

    int insertSelective(DdUser record);

    DdUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DdUser record);

    int updateByPrimaryKey(DdUser record);
    /**
     * 查询有效的用户
     * @param invalid
     * @return
     */
    List<DdUser> queryInvalidUsers(boolean invalid);
    
    /**
     * 查询部门成员
     * @param departmentId		部门ID
     * @param invalid			是否有效
     * @return
     */
    List<DdUser> queryDdUsersByDepartmentId(@Param("departmentId")String departmentId,@Param("invalid")boolean invalid);

    List<DdUser> queryDbUserByAdminId(String adminId);
    
    List<DdUser> queryAll();
}