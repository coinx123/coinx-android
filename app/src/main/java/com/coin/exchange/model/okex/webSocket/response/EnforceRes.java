package com.coin.exchange.model.okex.webSocket.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/7
 * @description 合约账户信息——逐仓返回
 */
public class EnforceRes {

    // 账户余额。 TODO 和rest v3相同
    @SerializedName("balance")
    private double balance;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("contracts")
    private List<Contracts> contracts;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<Contracts> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contracts> contracts) {
        this.contracts = contracts;
    }

    public static class Contracts {
        // 固定保证金 TODO 对应rest v3>合约账户信息 >margin_fixed  冻结的保证金 成交以后仓位所需要
        @SerializedName("bond")
        private double bond;

        // 合约余额  TODO 和rest v3相同
        @SerializedName("balance")
        private double balance;

        // 已实现盈亏 TODO 对应restv3>合约账户信息> realized_pnl  已实现盈亏
        @SerializedName("profit")
        private int profit;

        // 冻结      TODO 对应 rest v3>合约账户信息 > hold 未成交的冻结
        @SerializedName("freeze")
        private double freeze;

        // 合约ID    TODO 对应rest v3>合约账户信息>instrument_id 合约id
        @SerializedName("contract_id")
        private String contract_id;

        // 合约可用  TODO 对应rest v3 >合约账户信息> available_qty  合约可用
        @SerializedName("available")
        private double available;

        public double getBond() {
            return bond;
        }

        public void setBond(double bond) {
            this.bond = bond;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public int getProfit() {
            return profit;
        }

        public void setProfit(int profit) {
            this.profit = profit;
        }

        public double getFreeze() {
            return freeze;
        }

        public void setFreeze(double freeze) {
            this.freeze = freeze;
        }

        public String getContract_id() {
            return contract_id;
        }

        public void setContract_id(String contract_id) {
            this.contract_id = contract_id;
        }

        public double getAvailable() {
            return available;
        }

        public void setAvailable(double available) {
            this.available = available;
        }
    }
}
