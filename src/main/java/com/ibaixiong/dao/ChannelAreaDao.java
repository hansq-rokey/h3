package com.ibaixiong.dao;

import org.apache.ibatis.annotations.Param;

import com.ibaixiong.entity.ChannelArea;

public interface ChannelAreaDao {
    int deleteByPrimaryKey(Long id);

    int insert(ChannelArea record);

    int insertSelective(ChannelArea record);

    ChannelArea selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ChannelArea record);

    int updateByPrimaryKey(ChannelArea record);
    
    /**
     * 根据区来查询是否有负责该区域的城市运营中心ID
     * @author yaoweiguo
     * @date 2016年8月18日
     * @param countyId
     * @return
     */
    ChannelArea getChannelAreaBycountyId(@Param("countyId")Long countyId);
    
    /**
     * 根据市来查询是否有负责该区域的城市运营中心ID
     * @author yaoweiguo
     * @date 2016年8月18日
     * @param cityId
     * @return
     */
    ChannelArea getChannelAreaByCityId(@Param("cityId")Long cityId);
}