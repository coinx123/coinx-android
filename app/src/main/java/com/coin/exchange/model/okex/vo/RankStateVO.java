package com.coin.exchange.model.okex.vo;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/20
 * @description
 */
public class RankStateVO {

    public static final int ERROR = 1;
    public static final int LOADING = 2;
    public static final int EMPTY = 3;
    public static final int SUCCESS = 4;
    public static final int WAITING = 5;

    public static final String ERROR_MSG = "网络异常，请刷新重试";
    public static final String LOADING_MSG = "正在加载中";
    public static final String EMPTY_MSG = "暂无排行榜信息";
    public static final String SUCCESS_MSG = "";
    public static final String WAITING_MSG = "敬请期待";

    private int state;
    private String stateMsg;

    public RankStateVO(int state, String stateMsg) {
        this.state = state;
        this.stateMsg = stateMsg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateMsg() {
        return stateMsg;
    }

    public void setStateMsg(String stateMsg) {
        this.stateMsg = stateMsg;
    }
}
