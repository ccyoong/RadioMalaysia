package com.ccyoong.radiomalaysia;

import java.io.Serializable;

public class Station implements Serializable {


    private String id;

    private String name;

    private String slogan;

    private String onlineUrl;

    public Station(String id, String name, String slogan, String onlineUrl) {
        this.id = id;
        this.name = name;
        this.slogan = slogan;
        this.onlineUrl = onlineUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getOnlineUrl() {
        return onlineUrl;
    }

    public void setOnlineUrl(String onlineUrl) {
        this.onlineUrl = onlineUrl;
    }
}
