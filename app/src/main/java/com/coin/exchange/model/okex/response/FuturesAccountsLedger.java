package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author dean
 * @date 创建时间：2018/11/5
 * @description 账单流水查询(对应的界面应该是合约账单界面)
 */
public class FuturesAccountsLedger {
    //流水来源
    @SerializedName("type")
    private String type;
    //币种，如：btc
    @SerializedName("currency")
    private String currency;
    //
    @SerializedName("balance")
    private String balance;
    //	变动数量
    @SerializedName("amount")
    private String amount;
    //	账单创建时间
    @SerializedName("timestamp")
    private String timestamp;
    //	账单ID
    @SerializedName("ledger_id")
    private String ledger_id;
    //	如果类型是交易产生的，则该details字段将包含order_id和instrument_id
    @SerializedName("details")
    private Details details;

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLedger_id() {
        return ledger_id;
    }

    public void setLedger_id(String ledger_id) {
        this.ledger_id = ledger_id;
    }

    public static class Details {
        //	订单ID
        @SerializedName("order_id")
        private double order_id;
        //合约ID，如BTC-USD-180213
        @SerializedName("instrument_id")
        private String instrument_id;

        public double getOrder_id() {
            return order_id;
        }

        public void setOrder_id(double order_id) {
            this.order_id = order_id;
        }

        public String getInstrument_id() {
            return instrument_id;
        }

        public void setInstrument_id(String instrument_id) {
            this.instrument_id = instrument_id;
        }

        @Override
        public String toString() {
            return "Details{" +
                    "order_id='" + order_id + '\'' +
                    ", instrument_id='" + instrument_id + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FuturesAccountsLedger{" +
                "type='" + type + '\'' +
                ", currency='" + currency + '\'' +
                ", balance='" + balance + '\'' +
                ", amount='" + amount + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", ledger_id='" + ledger_id + '\'' +
                ", details=" + details +
                '}';
    }
}
