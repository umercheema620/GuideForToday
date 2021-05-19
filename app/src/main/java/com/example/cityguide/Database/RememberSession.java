package com.example.cityguide.Database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class RememberSession {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_REMEMBER = "IsRememberMe";
    public static final String REMEMBER_PHONE = "phone";
    public static final String REMEMBER_PASSWORD = "password";

    public RememberSession(Context _context){
        context = _context;
        sharedPreferences = context.getSharedPreferences("rememberMe",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void createRememberSession(String phone, String password){
        editor.putBoolean(IS_REMEMBER,true);
        editor.putString(REMEMBER_PHONE,phone);
        editor.putString(REMEMBER_PASSWORD,password);

        editor.commit();
    }

    public HashMap<String,String> getRememberDetail(){
        HashMap<String, String> user = new HashMap<String,String>();
        user.put(REMEMBER_PHONE,sharedPreferences.getString(REMEMBER_PHONE,null));
        user.put(REMEMBER_PASSWORD,sharedPreferences.getString(REMEMBER_PASSWORD,null));

        return user;
    }

    public boolean checkRemember(){
        if(sharedPreferences.getBoolean(IS_REMEMBER,false)){
            return true;
        }else{
            return false;
        }
    }
}
