package com.example.cityguide.Database;

import android.content.Context;
import android.content.SharedPreferences;

public class Remember2 {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public String Remeber_counter = "count";

    public Remember2(Context _context) {
        context = _context;
        sharedPreferences = context.getSharedPreferences("rememberMe",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createRememberSession(String counter){
        editor.putString(Remeber_counter,counter);

        editor.commit();
    }

    public String getDetail(){
        String count = sharedPreferences.getString(Remeber_counter,null);
        return count;
    }
}
