package com.coin.exchange.model.bitMex.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/17
 * @description
 */
public class UserInfoRes {

    @SerializedName("id")
    private int id;
    @SerializedName("ownerId")
    private int ownerId;
    @SerializedName("firstname")
    private String firstname;
    @SerializedName("lastname")
    private String lastname;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("created")
    private String created;
    @SerializedName("lastUpdated")
    private String lastUpdated;
    @SerializedName("restrictedEngineFields")
    private RestrictedEngineFields restrictedEngineFields;
    @SerializedName("TFAEnabled")
    private String TFAEnabled;
    @SerializedName("affiliateID")
    private String affiliateID;
    @SerializedName("pgpPubKey")
    private String pgpPubKey;
    @SerializedName("country")
    private String country;
    @SerializedName("geoipCountry")
    private String geoipCountry;
    @SerializedName("geoipRegion")
    private String geoipRegion;
    @SerializedName("typ")
    private String typ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public RestrictedEngineFields getRestrictedEngineFields() {
        return restrictedEngineFields;
    }

    public void setRestrictedEngineFields(RestrictedEngineFields restrictedEngineFields) {
        this.restrictedEngineFields = restrictedEngineFields;
    }

    public String getTFAEnabled() {
        return TFAEnabled;
    }

    public void setTFAEnabled(String TFAEnabled) {
        this.TFAEnabled = TFAEnabled;
    }

    public String getAffiliateID() {
        return affiliateID;
    }

    public void setAffiliateID(String affiliateID) {
        this.affiliateID = affiliateID;
    }

    public String getPgpPubKey() {
        return pgpPubKey;
    }

    public void setPgpPubKey(String pgpPubKey) {
        this.pgpPubKey = pgpPubKey;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGeoipCountry() {
        return geoipCountry;
    }

    public void setGeoipCountry(String geoipCountry) {
        this.geoipCountry = geoipCountry;
    }

    public String getGeoipRegion() {
        return geoipRegion;
    }

    public void setGeoipRegion(String geoipRegion) {
        this.geoipRegion = geoipRegion;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public static class OrderBookBinning {
    }

    public static class RestrictedEngineFields {
    }
}
