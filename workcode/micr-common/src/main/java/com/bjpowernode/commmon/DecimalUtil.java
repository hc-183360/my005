package com.bjpowernode.commmon;

import java.math.BigDecimal;

public class DecimalUtil {

    // n1 >= n2
    public static boolean ge(BigDecimal n1, BigDecimal n2){
        return n1.compareTo(n2) >=0 ;
    }

    // n1 <= n2
    public static boolean le(BigDecimal n1, BigDecimal n2){
        return n1.compareTo(n2) <=0 ;
    }
}
