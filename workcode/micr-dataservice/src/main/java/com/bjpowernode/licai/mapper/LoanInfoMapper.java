package com.bjpowernode.licai.mapper;

import com.bjpowernode.licai.model.LoanInfo;
import com.bjpowernode.licai.model.mix.LoanBidInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface LoanInfoMapper {
    //历史的平台rate
    BigDecimal selectAvgRate();

    //按类型分页查询产品
    List<LoanInfo> selectPageByType(@Param("productType") Integer productType,
                                    @Param("offSet") Integer offSet,
                                    @Param("rows") Integer rows);


    //某产品类型的，总记录数量
    int selectCountRecordByType(@Param("productType") Integer productType);

    //某产品的投资记录
    List<LoanBidInfo> selectBidInfoByLoanId(@Param("loanId") Integer loanId);

    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);


}