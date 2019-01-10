package com.coin.exchange.cache;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/7
 * @description 缓存工具类
 */

public class CacheHelper {

    private SharedPreferences cacheData;

    private final String LIMIT = "_limit";
    private final String DATA = "_data";

    // 1天
    public static final int DAY_TIMES = 60 * 60 * 24 * 1000;

    private static CacheHelper mInstance;
    private static Context appContext;

    /**
     * 在 Application 中需要进行初始化
     *
     * @param context 建议为 {@link android.app.Application}
     */
    public static void init(Context context) {
        appContext = context;
    }

    /**
     * 获取 单例对象
     *
     * @return
     */
    public static CacheHelper getInstance() {
        if (mInstance == null) {
            synchronized (CacheHelper.class) {
                if (mInstance == null) {
                    mInstance = new CacheHelper();
                }
            }
        }
        return mInstance;
    }

    private CacheHelper() {
        try {
            if (appContext == null) {
                throw new Exception("CacheHelper not init in Application");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cacheData == null) {
            cacheData = appContext.getSharedPreferences(getSpName(), Context.MODE_PRIVATE);
        }
    }

    /**
     * SharedPreferences存储在sd卡中的文件名字
     */
    private static String getSpName() {
        return appContext.getPackageName() + "_preferences";
    }

    /**
     * 获取 key值 对应的 内容
     *
     * @param key 键
     * @return 如果有值，且在时间内，则返回对应的内容；否则返回null
     */
    public String getCache(String key) {
        long deadline = cacheData.getLong(key + LIMIT, -1);
        if (deadline <= 0) {
            return cacheData.getString(key + DATA, null);
        } else {
            if (deadline < System.currentTimeMillis()) {
                return null;
            } else {
                return cacheData.getString(key + DATA, null);
            }
        }
    }

    /**
     * 获取 key值 对应的 内容
     *
     * @param key 键
     * @return 如果有值，且在时间内，则返回对应的内容；否则返回null
     */
    public boolean getBooleanCache(String key) {
        long deadline = cacheData.getLong(key + LIMIT, -1);
        if (deadline <= 0) {
            return cacheData.getBoolean(key + DATA, false);
        } else {
            if (deadline < System.currentTimeMillis()) {
                return false;
            } else {
                return cacheData.getBoolean(key + DATA, false);
            }
        }
    }

    /**
     * 保存 键=值 ，不限定有效时间
     *
     * @param key   键
     * @param value 值
     */
    public void setCache(String key, boolean value) {
        SharedPreferences.Editor shareData = cacheData.edit();
        shareData.putBoolean(key + DATA, value);
        SharedPreferencesCompat.apply(shareData);
    }

    /**
     * 保存 键=值 ，不限定有效时间
     *
     * @param key   键
     * @param value 值
     */
    public void setCache(String key, String value) {
        SharedPreferences.Editor shareData = cacheData.edit();
        shareData.putString(key + DATA, value);
        SharedPreferencesCompat.apply(shareData);
    }

    /**
     * 保存 键=值 ， 限定有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间（需大于0，否则无效）
     */
    public void setCache(String key, String value, int time) {
        if (time > 0) {
            long limitTime = System.currentTimeMillis() + time;
            SharedPreferences.Editor shareData = cacheData.edit();
            shareData.putLong(key + LIMIT, limitTime);
            SharedPreferencesCompat.apply(shareData);
        }

        setCache(key, value);
    }

    /**
     * 是否已经存有该键 (过期的值算作存在)
     *
     * @param key 键
     * @return true：存在；false：不存在
     */
    public boolean isContains(String key) {
        return cacheData.contains(key + DATA);
    }

    /**
     * 清空 SharePreference 内容
     */
    public void clear() {
        SharedPreferences.Editor editor = cacheData.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 移除某个值
     *
     * @param key 键
     */
    public void removeCache(String key) {
        SharedPreferences.Editor shareData = cacheData.edit();
        shareData.remove(key + DATA);
        shareData.remove(key + LIMIT);
        SharedPreferencesCompat.apply(shareData);
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {

        // 避免每次都要进行反射
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException expected) {
            } catch (IllegalAccessException expected) {
            } catch (InvocationTargetException expected) {
            }

            editor.commit();
        }
    }

}