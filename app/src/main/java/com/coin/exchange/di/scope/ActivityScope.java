package com.coin.exchange.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/15
 * @description activity的scope
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}