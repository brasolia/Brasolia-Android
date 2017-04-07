package br.com.brasolia.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.net.Uri;

import com.google.gson.Gson;

import br.com.brasolia.R;
import io.realm.RealmObject;

/**
 * Created by Matheus on 25/07/2016.
 */
public class User extends BaseObservable {
    private String name;
    private String lastName;
    private String photo;
    private String email;
    private String facebookId;
    private static User u;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static User getU() {
        return u;
    }

    public static void setU(User u) {
        User.u = u;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static void setUser(Context context, User u){
        SharedPreferences sharedPref = context.getSharedPreferences("brasolia", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        Gson g = new Gson();
        editor.putString("user", g.toJson(u));
        editor.commit();
        User.u = u;
    }

    public static User getUser(Context context){
        if(u != null)
            return u;
        SharedPreferences sharedPref = context.getSharedPreferences("brasolia", context.MODE_PRIVATE);
        String str = sharedPref.getString("user", null);
        if(str == null)
            return null;
        Gson g = new Gson();
        u =  g.fromJson(str, User.class);
        return u;
    }
}
