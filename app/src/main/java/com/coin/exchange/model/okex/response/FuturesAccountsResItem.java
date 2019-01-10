package com.coin.exchange.model.okex.response;

import java.io.Serializable;

/**
 * @author dean
 * @date 创建时间：2018/11/29
 * @description
 */
public class FuturesAccountsResItem implements Serializable {
    private String currency; //币种
    private String realized_margin;//已实现盈亏
    private String all_equity;//总权益
    private String used_margin;//已用保证金
    private String available_margin;//可用保证金
    private String freezing_deposit;//冻结保证金

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRealized_margin() {
        return realized_margin;
    }

    public void setRealized_margin(String realized_margin) {
        this.realized_margin = realized_margin;
    }

    public String getAll_equity() {
        return all_equity;
    }

    public void setAll_equity(String all_equity) {
        this.all_equity = all_equity;
    }

    public String getUsed_margin() {
        return used_margin;
    }

    public void setUsed_margin(String used_margin) {
        this.used_margin = used_margin;
    }

    public String getAvailable_margin() {
        return available_margin;
    }

    public void setAvailable_margin(String available_margin) {
        this.available_margin = available_margin;
    }

    public String getFreezing_deposit() {
        return freezing_deposit;
    }

    public void setFreezing_deposit(String freezing_deposit) {
        this.freezing_deposit = freezing_deposit;
    }
}
