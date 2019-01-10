package com.coin.exchange.config;

import android.support.v4.app.Fragment;

import com.coin.exchange.R;
import com.coin.exchange.model.okex.vo.MenuItemVO;
import com.coin.exchange.mvp.MineBind.MineBindFragment;
import com.coin.exchange.mvp.TradeFuturesAll.TradeFuturesAllFragment;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.view.fragment.kline.KFragmentLine;
import com.coin.exchange.view.fragment.main.MarketFragment;
import com.coin.exchange.view.fragment.main.MineFragment;
import com.coin.exchange.view.fragment.main.TradeFragment;
import com.coin.exchange.view.fragment.main.trade.TradeFuturesOptionalFragment;
import com.coin.exchange.view.fragment.main.trade.TradeOkAndBitFragment;
import com.coin.exchange.view.fragment.trade.ActivityTradeFragment;
import com.coin.exchange.view.fragment.trade.delegation.BitMEXDelegationFragment;
import com.coin.exchange.view.fragment.trade.delegation.DelegationFragment;
import com.coin.exchange.view.fragment.trade.position.PositionFragment;

import java.util.ArrayList;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/12
 * @description fragment的汇总列表
 */
public class FragmentConfig {

    public static final String INSTRUMENT_ID = "instrumentId";
    public static final String TYPE = "type";//表示当周，次周，永续等
    public static final String FROM = "from";//来自okex或者bitmex


    public static final String WEEK = "当周";
    public static final String NEXT_WEEK = "次周";
    public static final String QUARTER = "季度";

    //======================================首页配置=================================================
    // 首页导航的菜单
    private final static ArrayList<MenuItemVO> MAIN_NAV;

    static {
        MAIN_NAV = new ArrayList<>();
        MAIN_NAV.add(new MenuItemVO(0, "行情", R.mipmap.icon_market_gray));
        MAIN_NAV.add(new MenuItemVO(1, "交易", R.mipmap.icon_trade_gray));
        MAIN_NAV.add(new MenuItemVO(2, "我的", R.mipmap.icon_mine_gray));
    }

    public static ArrayList<MenuItemVO> getMainNav() {
        return MAIN_NAV;
    }

    /**
     * 获取主页面 Fragment
     *
     * @param index 获取下标
     * @return 对应的fragment
     */
    public static Fragment getMainFragment(int index) {
        Fragment fragment;
        switch (index) {
            case 0:
                fragment = MarketFragment.newInstance();
                break;

            case 1:
                fragment = TradeFragment.newInstance();
                break;

            case 2:
                fragment = MineFragment.newInstance();
                break;

            default:
                fragment = MarketFragment.newInstance();
        }
        return fragment;
    }
    //==============================================================================================

    //=========================================排行榜================================================
    private final static ArrayList<MenuItemVO> RANK_NAV;

    public final static int OKEX = 0;
    public final static int BITMEX = 1;

    static {
        RANK_NAV = new ArrayList<>();
        RANK_NAV.add(new MenuItemVO(OKEX, AppUtils.OKEX, R.mipmap.icon_okex, true));
        RANK_NAV.add(new MenuItemVO(BITMEX, AppUtils.BITMEX, R.mipmap.icon_bitmex, false));
    }

    public static ArrayList<MenuItemVO> getRankNav() {
        return RANK_NAV;
    }

    //==============================================================================================

    //===========================================自选================================================

    //==============================================================================================

    //======================================交易配置=================================================
    // 首页导航的菜单
    private final static ArrayList<MenuItemVO> TRADE_NAV;

    static {
        TRADE_NAV = new ArrayList<>();
        TRADE_NAV.add(new MenuItemVO(0, AppUtils.BITMEX, R.drawable.bitmex_icon));
        TRADE_NAV.add(new MenuItemVO(1, AppUtils.OKEX, R.drawable.okex_icon));

    }

    public static ArrayList<MenuItemVO> getTradeNav() {
        return TRADE_NAV;
    }

    /**
     * 获取交易页面 Fragment
     *
     * @param index 获取下标
     * @return 对应的fragment
     */
    public static Fragment getTradeFragment(int index) {
        Fragment fragment;
        switch (index) {
            case 0:
                fragment = TradeOkAndBitFragment.newInstance(AppUtils.BITMEX);
                break;

            case 1:
                fragment = TradeOkAndBitFragment.newInstance(AppUtils.OKEX);
                break;

            default:
                fragment = TradeOkAndBitFragment.newInstance(AppUtils.BITMEX);
        }
        return fragment;
    }
    //==============================================================================================


