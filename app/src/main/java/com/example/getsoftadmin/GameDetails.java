package com.example.getsoftadmin;

public class GameDetails {

    public String getName() {
        return Name;
    }

    public String getDesc() {
        return desc;
    }

    public String getPrice() {
        return price;
    }

    public String getImgurl() {
        return imgurl;
    }

    public GameDetails() {
    }

    public String getI() {
        return i;
    }

    private String Name;
    private String desc;
    private String price;
    private String imgurl;
    private String i;
    private String id;
    private String time;

    public GameDetails(String name, String desc, String price, String imgurl, String i, String id, String time, String date) {
        Name = name;
        this.desc = desc;
        this.price = price;
        this.imgurl = imgurl;
        this.i = i;
        this.id = id;
        this.time = time;
        this.date = date;
    }

    private String date;


    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }



}
