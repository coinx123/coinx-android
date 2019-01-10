package com.coin.libbase.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.coin.libbase.view.fragment.JBaseFragment;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/21
 * @description
 */

public class FragmentCompat {

    /**
     * 专用于定制fragment流程
     */
    public static class Flow {
        /**
         * 初次启动fragment
         *
         * @param savedInstanceState 判断是否需要初始化加载
         * @param fragmentManager    管理器
         * @param containerId        容器id
         * @param to                 目标Fragment
         */
        public static void add(Bundle savedInstanceState,
                               FragmentManager fragmentManager,
                               int containerId,
                               Fragment to) {
            if (savedInstanceState == null) {
                add(fragmentManager, containerId, to);
            }
        }

        /**
         * 初次启动fragment
         *
         * @param fragmentManager 管理器
         * @param containerId     容器id
         * @param to              目标Fragment
         */
        public static void add(FragmentManager fragmentManager,
                               int containerId,
                               Fragment to) {
            start(fragmentManager, containerId, null, to);
        }

        /**
         * 切换fragment
         *
         * @param fragmentManager 管理器
         * @param containerId     容器id
         * @param from            被隐藏的fragment
         * @param to              目标Fragment
         */
        public static void toggle(FragmentManager fragmentManager,
                                  int containerId,
                                  Fragment from,
                                  Fragment to) {
            start(fragmentManager, containerId, from, to);
        }

        private static void start(FragmentManager fragmentManager,
                                  int containerId,
                                  Fragment from,
                                  Fragment to) {
            String toName = to.getClass().getName();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            if (from == null) {
                ft.add(containerId, to, toName);
            } else {
                ft.add(containerId, to, toName);
                ft.hide(from);
            }

            //添加至回退站
            ft.addToBackStack(toName);
            ft.commit();
        }
    }

    /**
     * 专用于定制类似微信的tab切换fragment
     */
    public static class Layer {

        /**
         * 初始化fragment，list
         *
         * @param fragmentManager 管理器
         * @param containerId     容器id
         * @param showPosition    显示的下标
         * @param fragments       fragment的list
         */
        public static void init(FragmentManager fragmentManager,
                                int containerId,
                                int showPosition,
                                List<Fragment> fragments) {

            FragmentTransaction ft = fragmentManager.beginTransaction();
            int size = fragments.size();
            for (int i = 0; i < size; i++) {
                Fragment fragment = fragments.get(i);

                String toName = fragment.getClass().getName();

                if(!fragmentManager.getFragments().contains(fragment)){
                    ft.add(containerId, fragment, toName);
                }

                if (i != showPosition) {
                    ft.hide(fragment);
                } else {
                    fragment.onHiddenChanged(false);
                }
            }
            ft.commit();

        }

        /**
         * 获取在管理器中的fragment，进行恢复获取
         *
         * @param fragmentManager 管理器
         * @param myFragment      获取的管理器中的fragment列表
         */
        public static void restoreInstance(FragmentManager fragmentManager,
                                           List<Fragment> myFragment) {

            List<Fragment> fragments = fragmentManager.getFragments();
            myFragment.clear();
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (fragment instanceof JBaseFragment) {
                        myFragment.add(fragments.indexOf(fragment), fragment);
                    }
                }
            }

        }

        /**
         * 切换fragment
         *
         * @param fragmentManager 管理器
         * @param hideFragment    隐藏的fragment
         * @param showFragment    显示的fragment
         */
        public static void toggle(FragmentManager fragmentManager,
                                  Fragment hideFragment,
                                  Fragment showFragment) {

            if (fragmentManager == null || showFragment == hideFragment) {
                return;
            }

            FragmentTransaction ft = fragmentManager.beginTransaction().show(showFragment);

            if (hideFragment == null) {
                List<Fragment> fragments = fragmentManager.getFragments();
                if (fragments != null && fragments.size() > 0) {
                    for (Fragment fragment : fragments) {
                        if (fragment != null && fragment != showFragment) {
                            ft.hide(fragment);
                        }
                    }
                }
            } else {
                ft.hide(hideFragment);
            }

            ft.commit();

        }

        /**
         * currentPage 是否为当前显示的fragment
         *
         * @param fragmentManager 管理器
         * @param currentPage     当前的fragment
         * @return true：当前显示的fragment == currentPage；false：当前显示的fragment != currentPage
         */
        public static boolean isCurrent(FragmentManager fragmentManager,
                                        Fragment currentPage) {

            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (fragment != null && !fragment.isHidden() && fragment == currentPage) {
                        return true;
                    }
                }
            }

            return false;

        }
    }


    /**
     * 实现fragment监听返回键事件
     *
     * @param fragmentActivity 装载 fragment 的 activity 的视图
     * @return true：消费该事件；false：不消费该回退事件
     */
    public static boolean onBackPressed(FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        return isConsumeBackEvent(fragmentManager);
    }

    /**
     * 判断当前manager内的fragment是否消费回退事件
     * 根据 {@link JBaseFragment#onConsumeBackEvent(FragmentManager)} 的返回值进行判断
     * true 为需要消费， false 为不需要消费
     *
     * @param fragmentManager 调用对象自己的Manager
     * @return true 有要消费该回退事件的fragment；false 没有要消费该事件的fragment
     */
    public static boolean isConsumeBackEvent(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return false;
        }

        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            int size = fragments.size();

            for (int i = size - 1; i >= 0; i--) {
                Fragment fragment = fragments.get(i);
                if (fragment != null && fragment instanceof JBaseFragment) {
                    boolean consume = ((JBaseFragment) fragment).onConsumeBackEvent(fragmentManager);
                    Log.e("FragmentCompat:", fragment.toString() + ",是否消费:" + consume);

                    if (consume) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 打印 FragmentManager 的信息，主要用于调试
     *
     * @param fragmentManager 管理器
     */
    public static void printFragmentLog(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                Log.e("fragment_list", fragment != null ? fragment.toString() + ",isHidden:" + fragment.isHidden() : "null");
            }
        } else {
            Log.e("fragment_list", "Fragment is null.");
        }
    }

}
