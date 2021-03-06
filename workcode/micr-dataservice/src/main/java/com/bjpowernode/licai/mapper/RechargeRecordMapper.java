package com.bjpowernode.licai.mapper;

import com.bjpowernode.licai.model.RechargeRecord;
import com.bjpowernode.licai.model.mix.UserRechargeInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RechargeRecordMapper {

    //获取用户最近的充值记录
    List<UserRechargeInfo> selectPageRechrageInfo(@Param("uid") Integer uid,
                                                  @Param("offset") Integer offset,
                                                  @Param("rows") Integer rows);

    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);
}