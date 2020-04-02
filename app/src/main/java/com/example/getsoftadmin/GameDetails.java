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

    public String getImg() {
        return img;
    }

    public GameDetails() {
    }

    public GameDetails(String name, String desc, String price, String img,String id) {
        Name = name;
        this.id=id;
        this.desc = desc;
        this.price = price;
        this.img = img;
    }

    private String Name;
    private String desc;
    private String price;
    private String img;
    private String id;

    public String getId() {
        return id;
    }



}
