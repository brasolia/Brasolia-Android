package br.com.brasolia.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.brasolia.R;
import br.com.brasolia.webserver.BrasoliaAPI;
import io.realm.RealmObject;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Matheus on 19/07/2016.
 */
public class BrasoliaApplication extends Application {
    Retrofit retrofit;
    BrasoliaAPI api;



    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public BrasoliaAPI getApi(){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.serverUrl))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(BrasoliaAPI.class);


        return api;
    }


}
