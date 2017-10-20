package br.com.brasolia.Connectivity;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by cayke on 20/10/17.
 */

public interface BSEndpoint {

    //TESTADAS

    @GET("categories.json?print=pretty&orderBy=%22franchise%22&equalTo=%22-Kwb3jHCbRgNR9LdjquV%22")
    Call<JsonObject> getCategories();

    @GET("https://cultura-mobile.firebaseio.com/items.json?print=pretty&equalTo=true")
    Call<JsonObject> getItemsFromCategory(@Query("orderBy") String categoryID);

    //NAO TESTADAS
}
