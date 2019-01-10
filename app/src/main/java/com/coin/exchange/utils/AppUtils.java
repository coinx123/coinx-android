package com.coin.exchange.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.anthonycr.bonsai.Schedulers;
import com.anthonycr.bonsai.SingleOnSubscribe;
import com.coin.exchange.R;
import com.coin.exchange.cache.CacheHelper;
import com.coin.exchange.cache.PreferenceManager;
import com.coin.exchange.context.AppApplication;
import com.coin.exchange.database.CollectionItem;
import com.coin.exchange.database.CollectionModel;
import com.coin.libbase.utils.UIUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/13
 * @description app中杂七杂八的工具
 */
public class AppUtils {

    public static final String PLATFORM = "Platform";

    public static final String BITMEX = "BITMEX";
    public static final String OKEX = "OKEX";

    public static final Map<String, Integer> ICON_MAP = new HashMap<>();

    static {
        ICON_MAP.put("BTC", R.drawable.btc_coin);
        ICON_MAP.put("XBT", R.drawable.btc_coin);

        ICON_MAP.put("ETH", R.drawable.eth_icon);
        ICON_MAP.put("LTC", R.drawable.ltc_coin);
        ICON_MAP.put("ETC", R.drawable.etc_coin);
        ICON_MAP.put("BCH", R.drawable.bch_coin);
        ICON_MAP.put("XRP", R.drawable.xrp_coin);
        ICON_MAP.put("EOS", R.drawable.eos_coin);
        ICON_MAP.put("BTG", R.drawable.btg_coin);
        ICON_MAP.put("TRX", R.drawable.trx_coin);
        ICON_MAP.put("ADA", R.drawable.ada_coin);
        ICON_MAP.put("BSV", R.drawable.bsv_icon);
    }

    /**
     * 获取 亮色 的增长颜色
     *
     * @return
     */
    public static int getLightIncreaseColor() {
        boolean isRed = CacheHelper
                .getInstance()
                .getBooleanCache(PreferenceManager.INCREASE_COLOR_IS_RED);
        return isRed ?
                ContextCompat.getColor(AppApplication.getContext(), R.color.red) :
                ContextCompat.getColor(AppApplication.getContext(), R.color.green);
    }

    /**
     * 获取 亮色 的减少的颜色
     *
     * @return
     */
    public static int getLightDecreaseColor() {
        boolean isRed = CacheHelper
                .getInstance()
                .getBooleanCache(PreferenceManager.INCREASE_COLOR_IS_RED);
        return isRed ?
                ContextCompat.getColor(AppApplication.getContext(), R.color.green) :
                ContextCompat.getColor(AppApplication.getContext(), R.color.red);
    }

    /**
     * 获取 正常的 增长颜色
     *
     * @return
     */
    public static int getIncreaseColor() {
        boolean isRed = CacheHelper.getInstance().getBooleanCache(PreferenceManager.INCREASE_COLOR_IS_RED);
        return isRed ?
                ContextCompat.getColor(AppApplication.getContext(), R.color.main_red) :
                ContextCompat.getColor(AppApplication.getContext(), R.color.main_green);
    }

    /**
     * 获取 正常的 减少的颜色
     *
     * @return
     */
    public static int getDecreaseColor() {
        boolean isRed = CacheHelper.getInstance().getBooleanCache(PreferenceManager.INCREASE_COLOR_IS_RED);
        return isRed ?
                ContextCompat.getColor(AppApplication.getContext(), R.color.main_green) :
                ContextCompat.getColor(AppApplication.getContext(), R.color.main_red);
    }

    /**
     * 获取向下背景
     *
     * @return
     */
    public static int getDownBg() {
        boolean isRed = CacheHelper.getInstance().getBooleanCache(PreferenceManager.INCREASE_COLOR_IS_RED);
        return isRed ? R.drawable.kline_down_green : R.drawable.kline_down;
    }

