package br.com.brasolia.models;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

import br.com.brasolia.Connectivity.BSConnection;
import br.com.brasolia.Connectivity.BSRequests;
import br.com.brasolia.Connectivity.BSResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cayke on 07/04/17.
 */

public class BSTest {
    public static void init() {
        getCategories();
        //getEvents();
    }

    private static void getCategories() {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.getCategories();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) bsResponse.getData();

                        ArrayList<BSCategory> array = new ArrayList<BSCategory>();
                        for (Map<String, Object> dictionary : data) {
                            BSCategory category = new BSCategory(dictionary);
                            array.add(category);
                        }

                    }
                    else {
                        //todo erro de servidor
                        Log.d("getEvents", "server error");
                    }
                }
                else {
                    //todo print erro de conexao
                    Log.d("getEvents", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //todo print erro de conexao
                Log.d("getEvents", "conection failure");
            }
        });
    }

    private static void getEvents() {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.getEvents();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        //todo pegar o dado


//                        ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) ibResponse.getData();
//
//                        ArrayList<IBStore> array = new ArrayList<IBStore>();
//                        for (Map<String, Object> dictionary : data) {
//                            IBStore store = new IBStore(dictionary);
//                            array.add(store);
//                        }

                    }
                    else {
                        //todo erro de servidor
                        Log.d("getEvents", "server error");
                    }
                }
                else {
                    //todo print erro de conexao
                    Log.d("getEvents", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //todo print erro de conexao
                Log.d("getEvents", "conection failure");
            }
        });
    }
}
