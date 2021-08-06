package com.bjpowernode.licai.service;

import com.bjpowernode.licai.model.mix.UserRechargeInfo;

import java.util.List;

//充值的服务
public interface RechargeService {

    //获取用户最近的充值记录
    List<UserRechargeInfo> queryRecentlyUserRechargeInfo(Integer uid,
                                                         Integer pageNo,
                                                         Integer pageSize);
}
