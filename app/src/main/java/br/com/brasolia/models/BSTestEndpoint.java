package br.com.brasolia.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.brasolia.Connectivity.BSConnection;
import br.com.brasolia.Connectivity.BSEndpoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cayke on 20/10/17.
 */

public class BSTestEndpoint {
    public static void test(){
        //getCategories();
        getItems("-Kwb3wV0GB95EGLfdjzW");
    }

    private static void getCategories() {
        BSEndpoint endpoint = BSConnection.createService(BSEndpoint.class);
        Call<JsonObject> call = endpoint.getCategories();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Object responseObject = new Gson().fromJson(response.body(), Object.class);

                    try {
                        Map<String, Object> result = (Map<String, Object>) responseObject;
                        List<BSCategory> categories = new ArrayList<BSCategory>();

                        Iterator it = result.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            categories.add(new BSCategory((String) pair.getKey(), (Map<String, Object>) pair.getValue()));
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                    }
                    catch (Exception e) {
                        Log.d("getCategories", e.toString());
                    }
                }
                else {
                    Log.d("getCategories", "request error");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("getCategories", "conection failure");
            }
        });
    }

    private static void getItems(String categoryID) {
        BSEndpoint endpoint = BSConnection.createService(BSEndpoint.class);
        Call<JsonObject> call = endpoint.getItemsFromCategory("cat" + categoryID);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Object responseObject = new Gson().fromJson(response.body(), Object.class);

                    try {
                        Map<String, Object> result = (Map<String, Object>) responseObject;
                        List<BSItem> items = new ArrayList<>();

                        Iterator it = result.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if (((Map<String, Object>) pair.getValue()).get("type").equals("venue"))
                                items.add(new BSVenue((String) pair.getKey(), (Map<String, Object>) pair.getValue()));
                            else {
                                //todo
                            }

                            it.remove(); // avoids a ConcurrentModificationException
                        }
                    }
                    catch (Exception e) {
                        Log.d("getCategories", e.toString());
                    }
                }
                else {
                    Log.d("getCategories", "request error");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("getCategories", "conection failure");
            }
        });
    }
}
