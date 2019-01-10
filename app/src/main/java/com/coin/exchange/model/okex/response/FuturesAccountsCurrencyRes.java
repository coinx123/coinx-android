package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author dean
 * @date 创建时间：2018/11/7
 * @description 单个币种合约账户信息
 */
public class FuturesAccountsCurrencyRes {
    //账户余额
    @SerializedName("total_avail_balance")
    private String total_avail_balance;
    //	账户类型：逐仓 fixed
    @SerializedName("margin_mode")
    private String margin_mode;
    //	账户权益
    @SerializedName("equity")
    private String equity;

    @SerializedName("contracts")
    private List<Contracts> contracts;

    public String getTotal_avail_balance() {
        return total_avail_balance;
    }

    public void setTotal_avail_balance(String total_avail_balance) {
        this.total_avail_balance = total_avail_balance;
    }

    public String getMargin_mode() {
        return margin_mode;
    }

    public void setMargin_mode(String margin_mode) {
        this.margin_mode = margin_mode;
    }

    public String getEquity() {
        return equity;
    }

    public void setEquity(String equity) {
        this.equity = equity;
    }

    public List<Contracts> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contracts> contracts) {
        this.contracts = contracts;
    }

    public static class Contracts {
        //	未实现盈亏
        @SerializedName("unrealized_pnl")
        private String unrealized_pnl;
        //	已实现盈亏
        @SerializedName("realized_pnl")
        private String realized_pnl;
        //冻结的保证金(成交以后仓位所需的)
        @SerializedName("margin_frozen")
        private String margin_frozen;
        //	挂单冻结保证金
        @SerializedName("margin_for_unfilled")
        private String margin_for_unfilled;
        //合约id
        @SerializedName("instrument_id")
        private String instrument_id;
        //	逐仓账户余额
        @SerializedName("fixed_balance")
        private String fixed_balance;
        //逐仓可用余额
        @SerializedName("available_qty")
        private String available_qty;
        //已用保证金  全仓字段
        @SerializedName("margin")
        private String margin;
        //保证金率  全仓字段
        @SerializedName("margin_ratio")
        private String margin_ratio;

        public String getUnrealized_pnl() {
            return unrealized_pnl;
        }

        public void setUnrealized_pnl(String unrealized_pnl) {
            this.unrealized_pnl = unrealized_pnl;
        }

        public String getRealized_pnl() {
            return realized_pnl;
        }

        public void setRealized_pnl(String realized_pnl) {
            this.realized_pnl = realized_pnl;
        }

        public String getMargin_frozen() {
            return margin_frozen;
        }

        public void setMargin_frozen(String margin_frozen) {
            this.margin_frozen = margin_frozen;
        }

        public String getMargin_for_unfilled() {
            return margin_for_unfilled;
        }

        public void setMargin_for_unfilled(String margin_for_unfilled) {
            this.margin_for_unfilled = margin_for_unfilled;
        }

        public String getInstrument_id() {
            return instrument_id;
        }

        public void setInstrument_id(String instrument_id) {
            this.instrument_id = instrument_id;
        }

        public String getFixed_balance() {
            return fixed_balance;
        }

        public void setFixed_balance(String fixed_balance) {
            this.fixed_balance = fixed_balance;
        }

        public String getAvailable_qty() {
            return available_qty;
        }

        public void setAvailable_qty(String available_qty) {
            this.available_qty = available_qty;
        }

        public String getMargin() {
            return margin;
        }

        public void setMargin(String margin) {
            this.margin = margin;
        }

        public String getMargin_ratio() {
            return margin_ratio;
        }

        public void setMargin_ratio(String margin_ratio) {
            this.margin_ratio = margin_ratio;
        }

        @Override
        public String toString() {
            return "Contracts{" +
                    "unrealized_pnl='" + unrealized_pnl + '\'' +
                    ", realized_pnl='" + realized_pnl + '\'' +
                    ", margin_frozen='" + margin_frozen + '\'' +
                    ", margin_for_unfilled='" + margin_for_unfilled + '\'' +
                    ", instrument_id='" + instrument_id + '\'' +
                    ", fixed_balance='" + fixed_balance + '\'' +
                    ", available_qty='" + available_qty + '\'' +
                    ", margin='" + margin + '\'' +
                    ", margin_ratio='" + margin_ratio + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FuturesAccountsCurrencyRes{" +
                "total_avail_balance='" + total_avail_balance + '\'' +
                ", margin_mode='" + margin_mode + '\'' +
                ", equity='" + equity + '\'' +
                ", contracts=" + contracts +
                '}';
    }
}
