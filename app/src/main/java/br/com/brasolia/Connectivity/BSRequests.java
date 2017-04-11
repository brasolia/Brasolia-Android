package br.com.brasolia.Connectivity;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by cayke on 07/04/17.
 */

public interface BSRequests {
    //***************************TESTADAS***************************
    @FormUrlEncoded
    @POST("user.json")
    Call<JsonObject> register(@Field("email") String email, @Field("facebook_id") String facebook_id, @Field("google_id") String google_id, @Field("fName") String fName, @Field("lName") String lName);

    @GET("event.json")
    Call<JsonObject> getEvents();

    @GET("favoriteevents.json")
    Call<JsonObject> getFavoriteEvents();

    @GET("event.json")
    Call<JsonObject> getEventsByCategory(@Query("filter_categories") String category_id);

    @GET("category.json")
    Call<JsonObject> getCategories();

    @GET("user.json")
    Call<JsonObject> getUserInfo();

    @GET("logout.json")
    Call<JsonObject> logOutUser();

    @FormUrlEncoded
    @POST("login.json")
    Call<JsonObject> logUser(@Field("email") String email, @Field("facebook_id") String facebook_id, @Field("google_id") String google_id, @Field("remember") String remember);

    @FormUrlEncoded
    @POST("message.json")
    Call<JsonObject> makeComment(@Field("event_id") String event_id, @Field("message") String message);

    @GET("message.json")
    Call<JsonObject> getComments(@Query("event_id") String event_id, @Query("page") int page, @Query("N") int numberOfComments);

    @FormUrlEncoded
    @POST("eventliked.json")
    Call<JsonObject> likeEvent(@Field("event_id") String event_id, @Field("liked") boolean liked);

    @GET("eventliked.json")
    Call<JsonObject> getLikeEvent(@Query("event_id") String event_id);

    @GET("search.json")
    Call<JsonObject> searchEvent(@Query("search") String search);

    @GET("presenceconfirm.json")
    Call<JsonObject> getPresence(@Query("event_id") String event_id);

    @FormUrlEncoded
    @POST("presenceconfirm.json")
    Call<JsonObject> confirmPresence(@Field("event_id") String event_id, @Field("confirm") boolean confirm);

    //***************************NAO TESTADAS***************************
    @FormUrlEncoded
    @POST("userimage.json")
    Call<JsonObject> setUserImage(@Field("path") String imagePath);

    //***************************NAO CONSTA NA DOCUMENTACAO***************************
    @FormUrlEncoded
    @POST("pmessage.json")
    Call<JsonObject> sendSugestion(@Field("message") String message);
}
