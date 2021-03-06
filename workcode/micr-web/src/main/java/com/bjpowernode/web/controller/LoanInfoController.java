package com.bjpowernode.web.controller;

import com.bjpowernode.commmon.CommonUtil;
import com.bjpowernode.commmon.PageUtil;
import com.bjpowernode.contants.CommonContants;
import com.bjpowernode.licai.model.FinanceAccount;
import com.bjpowernode.licai.model.LoanInfo;
import com.bjpowernode.licai.model.User;
import com.bjpowernode.licai.model.mix.LoanBidInfo;
import com.bjpowernode.licai.service.BidInfoService;
import com.bjpowernode.licai.service.FinanceAccountService;
import com.bjpowernode.licai.service.LoanInfoService;
import com.bjpowernode.vo.PageInfo;
import com.bjpowernode.vo.ResultObject;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoanInfoController {

    @DubboReference(interfaceClass = LoanInfoService.class,version = "1.0")
    private LoanInfoService loanInfoService;

    @DubboReference(interfaceClass = FinanceAccountService.class,version = "1.0")
    private FinanceAccountService financeAccountService;


    @DubboReference(interfaceClass = BidInfoService.class,version = "1.0")
    private BidInfoService bidInfoService;
    /**
     * 查看更多的产品（按类型，分页查看产品）
     */
    @GetMapping("/loan/loan")
    public String loanPageByType(@RequestParam("ptype") Integer productType,
                                 @RequestParam(value = "pageNo",required = false,defaultValue = "1") Integer pageNo,
                                 @RequestParam(value = "pageSize",required = false,defaultValue = "9") Integer pageSize,
                                 Model model){

        String viewName = "loan";
        List<LoanInfo> loanInfoList = new ArrayList<>();
        if(CommonUtil.checkProductType(productType)){
            //产品类型是有效的，可以继续处理其他业务逻辑
            pageNo = PageUtil.defaultPageNo(pageNo);
            pageSize = PageUtil.defaultPageSize(pageSize);
            //数据List
            loanInfoList = loanInfoService.queryPageByType(productType,pageNo,pageSize);
            //总记录数量
            int totalRecords  = loanInfoService.queryRecordNumsPageByType(productType);
            //创建PageInfo
            PageInfo pageInfo = new PageInfo(pageNo,pageSize,totalRecords);
            // @TODO 投资排行榜

            model.addAttribute("page",pageInfo);
            model.addAttribute("productType",productType);
            model.addAttribute("loanInfoList",loanInfoList);
        } else {
            //提示，错误业务
            viewName = "error";
        }
        return viewName;
    }


    /**
     * 产品详情页面
     */
    @GetMapping("/loan/loanInfo")
    public String loanInfo(@RequestParam("loanId") Integer loanId,
                           Model model, HttpSession session){

        if( loanId != null && loanId.intValue() > 0 ){
            //根据产品的id，获取产品信息
            LoanInfo loanInfo = loanInfoService.queryByLoanId(loanId);

            //查询此产品的 投资记录
            List<LoanBidInfo> loanBidInfoList = loanInfoService.queryBidInfoByLoanId(loanId);

            User user  = (User) session.getAttribute(CommonContants.SESSION_USER_KEY);
            if( user != null){
                // 获取账号的资金
                FinanceAccount account = financeAccountService.queryAccount(user.getId());
                if( account != null){
                    model.addAttribute("accountMoney",account.getAvailableMoney());
                }
            }

            //数据放到model
            model.addAttribute("loanInfo",loanInfo);
            model.addAttribute("loanBidInfoList",loanBidInfoList);

            return "loanInfo";
        } else {
            return "error";
        }
    }


    //投资
    @PostMapping("/loan/invest")
    @ResponseBody
    public ResultObject invest(@RequestParam("loanId") Integer loanId,
                               @RequestParam("bidMoney") BigDecimal bidMoney,
                               HttpSession session){
        ResultObject ro  = ResultObject.error("投资失败，请稍后重试");
        if( loanId == null || loanId < 0 ){
            ro.setMessage("产品信息不正确");
        } else if( bidMoney == null && bidMoney.intValue() < 100){
            ro.setMessage("投资金额不正确");
        } else {
            //投资的处理
            User user  = (User) session.getAttribute(CommonContants.SESSION_USER_KEY);

            //投资
            boolean result = bidInfoService.invest(user.getId(),loanId,bidMoney);
        }


        return ro;

    }
}