    //======================================OKEX交易界面配置-自选，全部=================================================
    // 首页导航的菜单
    private final static ArrayList<MenuItemVO> OK_TRADE_NAV;

    static {
        OK_TRADE_NAV = new ArrayList<>();
        OK_TRADE_NAV.add(new MenuItemVO(0, "自选"));
        OK_TRADE_NAV.add(new MenuItemVO(1, "全部"));

    }

    public static ArrayList<MenuItemVO> getOKTradeNav() {
        return OK_TRADE_NAV;
    }

    /**
     * 获取交易自选，全部页面 Fragment
     *
     * @param index 获取下标
     * @return 对应的fragment
     */
    public static Fragment getOkTradeFragment(int index, String from) {
        Fragment fragment;
        switch (index) {
            case 0:
                fragment = TradeFuturesOptionalFragment.newInstance(from);
                break;

            case 1:
                fragment = TradeFuturesAllFragment.newInstance(from);
                break;

            default:
                fragment = TradeFuturesOptionalFragment.newInstance(from);
        }
        return fragment;
    }
    //==============================================================================================

    //======================================okex K线页面配置=================================================
    // 首页导航的菜单
    private final static ArrayList<MenuItemVO> KLINE_NAV;

    static {
        KLINE_NAV = new ArrayList<>();
        KLINE_NAV.add(new MenuItemVO(0, "分时"));
        KLINE_NAV.add(new MenuItemVO(1, "5分钟"));
        KLINE_NAV.add(new MenuItemVO(2, "1小时"));
        KLINE_NAV.add(new MenuItemVO(3, "4小时"));
        KLINE_NAV.add(new MenuItemVO(4, "日线"));
        KLINE_NAV.add(new MenuItemVO(5, "更多", R.mipmap.more_icon));
        KLINE_NAV.add(new MenuItemVO(6, "指标", R.mipmap.more_icon));
    }

    public static ArrayList<MenuItemVO> getKLineNAV() {
        return KLINE_NAV;
    }

    /**
     * 获取K线图页面 Fragment
     *
     * @param index 获取下标
     * @return 对应的fragment
     */
    public static Fragment getKLineFragment(int index, String id, String from) {
        Fragment fragment;
        switch (index) {
            case 0:
                fragment = KFragmentLine.newInstance(1 * 60, id, from);
                break;
            case 1:
                fragment = KFragmentLine.newInstance(5 * 60, id, from);
                break;
            case 2:
                fragment = KFragmentLine.newInstance(60 * 60, id, from);
                break;
            case 3:
                fragment = KFragmentLine.newInstance(4 * 60 * 60, id, from);
                break;
            case 4:
                fragment = KFragmentLine.newInstance(24 * 60 * 60, id, from);
                break;
            case 5:
                fragment = KFragmentLine.newInstance(15 * 60, id, from);
                break;
            case 6:
                fragment = KFragmentLine.newInstance(30 * 60, id, from);
                break;
            case 7:
                fragment = KFragmentLine.newInstance(7 * 24 * 60 * 60, id, from);
                break;

            default:
                fragment = KFragmentLine.newInstance(1 * 60, id, from);
        }
        return fragment;
    }

    //==============================================================================================
//======================================bitmex K线页面配置=================================================
    // 首页导航的菜单
    private final static ArrayList<MenuItemVO> BITMEX_KLINE_NAV;

    static {
        BITMEX_KLINE_NAV = new ArrayList<>();
        BITMEX_KLINE_NAV.add(new MenuItemVO(0, "分时"));
        BITMEX_KLINE_NAV.add(new MenuItemVO(1, "5分钟"));
        BITMEX_KLINE_NAV.add(new MenuItemVO(2, "1小时"));
//        BITMEX_KLINE_NAV.add(new MenuItemVO(3, "4小时"));
        BITMEX_KLINE_NAV.add(new MenuItemVO(3, "日线"));
//        BITMEX_KLINE_NAV.add(new MenuItemVO(4, "更多", R.mipmap.more_icon));
        BITMEX_KLINE_NAV.add(new MenuItemVO(4, "指标", R.mipmap.more_icon));
    }

    public static ArrayList<MenuItemVO> getBitmexKLineNAV() {
        return BITMEX_KLINE_NAV;
    }