    /**
     * 获取向下背景
     *
     * @return
     */
    public static int getUpBg() {
        boolean isRed = CacheHelper.getInstance().getBooleanCache(PreferenceManager.INCREASE_COLOR_IS_RED);
        return isRed ? R.drawable.kline_up_red : R.drawable.kline_up;
    }

    /**
     * 获取 正常的 增加的Drawable 2dp圆角
     *
     * @return
     */
    public static Drawable getIncreaseBg() {
        boolean isRed = CacheHelper.getInstance().getBooleanCache(PreferenceManager.INCREASE_COLOR_IS_RED);
        return isRed ?
                ContextCompat.getDrawable(AppApplication.getContext(), R.drawable.shape_red_round_bg) :
                ContextCompat.getDrawable(AppApplication.getContext(), R.drawable.shape_green_round_bg);
    }

    /**
     * 获取 正常的 减少的Drawable 2dp圆角
     *
     * @return
     */
    public static Drawable getDecreaseBg() {
        boolean isRed = CacheHelper.getInstance().getBooleanCache(PreferenceManager.INCREASE_COLOR_IS_RED);
        return isRed ?
                ContextCompat.getDrawable(AppApplication.getContext(), R.drawable.shape_green_round_bg) :
                ContextCompat.getDrawable(AppApplication.getContext(), R.drawable.shape_red_round_bg);
    }

    public static int dpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density + 0.5f);
    }

    /**
     * 通过 进程id：pid 获取 进程名称
     *
     * @param context
     * @param pid
     * @return
     */
    public static String getAppNameByPID(Context context, int pid) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (manager != null) {

            for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
                if (processInfo.pid == pid) {
                    return processInfo.processName;
                }
            }

        }
        return "";
    }

    /**
     * 判断进程是否运行
     *
     * @param context
     * @param proessName
     * @return
     */
    public static boolean isProcessRunning(Context context, String proessName) {
        boolean isRunning = false;
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

            List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo info : lists) {
                if (info.processName.equals(proessName)) {
                    isRunning = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isRunning;
    }

    /**
     * 判断该自选是否已经存在,并且处理
     *
     * @param collectionModel
     * @param instrumentId
     * @param view
     * @param collectionItem
     */
    public static void isExitAndDelOrAdd(final CollectionModel collectionModel, String instrumentId,
                                         final ImageView view, final CollectionItem collectionItem) {
        collectionModel.isCollection(instrumentId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.main())
                .subscribe(new SingleOnSubscribe<Boolean>() {
                    @Override
                    public void onItem(@Nullable Boolean item) {
                        super.onItem(item);
                        if (collectionItem == null) { //单纯判断是否存在
                            if (item) {
                                view.setBackgroundResource(R.drawable.collection_ok);
                            } else {
                                view.setBackgroundResource(R.drawable.collection);
                            }
                        } else { //对后续的处理
                            if (item) {
                                collectionModel.deleteCollection(collectionItem)
                                        .subscribeOn(com.anthonycr.bonsai.Schedulers.io())
                                        .observeOn(com.anthonycr.bonsai.Schedulers.main())
                                        .subscribe(new SingleOnSubscribe<Boolean>() {
                                            @Override
                                            public void onItem(@Nullable Boolean item) {
                                                super.onItem(item);
                                                view.setBackgroundResource(R.drawable.collection);
                                            }
                                        });
                            } else {
                                collectionModel.addCollectionIfNotExists(collectionItem)
                                        .subscribeOn(com.anthonycr.bonsai.Schedulers.io())
                                        .observeOn(com.anthonycr.bonsai.Schedulers.main())
                                        .subscribe(new SingleOnSubscribe<Boolean>() {
                                            @Override
                                            public void onItem(@Nullable Boolean item) {
                                                super.onItem(item);
                                                view.setBackgroundResource(R.drawable.collection_ok);
                                            }
                                        });
                            }
                        }

                    }
                });
    }
    /**
     * 获取手机状态栏的高度
     *
     * @return 状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int x, statusBarHeight = UIUtils.dip2px(context, 20);
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

}
