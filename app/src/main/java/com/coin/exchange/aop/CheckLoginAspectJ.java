package com.coin.exchange.aop;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.coin.exchange.context.AppApplication;
import com.coin.exchange.cache.BitMexUserCache;
import com.coin.exchange.cache.OkExUserCache;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.view.BindActivity;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/10/16
 * @description
 */

//切面类必须要有此注释
@Aspect
public class CheckLoginAspectJ {

    private String TAG = CheckLoginAspectJ.class.getSimpleName();

    @Pointcut("execution(@com.coin.exchange.aop.CheckLogin * *(..))")
    public void executeCheckLogin() {
        Log.i(TAG, "executeCheckLogin: ");
    }

    @Around("executeCheckLogin()")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckLogin checkLogin = signature.getMethod().getAnnotation(CheckLogin.class);
        if (checkLogin != null) {

            Context context = AppApplication.getContext();

            String type = checkLogin.value();

            if (type.equals(AppUtils.BITMEX) && BitMexUserCache.isEmpty()) {
                Intent intent = new Intent(AppApplication.getContext(), BindActivity.class);
                intent.putExtra(BindActivity.TYPE, AppUtils.BITMEX);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return null;
            } else if (type.equals(AppUtils.OKEX) && OkExUserCache.isEmpty()) {
                Intent intent = new Intent(AppApplication.getContext(), BindActivity.class);
                intent.putExtra(BindActivity.TYPE, AppUtils.OKEX);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return null;
            }

        }

        return joinPoint.proceed();

    }

}