    /**
     * 获取K线图页面 Fragment
     *
     * @param index 获取下标
     * @return 对应的fragment
     */
    public static Fragment getBitmexKLineFragment(int index, String id, String from) {
        Fragment fragment;
        switch (index) {
            case 0:
                fragment = KFragmentLine.newInstance(1 * 60, id, from);
                break;
            case 1:
                fragment = KFragmentLine.newInstance(5 * 60, id, from);
                break;
            case 2:
                fragment = KFragmentLine.newInstance(60 * 60, id, from);
                break;
//            case 3:
//                fragment = KFragmentLine.newInstance(4 * 60 * 60, id);
//                break;
            case 3:
                fragment = KFragmentLine.newInstance(24 * 60 * 60, id, from);
                break;
//            case 5:
//                fragment = KFragmentLine.newInstance(15 * 60, id);
//                break;
//            case 6:
//                fragment = KFragmentLine.newInstance(30 * 60, id);
//                break;
//            case 7:
//                fragment = KFragmentLine.newInstance(7 * 24 * 60 * 60, id);
//                break;

            default:
                fragment = KFragmentLine.newInstance(1 * 60, id, from);
        }
        return fragment;
    }
    //==============================================================================================


    //======================================交易界面配置=================================================
    // 交易导航的菜单
    private final static ArrayList<MenuItemVO> TRADE_ACTIVITY_NAV;

    static {
        TRADE_ACTIVITY_NAV = new ArrayList<>();
        TRADE_ACTIVITY_NAV.add(new MenuItemVO(0, "交易"));
        TRADE_ACTIVITY_NAV.add(new MenuItemVO(1, "委托"));
        TRADE_ACTIVITY_NAV.add(new MenuItemVO(2, "持仓"));
    }

    public static ArrayList<MenuItemVO> getTradeActivityNav() {
        return TRADE_ACTIVITY_NAV;
    }

    /**
     * 获取交易页面 Fragment
     *
     * @param index 获取下标
     * @return 对应的fragment
     */
    public static Fragment getTradeActivityFragment(int index, String platform) {
        Fragment fragment;
        switch (index) {
            case 0:
                fragment = ActivityTradeFragment.newInstance(platform);
                break;

            case 1:
                if (platform.equals(AppUtils.BITMEX)) {
                    fragment = BitMEXDelegationFragment.newInstance();
                } else {
                    fragment = DelegationFragment.newInstance();
                }
                break;

            case 2:
                fragment = PositionFragment.newInstance(platform);
                break;

            default:
                fragment = ActivityTradeFragment.newInstance(platform);
        }
        return fragment;
    }

    //==============================================================================================
    //======================================我的绑定配置=================================================
    // 首页导航的菜单
    private final static ArrayList<MenuItemVO> MINE_NAV;

    static {
        MINE_NAV = new ArrayList<>();
        MINE_NAV.add(new MenuItemVO(0, AppUtils.OKEX));
        MINE_NAV.add(new MenuItemVO(1, AppUtils.BITMEX));

    }

    public static ArrayList<MenuItemVO> getMineNav() {
        return MINE_NAV;
    }

    /**
     * 获取我的绑定 Fragment
     *
     * @param index 获取下标
     * @return 对应的fragment
     */
    public static Fragment getMineFragment(int index) {
        Fragment fragment;
        switch (index) {
            case 0:
                fragment = MineBindFragment.newInstance(AppUtils.OKEX);
                break;
            case 1:
                fragment = MineBindFragment.newInstance(AppUtils.BITMEX);
                break;
            default:
                fragment = MineBindFragment.newInstance(AppUtils.OKEX);
        }
        return fragment;
    }
    //==============================================================================================

    //=========================================BitMEX 委托==========================================
    // 首页导航的菜单
    private final static ArrayList<MenuItemVO> BITMEX_DELE_NAV;

    static {
        BITMEX_DELE_NAV = new ArrayList<>();
        BITMEX_DELE_NAV.add(new MenuItemVO(0, "未成交", "New"));
        BITMEX_DELE_NAV.add(new MenuItemVO(1, "已成交", "Filled"));
        BITMEX_DELE_NAV.add(new MenuItemVO(1, "已撤销", "Canceled"));
    }

    public static ArrayList<MenuItemVO> getBitMEXDeleNav() {
        return BITMEX_DELE_NAV;
    }
    //==============================================================================================

}
