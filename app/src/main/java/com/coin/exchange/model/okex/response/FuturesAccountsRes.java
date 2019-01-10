package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author dean
 * @date 创建时间：2018/11/29
 * @description 所有币种合约账户信息
 */
public class FuturesAccountsRes {

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @SerializedName("info")
    private Info info;

    public static class Info {

        public Btc getBtc() {
            return btc;
        }

        public void setBtc(Btc btc) {
            this.btc = btc;
        }

        public Eth getEth() {
            return eth;
        }

        public void setEth(Eth eth) {
            this.eth = eth;
        }

        public Ltc getLtc() {
            return ltc;
        }

        public void setLtc(Ltc ltc) {
            this.ltc = ltc;
        }

        public Etc getEtc() {
            return etc;
        }

        public void setEtc(Etc etc) {
            this.etc = etc;
        }

        public Bch getBch() {
            return bch;
        }

        public void setBch(Bch bch) {
            this.bch = bch;
        }

        public Xrp getXrp() {
            return xrp;
        }

        public void setXrp(Xrp xrp) {
            this.xrp = xrp;
        }

        public Eos getEos() {
            return eos;
        }

        public void setEos(Eos eos) {
            this.eos = eos;
        }

        public Btg getBtg() {
            return btg;
        }

        public void setBtg(Btg btg) {
            this.btg = btg;
        }

        @SerializedName("btc")
        private Btc btc;
        @SerializedName("eth")
        private Eth eth;
        @SerializedName("ltc")
        private Ltc ltc;
        @SerializedName("etc")
        private Etc etc;
        @SerializedName("bch")
        private Bch bch;
        @SerializedName("xrp")
        private Xrp xrp;
        @SerializedName("eos")
        private Eos eos;
        @SerializedName("btg")
        private Btg btg;
    }

    public static class Btc {
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

        //	账户余额
        @SerializedName("total_avail_balance")
        private String total_avail_balance;
        //账户类型：全仓 crossed
        @SerializedName("margin_mode")
        private String margin_mode;
        //账户权益
        @SerializedName("equity")
        private String equity;

        //	已用保证金  全仓才有
        @SerializedName("margin")
        private String margin;
        //	保证金率  全仓才有
        @SerializedName("margin_ratio")
        private String margin_ratio;
        //	已实现盈亏  全仓才有
        @SerializedName("realized_pnl")
        private String realized_pnl;
        //	未实现盈亏  全仓才有
        @SerializedName("unrealized_pnl")
        private String unrealized_pnl;


        @SerializedName("contracts")//逐仓才有
        private List<Contracts> contracts;

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

        public String getRealized_pnl() {
            return realized_pnl;
        }

        public void setRealized_pnl(String realized_pnl) {
            this.realized_pnl = realized_pnl;
        }

        public String getUnrealized_pnl() {
            return unrealized_pnl;
        }

        public void setUnrealized_pnl(String unrealized_pnl) {
            this.unrealized_pnl = unrealized_pnl;
        }

    }

    public static class Eth {
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

        @SerializedName("total_avail_balance")
        private String total_avail_balance;
        @SerializedName("margin_mode")
        private String margin_mode;
        @SerializedName("equity")
        private String equity;

        //	已用保证金  全仓才有
        @SerializedName("margin")
        private String margin;
        //	保证金率  全仓才有
        @SerializedName("margin_ratio")
        private String margin_ratio;

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

        public String getRealized_pnl() {
            return realized_pnl;
        }

        public void setRealized_pnl(String realized_pnl) {
            this.realized_pnl = realized_pnl;
        }

        public String getUnrealized_pnl() {
            return unrealized_pnl;
        }

        public void setUnrealized_pnl(String unrealized_pnl) {
            this.unrealized_pnl = unrealized_pnl;
        }

        //	已实现盈亏  全仓才有
        @SerializedName("realized_pnl")
        private String realized_pnl;
        //	未实现盈亏  全仓才有
        @SerializedName("unrealized_pnl")
        private String unrealized_pnl;

        @SerializedName("contracts")//逐仓才有
        private List<Contracts> contracts;
    }

    public static class Ltc {
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

        @SerializedName("total_avail_balance")
        private String total_avail_balance;
        @SerializedName("margin_mode")
        private String margin_mode;
        @SerializedName("equity")
        private String equity;
        @SerializedName("contracts")
        private List<Contracts> contracts;
    }

    public static class Etc {
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

        @SerializedName("total_avail_balance")
        private String total_avail_balance;
        @SerializedName("margin_mode")
        private String margin_mode;
        @SerializedName("equity")
        private String equity;
        @SerializedName("contracts")
        private List<Contracts> contracts;
    }

    public static class Bch {
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

        @SerializedName("total_avail_balance")
        private String total_avail_balance;
        @SerializedName("margin_mode")
        private String margin_mode;
        @SerializedName("equity")
        private String equity;
        @SerializedName("contracts")
        private List<Contracts> contracts;
    }

    public static class Xrp {
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

        @SerializedName("total_avail_balance")
        private String total_avail_balance;
        @SerializedName("margin_mode")
        private String margin_mode;
        @SerializedName("equity")
        private String equity;
        @SerializedName("contracts")
        private List<Contracts> contracts;
    }

    public static class Eos {
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

        @SerializedName("total_avail_balance")
        private String total_avail_balance;
        @SerializedName("margin_mode")
        private String margin_mode;
        @SerializedName("equity")
        private String equity;
        @SerializedName("contracts")
        private List<Contracts> contracts;
    }

    public static class Btg {
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

        @SerializedName("total_avail_balance")
        private String total_avail_balance;
        @SerializedName("margin_mode")
        private String margin_mode;
        @SerializedName("equity")
        private String equity;
        @SerializedName("contracts")
        private List<Contracts> contracts;
    }

    public static class Contracts {
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

        @SerializedName("unrealized_pnl")
        private String unrealized_pnl;
        @SerializedName("realized_pnl")
        private String realized_pnl;
        @SerializedName("margin_frozen")
        private String margin_frozen;
        @SerializedName("margin_for_unfilled")
        private String margin_for_unfilled;
        @SerializedName("instrument_id")
        private String instrument_id;
        @SerializedName("fixed_balance")
        private String fixed_balance;
        @SerializedName("available_qty")
        private String available_qty;
    }

}
