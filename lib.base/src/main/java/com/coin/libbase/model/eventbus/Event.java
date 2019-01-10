package com.coin.libbase.model.eventbus;

import android.support.annotation.NonNull;

/**
 * @author dean
 * @date 创建时间：2018/11/15
 * @description
 */
public class Event {

    public static class AddOptionalEvent {
        public boolean isAdd() {
            return isAdd;
        }

        public void setAdd(boolean add) {
            isAdd = add;
        }

        private boolean isAdd;

        public AddOptionalEvent(boolean isAdd) {
            this.isAdd = isAdd;
        }
    }

    public static class KlineIndex {

        public KlineIndex(int index) {
            this.index = index;
        }

        public int index;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public static class SendInstrument {
        public String time;
        public String instrumentId;
        public String from;

        public SendInstrument(@NonNull String time, @NonNull String instrumentId, @NonNull String from) {
            this.time = time;
            this.instrumentId = instrumentId;
            this.from = from;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getInstrumentId() {
            return instrumentId;
        }

        public void setInstrumentId(String instrumentId) {
            this.instrumentId = instrumentId;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }

    /**
     * 重绘页面
     */
    public static class RestartEvent {

    }

    /**
     * 影藏合约现在也
     */
    public static class HideInstrument {

    }
}
