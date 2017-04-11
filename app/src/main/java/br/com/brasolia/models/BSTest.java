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
        //getCategories();
        getEvents();
        //logUser();
        //registerUser();
        //getUserInfo();
        //logoutUser();
        //makeComment("comentario muito louco");
        //getComments();
        //setUserImage(); //nao funciona
        //likeEvent(true);
        //getEventLiked();
        //getFavoriteEvents();
        //search("supremo");
        //getEventsForCategory();
        //getPresence();
        //confirmPresence(true);
    }

    private static void confirmPresence(boolean confirm) {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.confirmPresence("58e8fa9a5157a057e69ae0e0", confirm);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        Log.d("confirmPresence", "success");
                    }
                    else {
                        Log.d("confirmPresence", "server error");
                    }
                }
                else {
                    Log.d("confirmPresence", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("confirmPresence", "conection failure");
            }
        });
    }
    private static void getPresence() {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.getPresence("58e8fa9a5157a057e69ae0e0");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        Log.d("getPresence", "success");
                    }
                    else {
                        Log.d("getPresence", "server error");
                    }
                }
                else {
                    Log.d("getPresence", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("getPresence", "conection failure");
            }
        });
    }

    private static void getEventsForCategory() {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.getEventsByCategory("57c048bd5157a02bd10df148");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) bsResponse.getData();

                        ArrayList<BSEvent> array = new ArrayList<BSEvent>();
                        for (Map<String, Object> dictionary : data) {
                            array.add(new BSEvent(dictionary));
                        }

                        Log.d("getEventsForCategory", "success");
                    }
                    else {
                        Log.d("getEventsForCategory", "server error");
                    }
                }
                else {
                    Log.d("getEventsForCategory", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("getEventsForCategory", "conection failure");
            }
        });
    }
    private static void search(String search ) {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.searchEvent(search);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) bsResponse.getData();

                        ArrayList<BSEvent> array = new ArrayList<BSEvent>();
                        for (Map<String, Object> dictionary : data) {
                            array.add(new BSEvent(dictionary));
                        }

                        Log.d("search", "success");
                    }
                    else {
                        Log.d("search", "server error");
                    }
                }
                else {
                    Log.d("search", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("search", "conection failure");
            }
        });
    }

    private static void getFavoriteEvents() {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.getFavoriteEvents();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) bsResponse.getData();

                        ArrayList<BSEvent> array = new ArrayList<BSEvent>();
                        for (Map<String, Object> dictionary : data) {
                            array.add(new BSEvent(dictionary));
                        }

                        Log.d("getFavoriteEvents", "success");
                    }
                    else {
                        Log.d("getFavoriteEvents", "server error");
                    }
                }
                else {
                    Log.d("getFavoriteEvents", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("getFavoriteEvents", "conection failure");
            }
        });
    }

    private static void getEventLiked() {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.getLikeEvent("58e8fa9a5157a057e69ae0e0");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        Log.d("getEventLiked", "success");
                    }
                    else {
                        Log.d("getEventLiked", "server error");
                    }
                }
                else {
                    Log.d("getEventLiked", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("getEventLiked", "conection failure");
            }
        });
    }

    private static void likeEvent(boolean liked) {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.likeEvent("58e8fa9a5157a057e69ae0e0", liked);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        Log.d("likeEvent", "success");
                    }
                    else {
                        Log.d("likeEvent", "server error");
                    }
                }
                else {
                    Log.d("likeEvent", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("likeEvent", "conection failure");
            }
        });
    }

    private static void setUserImage() {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.setUserImage("https://scontent.fbsb4-1.fna.fbcdn.net/v/t1.0-9/17308748_1279495765466119_5902661537556998459_n.jpg?oh=aa59c13f043a6e9adf2fe3fec29cbb8c&oe=59545D7D");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        Log.d("setUserImage", "success");
                    }
                    else {
                        Log.d("setUserImage", "server error");
                    }
                }
                else {
                    Log.d("setUserImage", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("setUserImage", "conection failure");
            }
        });
    }

    private static void getComments() {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.getComments("58e8fa9a5157a057e69ae0e0", 1, 100);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) bsResponse.getData();

                        ArrayList<BSComment> array = new ArrayList<BSComment>();
                        for (Map<String, Object> dictionary : data) {
                            array.add(new BSComment(dictionary));
                        }

                        Log.d("getComments", "success");
                    }
                    else {
                        Log.d("getComments", "server error");
                    }
                }
                else {
                    Log.d("getComments", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("getComments", "conection failure");
            }
        });
    }

    private static void makeComment(String comment) {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.makeComment("58e8fa9a5157a057e69ae0e0", comment);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {

                        Log.d("makeComment", "success");
                    }
                    else {
                        Log.d("makeComment", "server error");
                    }
                }
                else {
                    Log.d("makeComment", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("makeComment", "conection failure");
            }
        });
    }

    private static void logoutUser() {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.logOutUser();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {

                        Log.d("logoutUser", "sucess");
                    }
                    else {
                        Log.d("logoutUser", "server error");
                    }
                }
                else {
                    Log.d("logoutUser", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("logoutUser", "conection failure");
            }
        });
    }

    private static void getUserInfo() {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.getUserInfo();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {

                        Log.d("getUserInfo", "sucess");
                    }
                    else {
                        Log.d("getUserInfo", "server error");
                    }
                }
                else {
                    Log.d("getUserInfo", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("getUserInfo", "conection failure");
            }
        });
    }

    private static void registerUser() {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.register("cayke10@gmail.com", "906378792777820", null, "Cayke", "Prudente");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        Map<String, Object> data = (Map<String, Object>) bsResponse.getData();

                        BSUser user = new BSUser(data);
                        user.saveUser();

                        Log.d("registerUser", "sucess");
                    }
                    else {
                        Log.d("registerUser", "server error");
                    }
                }
                else {
                    Log.d("registerUser", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("registerUser", "conection failure");
            }
        });
    }

    private static void logUser() {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.logUser("cayke10@gmail.com", "906378792777820", null, "on");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {

                        Map<String, Object> data = (Map<String, Object>) bsResponse.getData();

                        BSUser user = new BSUser(data);
                        user.saveUser();

                        Log.d("logUser", "sucess");
                    }
                    else {
                        //todo erro de servidor
                        Log.d("logUser", "server error");
                    }
                }
                else {
                    //todo print erro de conexao
                    Log.d("logUser", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //todo print erro de conexao
                Log.d("logUser", "conection failure");
            }
        });
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

                        Log.d("getCategories", "success");
                    }
                    else {
                        Log.d("getCategories", "server error");
                    }
                }
                else {
                    Log.d("getCategories", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("getCategories", "conection failure");
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
                        ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) bsResponse.getData();

                        ArrayList<BSEvent> array = new ArrayList<BSEvent>();
                        for (Map<String, Object> dictionary : data) {
                            array.add(new BSEvent(dictionary));
                        }

                        Log.d("getEvents", "success");
                    }
                    else {
                        Log.d("getEvents", "server error");
                    }
                }
                else {
                    Log.d("getEvents", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("getEvents", "conection failure");
            }
        });
    }
}