package br.com.brasolia.Connectivity;

import com.google.gson.JsonObject;

import java.util.List;

import br.com.brasolia.models.NewBSEvent;
import br.com.brasolia.models.NewBSImage;
import br.com.brasolia.models.NewBSVenue;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
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



    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////// NOVAS REQUESTS - BRASOLIA 2.0 /////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //GET ALL EVENTS
    @GET ("api/BSB/events")
    Call<List<NewBSEvent>> getAllEvents();

    //GET EVENT BY ID
    @GET ("api/BSB/events/{event_id}")
    Call<List<NewBSEvent>> getEventById(@Path("event_id") int event_id);

    //----------------------------------------------------------------------------------------------
    //GET ALL VENUES
    @GET ("api/BSB/venues")
    Call<List<NewBSVenue>> getAllVenues();

    //GET VENUE BY ID
    @GET ("api/BSB/venues/{venue_id}")
    Call<List<NewBSVenue>> getVenueById(@Path("venue_id") int venue_id);

    //----------------------------------------------------------------------------------------------

    //GET ALL CITIES
    @GET ("api/cities")
    Call<List<NewBSEvent>> getAllCities();

    //GET CITY BY ID
    @GET ("api/cities/{city_id}")
    Call<List<NewBSEvent>> getCityById(@Path("city_id") int city_id);

    //----------------------------------------------------------------------------------------------

    //GET ALL CATEGORY TYPES
    @GET ("api/category-types")
    Call<List<NewBSEvent>> getAllCategoryTypes();

    //GET CATEGORY TYPES BY ID
    @GET ("api/category-types/{categoryTypes_id}")
    Call<List<NewBSEvent>> getCategoryTypeById(@Path("categoryTypes_id") int categoryTypes_id);

    //----------------------------------------------------------------------------------------------

    //GET ALL IMAGES
    @GET ("api/images")
    Call<List<NewBSImage>> getAllImages();

    //GET IMAGE BY ID
    @GET ("api/images/{image_id}")
    Call<List<NewBSImage>> getImageById(@Path("image_id") int image_id);

    //----------------------------------------------------------------------------------------------

    //LOGIN
    @POST ("login")
    Call<JsonObject> doLogin (@Header("Authorization") String authorization);

    //USER (ME)
    @GET("api/BSB/user")
    Call<String> getUser(@Header("Authorization") String authorization);

    //----------------------------------------------------------------------------------------------
}
