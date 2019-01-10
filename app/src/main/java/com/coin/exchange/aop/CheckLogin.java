package com.coin.exchange.aop;

import com.coin.exchange.adapter.MarketAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/10/16
 * @description 检测是否已经登陆
 */

//用于方法
@Target(ElementType.METHOD)
//运行时
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLogin {

    String value();

}
