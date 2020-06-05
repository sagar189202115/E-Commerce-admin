package com.example.getsoftadmin;

class Model {
    private String Name,Mobile,Password,id;


    public Model() {
    }

    public Model(String name, String email, String password, String id) {
        Name = name;
        Mobile = email;
        Password = password;
        this.id=id;
    }

    public String getName() {
        return Name;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getPassword() {
        return Password;
    }
    public String getId(){
        return id;
    }
}
