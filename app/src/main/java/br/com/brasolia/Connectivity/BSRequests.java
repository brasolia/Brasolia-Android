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
    @FormUrlEncoded
    @POST("user.json")
    Call<JsonObject> register(@Field("email") String email, @Field("facebook_id") String facebook_id, @Field("fName") String fName, @Field("lName") String lName);

    @FormUrlEncoded
    @POST("user.json")
    Call<JsonObject> registerGoogle(@Field("email") String email, @Field("google_id") String google_id, @Field("fName") String fName, @Field("lName") String lName);

    @FormUrlEncoded
    @POST("login.json")
    Call<JsonObject> login(@Field("email") String email, @Field("facebook_id") String facebook_id, @Field("remember") String remember);

    @FormUrlEncoded
    @POST("login.json")
    Call<JsonObject> loginGoogle(@Field("email") String email, @Field("google_id") String google_id, @Field("remember") String remember);

    @FormUrlEncoded
    @POST("message.json")
    Call<JsonObject> makeComment(@Field("event_id") String event_id, @Field("message") String message);

    @FormUrlEncoded
    @POST("eventliked.json")
    Call<JsonObject> likeEventService(@Field("event_id") String event_id, @Field("liked") String liked);

    @FormUrlEncoded
    @POST("pmessage.json")
    Call<JsonObject> sendSugestion(@Field("message") String message);

    @GET("eventliked.json")
    Call<JsonObject> getLikeEvent(@Query("event_id") String event_id);

    @GET("category.json")
    Call<JsonObject> getCategories();

    @GET("message.json")
    Call<JsonObject> getComments(@Query("event_id") String event_id, @Query("page") String page, @Query("N") String n);

    @GET("event.json")
    Call<JsonObject> getEvents();

    @GET("event.json")
    Call<JsonObject> getEventsByCategory(@Query("filter_categories") String category_id);

    @GET("favoriteevents.json")
    Call<JsonObject> getFavoriteEvents();
}
