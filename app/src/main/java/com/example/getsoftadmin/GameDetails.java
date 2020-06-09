package com.example.getsoftadmin;

import java.util.ArrayList;

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


    private String Name;
    private String desc;
    private String price;
    private String imgurl;

    private String id;
    private String time;
    private ArrayList<String> tags;

    public ArrayList<String> getTags() {
        return tags;
    }



    public GameDetails(String name, String desc, String price, String imgurl, String id, String time, String date, ArrayList<String> tags) {
        Name = name;
        this.tags=tags;
        this.desc = desc;
        this.price = price;
        this.imgurl = imgurl;
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
