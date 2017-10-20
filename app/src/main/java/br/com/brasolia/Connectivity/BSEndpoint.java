package br.com.brasolia.Connectivity;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by cayke on 20/10/17.
 */

public interface BSEndpoint {

    //TESTADAS

    @GET("categories.json?print=pretty&orderBy=%22franchise%22&equalTo=%22-Kwb3jHCbRgNR9LdjquV%22")
    Call<JsonObject> getCategories();



    //NAO TESTADAS
}
