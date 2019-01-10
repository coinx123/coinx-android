package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author dean
 * @date 创建时间：2018/11/6
 * @description 获取订单列表
 */
public class FuturesOrderListRes {

    @SerializedName("order_info")
    private List<Order_info> order_info;
    @SerializedName("result")
    private boolean result;

    public List<Order_info> getOrder_info() {
        return order_info;
    }

    public void setOrder_info(List<Order_info> order_info) {
        this.order_info = order_info;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public static class Order_info {
        //	杠杆倍数 value:10/20 默认10
        @SerializedName("leverage")
        private String leverage;
        //合约面值
        @SerializedName("contract_val")
        private String contract_val;
        //	订单类型(1:开多 2:开空 3:开多 4:平空)
        @SerializedName("type")
        private String type;
        //	订单状态(-1.撤单成功；0:等待成交 1:部分成交 2:已完成）
        @SerializedName("status")
        private String status;
        //平均价格
        @SerializedName("price_avg")
        private String price_avg;
        //	订单价格
        @SerializedName("price")
        private String price;
        //	订单ID
        @SerializedName("order_id")
        private String order_id;
        //手续费
        @SerializedName("fee")
        private String fee;
        //	成交数量
        @SerializedName("filled_qty")
        private String filled_qty;
        //	委托时间
        @SerializedName("timestamp")
        private String timestamp;
        //	数量
        @SerializedName("size")
        private String size;
        //	合约ID，如BTC-USD-180213
        @SerializedName("instrument_id")
        private String instrument_id;

        public String getLeverage() {
            return leverage;
        }

        public void setLeverage(String leverage) {
            this.leverage = leverage;
        }

        public String getContract_val() {
            return contract_val;
        }

        public void setContract_val(String contract_val) {
            this.contract_val = contract_val;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPrice_avg() {
            return price_avg;
        }

        public void setPrice_avg(String price_avg) {
            this.price_avg = price_avg;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getFilled_qty() {
            return filled_qty;
        }

        public void setFilled_qty(String filled_qty) {
            this.filled_qty = filled_qty;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getInstrument_id() {
            return instrument_id;
        }

        public void setInstrument_id(String instrument_id) {
            this.instrument_id = instrument_id;
        }

        @Override
        public String toString() {
            return "Order_info{" +
                    "leverage='" + leverage + '\'' +
                    ", contract_val='" + contract_val + '\'' +
                    ", type='" + type + '\'' +
                    ", status='" + status + '\'' +
                    ", price_avg='" + price_avg + '\'' +
                    ", price='" + price + '\'' +
                    ", order_id='" + order_id + '\'' +
                    ", fee='" + fee + '\'' +
                    ", filled_qty='" + filled_qty + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    ", size='" + size + '\'' +
                    ", instrument_id='" + instrument_id + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FuturesOrderListRes{" +
                "order_info=" + order_info +
                ", result=" + result +
                '}';
    }
}
