package com.coin.exchange.config.okEx;

import com.coin.exchange.config.FragmentConfig;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/6
 * @description OkEx WebSocket 请求数据 辅助类
 * <p>
 * ① X值为：btc, ltc, eth, etc, bch, eos, xrp, btg
 *  ② Y值为：this_week, next_week, quarter
 * ③ Z值为：1min, 3min, 5min, 15min, 30min, 1hour, 2hour, 4hour, 6hour, 12hour, day, 3day, week
 * 4. Z1值为：5, 10, 20(获取深度条数)
 */
public final class ChannelHelper {

    public static final class X {
        public static final String BTC = "btc"; // 比特币

        public static final String LTC = "ltc"; // 莱特币
        public static final String ETH = "eth"; // 以太坊
        public static final String ETC = "etc"; //
        public static final String BCH = "bch";
        public static final String EOS = "eos";
        public static final String XRP = "xrp";
        public static final String BTG = "btg";
    }

    public static final class Y {

        public static final String THIS_WEEK = "this_week";
        public static final String NEXT_WEEK = "next_week";
        public static final String QUARTER = "quarter";

    }

    public static final class Z {

        public static final String ONE_MIN = "1min";
        public static final String THREE_MIN = "3min";
        public static final String FIVE_MIN = "5min";
        public static final String FIFTEEN_MIN = "15min";
        public static final String THIRTY_MIN = "30min";

        public static final String ONE_HOUR = "1hour";
        public static final String TWO_HOUR = "2hour";
        public static final String FOUR_HOUR = "4hour";
        public static final String SIX_HOUR = "6hour";
        public static final String TWELVE_HOUR = "12hour";

        public static final String ONE_DAY = "day";
        public static final String THREE_DAY = "3day";

        public static final String WEEK = "week";

    }

    public static final class Z1 {

        public static final String FIVE = "5";
        public static final String TEN = "10";
        public static final String TWENTY = "20";

    }

    // 订阅合约行情
    public static final String FUTURE_DETAIL = "ok_sub_futureusd_%1$s_ticker_%2$s";
//    public static final String FUTURE_DETAIL = "ok_sub_futu_%1$s_ticker_%2$s";

    // 阅合约K线数据
    public static final String FUTURE_K_LINE = "ok_sub_futureusd_%1$s_kline_%2$s_%3$s";

    // 订阅合约市场深度(200增量数据返回)
    public static final String FUTURE_DEPTH = "ok_sub_futureusd_%1$s_depth_%2$s";

    // 订阅合约市场深度(全量返回)
    public static final String FUTURE_ALL_DEPTH = "ok_sub_futureusd_%1$s_depth_%2$s_%3$s";

    // 订阅合约交易信息
    public static final String FUTURE_TRADE = "ok_sub_futureusd_%1$s_trade_%2$s";

    // 订阅合约指数
    public static final String FUTURE_INDEX = "ok_sub_futureusd_%1$s_index";

    // 合约预估交割价格
    public static final String FUTURE_FORECAST = "%1$s_forecast_price";

    // 交易信息 [需个人信息]
    public static final String FUTURE_PERSON_TRADES = "ok_sub_futureusd_trades";

    // 合约账户信息 [需个人信息]
    public static final String FUTURE_PERSON_USER_INFO = "ok_sub_futureusd_userinfo";

    public static String getTime(String time) {
        if (time.contains(FragmentConfig.WEEK)) {
            return ChannelHelper.Y.THIS_WEEK;
        } else if (time.contains(FragmentConfig.NEXT_WEEK)) {
            return ChannelHelper.Y.NEXT_WEEK;
        } else if (time.contains(FragmentConfig.QUARTER)) {
            return ChannelHelper.Y.QUARTER;
        }
        return ChannelHelper.Y.THIS_WEEK;
    }

}
