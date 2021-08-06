package com.bjpowernode.licai.service.impl;

import com.bjpowernode.commmon.PageUtil;
import com.bjpowernode.licai.mapper.RechargeRecordMapper;
import com.bjpowernode.licai.model.mix.UserRechargeInfo;
import com.bjpowernode.licai.service.RechargeService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = RechargeService.class,version = "1.0")
public class RechargeServiceImpl implements RechargeService {


    @Resource
    private RechargeRecordMapper rechargeRecordMapper;

    //获取用户最近的充值记录
    @Override
    public List<UserRechargeInfo> queryRecentlyUserRechargeInfo(Integer uid,
                                                                Integer pageNo,
                                                                Integer pageSize) {
        List<UserRechargeInfo> list = new ArrayList<>();
        if( uid != null && uid > 0){
            pageNo = PageUtil.defaultPageNo(pageNo);
            pageSize = PageUtil.defaultPageSize(pageSize);
            int offset = (pageNo - 1 ) * pageSize;
            list = rechargeRecordMapper.selectPageRechrageInfo(uid,offset,pageSize);
        }
        return list;
    }
}
