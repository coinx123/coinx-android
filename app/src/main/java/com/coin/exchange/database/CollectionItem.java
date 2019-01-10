package com.coin.exchange.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class CollectionItem implements Comparable<CollectionItem> {

    @NonNull
    private String mUrl = "";  //合约id
    @NonNull
    private String mTitle = "";  //合约时间，如当周，次周和季度

    private int mPosition = 0;//0代表okex，1代表bitmex

    public int getVolume_24h() {
        return volume_24h;
    }

    public void setVolume_24h(int volume_24h) {
        this.volume_24h = volume_24h;
    }

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    private int volume_24h;
    private double last;
    private String quoteCurrency = "";

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    public double getIndicativeSettlePrice() {
        return indicativeSettlePrice;
    }

    public void setIndicativeSettlePrice(double indicativeSettlePrice) {
        this.indicativeSettlePrice = indicativeSettlePrice;
    }

    public double getLastChangePcnt() {
        return lastChangePcnt;
    }

    public void setLastChangePcnt(double lastChangePcnt) {
        this.lastChangePcnt = lastChangePcnt;
    }

    private double indicativeSettlePrice;//bitmex  指数价格
    private double lastChangePcnt;//bitmex 百分比

    public CollectionItem() {
    }

    public CollectionItem(@NonNull String url, @NonNull String title) {
        this.mUrl = url;
        this.mTitle = title;
    }

    public CollectionItem(@NonNull String url, @NonNull String title, int mPosition) {
        this.mUrl = url;
        this.mTitle = title;
        this.mPosition = mPosition;
    }

    public void setPosition(int order) {
        mPosition = order;
    }

    public int getPosition() {
        return mPosition;
    }

    @NonNull
    public String getUrl() {
        return this.mUrl;
    }

    public void setUrl(@Nullable String url) {
        this.mUrl = (url == null) ? "" : url;
    }

    @NonNull
    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(@Nullable String title) {
        this.mTitle = (title == null) ? "" : title;
    }

    @Override
    public String toString() {
        return "{" +
                "mUrl='" + mUrl + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mPosition=" + mPosition +
                ", volume_24h=" + volume_24h +
                ", last=" + last +
                '}';
    }

    @Override
    public int compareTo(@NonNull CollectionItem another) {
        int compare = this.mTitle.compareToIgnoreCase(another.mTitle);
        if (compare == 0) {
            return this.mUrl.compareTo(another.mUrl);
        }
        return compare;
    }


}
