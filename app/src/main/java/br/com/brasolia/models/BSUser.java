package br.com.brasolia.models;

import android.content.SharedPreferences;

import java.util.Map;

import br.com.brasolia.application.BrasoliaApplication;

/**
 * Created by cayke on 10/04/17.
 */

public class BSUser {
    private String email;
    private String fName;
    private String lName;
    private String imageKey;
    private String gender;

    private static final String USER_PREFS = "br.com.brasolia.UserPrefsFile";

    public BSUser(Map<String, Object> dictionary) {
        email = (String) BSDictionary.getValueWithKeyAndType(dictionary, "email", String.class);
        fName = (String) BSDictionary.getValueWithKeyAndType(dictionary, "fName", String.class);
        lName = (String) BSDictionary.getValueWithKeyAndType(dictionary, "lName", String.class);
        gender = (String) BSDictionary.getValueWithKeyAndType(dictionary, "gender", String.class);
        imageKey = (String) BSDictionary.getValueWithKeyAndType(dictionary, "image", String.class);
    }

    public BSUser(String email, String name, String lName, String gender, String imageKey) {
        this.email = email;
        this.fName = name;
        this.imageKey = imageKey;
        this.lName = lName;
        this.gender = gender;
    }

    public void saveUser() {
        SharedPreferences userPrefs = BrasoliaApplication.getAppContext().getSharedPreferences(USER_PREFS, 0);
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putString("email", email);
        editor.putString("fName", fName);
        editor.putString("imageKey", imageKey);
        editor.putString("lName", lName);
        editor.putString("gender", gender);
        editor.apply();
    }

    public static BSUser loadUserFromDevice() {
        SharedPreferences userPrefs = BrasoliaApplication.getAppContext().getSharedPreferences(USER_PREFS, 0);
        String email = userPrefs.getString("email", null);
        String name = userPrefs.getString("fName", null);
        String imageKey = userPrefs.getString("imageKey", null);
        String lName = userPrefs.getString("lName", null);
        String gender = userPrefs.getString("gender", null);

        if (email == null || name == null) {
            return null;
        } else {
            return new BSUser(email, name, lName, gender, imageKey);
        }
    }

    public static void removeUserFromDevice()
    {
        SharedPreferences userPrefs = BrasoliaApplication.getAppContext().getSharedPreferences(USER_PREFS, 0);
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.remove("email");
        editor.remove("fName");
        editor.remove("imageKey");
        editor.remove("lName");
        editor.remove("gender");
        editor.apply();
    }

    public String getEmail() {
        return email;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getImageKey() {
        return imageKey;
    }

    public String getGender() {
        return gender;
    }
}
