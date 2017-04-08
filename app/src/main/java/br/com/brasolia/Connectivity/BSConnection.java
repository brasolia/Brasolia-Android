package br.com.brasolia.Connectivity;

import java.net.CookieManager;
import java.net.CookiePolicy;

import br.com.brasolia.application.BrasoliaApplication;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cayke on 07/04/17.
 */

public class BSConnection {
    private static final String API_BASE_URL = "http://52.67.150.108/api/BSB/";

    private static OkHttpClient client() {
        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(BrasoliaApplication.getAppContext()), CookiePolicy.ACCEPT_ALL);
        return new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(cookieManager)).build();
    }

    private static Retrofit builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(client())
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public static <S> S createService(Class<S> serviceClass) {
        return builder.create(serviceClass);
    }
}
