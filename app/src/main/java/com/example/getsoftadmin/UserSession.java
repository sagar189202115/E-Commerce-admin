package com.example.getsoftadmin;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class UserSession {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String MYSHARED = "user";
    static List<String> gameids=new ArrayList<>();

    public UserSession(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(MYSHARED,Context.MODE_PRIVATE);
    }

    public void setData(String mobile, String name, String id){
        editor = sharedPreferences.edit();
        editor.putString("mobile",mobile);
        editor.putString("name",name);
        editor.putString("uniqueid",id);
        editor.commit();
    }
    public void destroyData()
    {
        editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();


    }
    public void setgameids(List<String> gameids){

        this.gameids=gameids;
    }

    public String getName() {
        return sharedPreferences.getString("name","");
    }

    public String getMobile() {
        return sharedPreferences.getString("mobile","");
    }

    public String getId() {
        return sharedPreferences.getString("uniqueid","");
    }


}
