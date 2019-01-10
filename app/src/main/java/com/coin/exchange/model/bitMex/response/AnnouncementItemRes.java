package com.coin.exchange.model.bitMex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/31
 * @description
 */
public class AnnouncementItemRes {

    @SerializedName("id")
    private int id;
    @SerializedName("link")
    private String link;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("date")
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "AnnouncementItemRes{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
