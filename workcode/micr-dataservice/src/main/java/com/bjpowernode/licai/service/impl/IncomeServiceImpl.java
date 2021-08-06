package com.bjpowernode.licai.service.impl;

import com.bjpowernode.commmon.PageUtil;
import com.bjpowernode.licai.mapper.IncomeRecordMapper;
import com.bjpowernode.licai.model.mix.UserIncomeInfo;
import com.bjpowernode.licai.service.IncomeService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = IncomeService.class,version = "1.0")
public class IncomeServiceImpl implements IncomeService {

    @Resource
    private IncomeRecordMapper recordMapper;

    //某个用户最近的收益记录
    @Override
    public List<UserIncomeInfo> queryRecentlyUserIncome(Integer uid, Integer pageNo, Integer pageSize) {
        List<UserIncomeInfo> list = new ArrayList<>();
        if( uid != null && uid > 0 ){
            pageNo = PageUtil.defaultPageNo(pageNo);
            pageSize = PageUtil.defaultPageSize(pageSize);
            int offset = (pageNo - 1 ) * pageSize;
            list = recordMapper.selectRecentlyInfo(uid,offset,pageSize);
        }
        return list;
    }
}
