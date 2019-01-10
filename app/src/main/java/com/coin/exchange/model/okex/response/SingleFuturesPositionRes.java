package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author dean
 * @date 创建时间：2018/11/5
 * @description 单个合约持仓信息(手机端很可能用不到暂时不写字段注释)
 */
public class SingleFuturesPositionRes {

    @SerializedName("margin_mode")
    private String margin_mode;
    @SerializedName("holding")
    private List<Holding> holding;
    @SerializedName("result")
    private boolean result;

    public String getMargin_mode() {
        return margin_mode;
    }

    public void setMargin_mode(String margin_mode) {
        this.margin_mode = margin_mode;
    }

    public List<Holding> getHolding() {
        return holding;
    }

    public void setHolding(List<Holding> holding) {
        this.holding = holding;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public static class Holding {
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("short_leverage")
        private String short_leverage;
        @SerializedName("long_leverage")
        private String long_leverage;
        @SerializedName("instrument_id")
        private String instrument_id;
        @SerializedName("short_settlement_price")
        private String short_settlement_price;
        @SerializedName("short_avg_cost")
        private String short_avg_cost;
        @SerializedName("short_pnl_ratio")
        private String short_pnl_ratio;
        @SerializedName("short_liqui_price")
        private String short_liqui_price;
        @SerializedName("short_margin")
        private String short_margin;
        @SerializedName("short_avail_qty")
        private String short_avail_qty;
        @SerializedName("short_qty")
        private String short_qty;
        @SerializedName("realised_pnl")
        private String realised_pnl;
        @SerializedName("long_settlement_price")
        private String long_settlement_price;
        @SerializedName("long_avg_cost")
        private String long_avg_cost;
        @SerializedName("long_pnl_ratio")
        private String long_pnl_ratio;
        @SerializedName("long_liqui_price")
        private String long_liqui_price;
        @SerializedName("long_margin")
        private String long_margin;
        @SerializedName("long_avail_qty")
        private String long_avail_qty;
        @SerializedName("long_qty")
        private String long_qty;
        @SerializedName("leverage")
        private String leverage;
        @SerializedName("liquidation_price")
        private String liquidation_price;

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getShort_leverage() {
            return short_leverage;
        }

        public void setShort_leverage(String short_leverage) {
            this.short_leverage = short_leverage;
        }

        public String getLong_leverage() {
            return long_leverage;
        }

        public void setLong_leverage(String long_leverage) {
            this.long_leverage = long_leverage;
        }

        public String getInstrument_id() {
            return instrument_id;
        }

        public void setInstrument_id(String instrument_id) {
            this.instrument_id = instrument_id;
        }

        public String getShort_settlement_price() {
            return short_settlement_price;
        }

        public void setShort_settlement_price(String short_settlement_price) {
            this.short_settlement_price = short_settlement_price;
        }

        public String getShort_avg_cost() {
            return short_avg_cost;
        }

        public void setShort_avg_cost(String short_avg_cost) {
            this.short_avg_cost = short_avg_cost;
        }

        public String getShort_pnl_ratio() {
            return short_pnl_ratio;
        }

        public void setShort_pnl_ratio(String short_pnl_ratio) {
            this.short_pnl_ratio = short_pnl_ratio;
        }

        public String getShort_liqui_price() {
            return short_liqui_price;
        }

        public void setShort_liqui_price(String short_liqui_price) {
            this.short_liqui_price = short_liqui_price;
        }

        public String getShort_margin() {
            return short_margin;
        }

        public void setShort_margin(String short_margin) {
            this.short_margin = short_margin;
        }

        public String getShort_avail_qty() {
            return short_avail_qty;
        }

        public void setShort_avail_qty(String short_avail_qty) {
            this.short_avail_qty = short_avail_qty;
        }

        public String getShort_qty() {
            return short_qty;
        }

        public void setShort_qty(String short_qty) {
            this.short_qty = short_qty;
        }

        public String getRealised_pnl() {
            return realised_pnl;
        }

        public void setRealised_pnl(String realised_pnl) {
            this.realised_pnl = realised_pnl;
        }

        public String getLong_settlement_price() {
            return long_settlement_price;
        }

        public void setLong_settlement_price(String long_settlement_price) {
            this.long_settlement_price = long_settlement_price;
        }

        public String getLong_avg_cost() {
            return long_avg_cost;
        }

        public void setLong_avg_cost(String long_avg_cost) {
            this.long_avg_cost = long_avg_cost;
        }

        public String getLong_pnl_ratio() {
            return long_pnl_ratio;
        }

        public void setLong_pnl_ratio(String long_pnl_ratio) {
            this.long_pnl_ratio = long_pnl_ratio;
        }

        public String getLong_liqui_price() {
            return long_liqui_price;
        }

        public void setLong_liqui_price(String long_liqui_price) {
            this.long_liqui_price = long_liqui_price;
        }

        public String getLong_margin() {
            return long_margin;
        }

        public void setLong_margin(String long_margin) {
            this.long_margin = long_margin;
        }

        public String getLong_avail_qty() {
            return long_avail_qty;
        }

        public void setLong_avail_qty(String long_avail_qty) {
            this.long_avail_qty = long_avail_qty;
        }

        public String getLong_qty() {
            return long_qty;
        }

        public void setLong_qty(String long_qty) {
            this.long_qty = long_qty;
        }

        public String getLeverage() {
            return leverage;
        }

        public void setLeverage(String leverage) {
            this.leverage = leverage;
        }

        public String getLiquidation_price() {
            return liquidation_price;
        }

        public void setLiquidation_price(String liquidation_price) {
            this.liquidation_price = liquidation_price;
        }

        @Override
        public String toString() {
            return "Holding{" +
                    "updated_at='" + updated_at + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", short_leverage='" + short_leverage + '\'' +
                    ", long_leverage='" + long_leverage + '\'' +
                    ", instrument_id='" + instrument_id + '\'' +
                    ", short_settlement_price='" + short_settlement_price + '\'' +
                    ", short_avg_cost='" + short_avg_cost + '\'' +
                    ", short_pnl_ratio='" + short_pnl_ratio + '\'' +
                    ", short_liqui_price='" + short_liqui_price + '\'' +
                    ", short_margin='" + short_margin + '\'' +
                    ", short_avail_qty='" + short_avail_qty + '\'' +
                    ", short_qty='" + short_qty + '\'' +
                    ", realised_pnl='" + realised_pnl + '\'' +
                    ", long_settlement_price='" + long_settlement_price + '\'' +
                    ", long_avg_cost='" + long_avg_cost + '\'' +
                    ", long_pnl_ratio='" + long_pnl_ratio + '\'' +
                    ", long_liqui_price='" + long_liqui_price + '\'' +
                    ", long_margin='" + long_margin + '\'' +
                    ", long_avail_qty='" + long_avail_qty + '\'' +
                    ", long_qty='" + long_qty + '\'' +
                    ", leverage='" + leverage + '\'' +
                    ", liquidation_price='" + liquidation_price + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SingleFuturesPositionRes{" +
                "margin_mode='" + margin_mode + '\'' +
                ", holding=" + holding +
                ", result=" + result +
                '}';
    }
}
