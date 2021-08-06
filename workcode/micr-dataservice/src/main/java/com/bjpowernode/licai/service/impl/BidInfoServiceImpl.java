package com.bjpowernode.licai.service.impl;

import com.bjpowernode.commmon.CommonUtil;
import com.bjpowernode.commmon.DecimalUtil;
import com.bjpowernode.commmon.PageUtil;
import com.bjpowernode.licai.mapper.BidInfoMapper;
import com.bjpowernode.licai.mapper.FinanceAccountMapper;
import com.bjpowernode.licai.model.FinanceAccount;
import com.bjpowernode.licai.model.mix.UserBidInfo;
import com.bjpowernode.licai.service.BidInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = BidInfoService.class,version = "1.0")
public class BidInfoServiceImpl implements BidInfoService {

    @Resource
    private BidInfoMapper bidInfoMapper;

    @Resource
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public BigDecimal querySumBidMoney() {
        BigDecimal sumBidMoney = bidInfoMapper.selectSumBidMoney();
        return sumBidMoney;
    }

    @Override
    public List<UserBidInfo> queryRecentlyUserBidInfo(Integer uid,
                                                      Integer pageNo,
                                                      Integer pageSize) {

        List<UserBidInfo> list = new ArrayList<>();
        if( uid != null && uid > 0){
            pageNo = PageUtil.defaultPageNo(pageNo);
            pageSize = PageUtil.defaultPageSize(pageSize);

            int offset = (pageNo  - 1) * pageSize;
            list = bidInfoMapper.selectPageUserBidInfo(uid,offset,pageSize);

        }
        return list;
    }

    /**
     * 投资
     * @param uid  用户id
     * @param loanId 产品id
     * @param bidMoney 投资金额
     * @return true：投资成功， false 投资失败
     */
    @Transactional
    @Override
    public boolean invest(Integer uid, Integer loanId, BigDecimal bidMoney) {
        int rows = 0;
        boolean  result  = false;
        //1.检查用户的金额
        FinanceAccount account = financeAccountMapper.selectUidForUpdate(uid);
        if( account != null){
            if(DecimalUtil.le( bidMoney, account.getAvailableMoney())){
                //资金充足， 扣除金额
                rows = financeAccountMapper.updateAvaiableMoneyInvest(uid,bidMoney);
                if( rows < 1){
                    throw new RuntimeException("投资-扣除用户资金失败");
                }

                //处理产品
            }
        }





        return result;
    }
}
