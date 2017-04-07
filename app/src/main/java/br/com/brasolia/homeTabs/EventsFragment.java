package br.com.brasolia.homeTabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import br.com.brasolia.EventActivity;
import br.com.brasolia.R;
import br.com.brasolia.adapters.CustomListAdapter;
import br.com.brasolia.application.BrasoliaApplication;
import br.com.brasolia.eventbus.UpdateLike;
import br.com.brasolia.models.Category;
import br.com.brasolia.models.User;
import br.com.brasolia.webserver.BrasoliaAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Matheus on 15/07/2016.
 */
public class EventsFragment extends Fragment {

    private ImageButton imgFilter1, imgFilter2, imgFilter4;
    private View rootView;
    private TextView tvStatus;
    private LinearLayout ll;
    private BrasoliaAPI api;
    private CustomListAdapter adapter;
    private ListView list;
    private JsonArray data = new JsonArray(), cast;
    public static JsonArray allCast;
    static Double latitude;
    static Double longitude;
    private SharedPreferences sp;
    private User user;
    private JsonObject _currentEvent = null;
    private Category lastCategory;

    public EventsFragment() {
    }

    public static EventsFragment newInstance() {
        EventsFragment fragment = new EventsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getContext());

        rootView = inflater.inflate(R.layout.fragment_events, container, false);

//        allData = new JsonArray();

        // SCREEN ELEMENTS -------------------------------------------------------------------------
        imgFilter1 = (ImageButton) rootView.findViewById(R.id.imgEventsBarFilter1);
        imgFilter2 = (ImageButton) rootView.findViewById(R.id.imgEventsBarFilter2);
        imgFilter4 = (ImageButton) rootView.findViewById(R.id.imgEventsBarFilter4);
        tvStatus = (TextView) rootView.findViewById(R.id.tvStatusEvents);
        list = (ListView) rootView.findViewById(R.id.list);
        ll = (LinearLayout) rootView.findViewById(R.id.linearLayout);
        // -----------------------------------------------------------------------------------------

        user = User.getUser(getActivity());

        sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        latitude = Double.valueOf(sp.getString("latitude", "-1.0"));
        longitude = Double.valueOf(sp.getString("longitude", "-1.0"));

        tvStatus.setVisibility(View.GONE);

        list.setOnTouchListener(new AbsListView.OnTouchListener() {
            float initialY, finalY;
            boolean isScrollingUp;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);

                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        initialY = event.getY();
                    case (MotionEvent.ACTION_UP):
                        finalY = event.getY();

                        if (initialY < finalY) {
                            ll.setVisibility(View.VISIBLE);

                            isScrollingUp = true;
                        } else if (initialY > finalY) {
                            ll.setVisibility(View.GONE);
                            isScrollingUp = false;
                        }
                    default:
                }

                if (isScrollingUp) {
                    // do animation for scrolling up
                } else {
                    // do animation for scrolling down
                }

                return false; // has to be false, or it will freeze the listView
            }
        });

        getAllEvents();

        imgFilter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Time filter
                imgFilter1.setImageResource(R.drawable.selectedmenu1);
                imgFilter2.setImageResource(R.drawable.menu2);
                imgFilter4.setImageResource(R.drawable.menu4);

                if (data.size() != 0) {

                    list.smoothScrollToPosition(0);

                    data = sortJsonArrayDate(data);
                    adapter.setPositionFilter(0);
                    adapter.setData(data);
                    adapter.notifyDataSetChanged();

                }
            }
        });

        imgFilter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Distance filter
                imgFilter1.setImageResource(R.drawable.menu1);
                imgFilter2.setImageResource(R.drawable.selectedmenu2);
                imgFilter4.setImageResource(R.drawable.menu4);

                if (data.size() != 0) {

                    list.smoothScrollToPosition(0);

                    data = sortJsonArrayDistance(data);
                    adapter.setPositionFilter(1);
                    adapter.setData(data);
                    adapter.notifyDataSetChanged();

                } else
                    Toast.makeText(getContext(), "Verifique sua conexão.", Toast.LENGTH_LONG).show();


            }
        });

        imgFilter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // price filter
                imgFilter1.setImageResource(R.drawable.menu1);
                imgFilter2.setImageResource(R.drawable.menu2);
                imgFilter4.setImageResource(R.drawable.selectedmenu4);

                if (data.size() != 0) {

                    list.smoothScrollToPosition(0);

                    data = sortJsonArrayPrice(data);
                    adapter.setPositionFilter(2);
                    adapter.setData(data);
                    adapter.notifyDataSetChanged();

                } else
                    Toast.makeText(getContext(), "Verifique sua conexão.", Toast.LENGTH_LONG).show();

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(getActivity(), EventActivity.class);
                i.putExtra("event", cast.get(data.get(position).getAsJsonObject().get("index").getAsInt()).toString());
                startActivity(i);

            }
        });

        EventBus.getDefault().register(this);

        return rootView;
    }

    private void getAllEvents() {
        api = ((BrasoliaApplication) getActivity().getApplication()).getApi();

        Call<JsonObject> call = api.getEvents();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject resp = response.body();
                } else {

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });

        api
                .getEvents()
                .enqueue(new Callback<JsonObject>() {
                             @Override
                             public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                 if (response.isSuccessful()) {
                                     data = new JsonArray();
                                     cast = response.body().getAsJsonArray("data");
                                     allCast = response.body().getAsJsonArray("data");

                                     SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yy HH:mm");

                                     Date currentDate = new Date();
                                     JsonArray castDates = new JsonArray();
                                     for (int i = 0; i < cast.size(); i++) {
                                         Calendar calendar1 = Calendar.getInstance();
                                         long date1 = cast.get(i).getAsJsonObject().get("dates").getAsJsonArray().get(0).getAsJsonObject().get("datehour_init").getAsJsonObject().get("$date").getAsLong();
                                         calendar1.setTimeInMillis(date1);

                                         Calendar calendar2 = Calendar.getInstance();
                                         long date2 = cast.get(i).getAsJsonObject().get("dates").getAsJsonArray().get(0).getAsJsonObject().get("datehour_end").getAsJsonObject().get("$date").getAsLong();
                                         calendar2.setTimeInMillis(date2);

                                         if (calendar2.getTime().after(currentDate)) {
                                             castDates.add(cast.get(i));
                                         }
                                     }
                                     cast = castDates;
                                     allCast = castDates;

                                     for (int i = 0; i < cast.size(); i++) {
                                         JsonObject actualValue = cast.get(i).getAsJsonObject();
                                         _currentEvent = new JsonObject();
                                         _currentEvent.addProperty("id", actualValue.get("id").getAsJsonObject().get("$oid").getAsString());
                                         _currentEvent.addProperty("title", actualValue.get("name").getAsString());
                                         _currentEvent.addProperty("addressTitle", actualValue.get("locality").getAsJsonObject().get("title").getAsString());
                                         _currentEvent.addProperty("cover", "https://s3-us-west-2.amazonaws.com/bs.cover/" + actualValue.get("cover").getAsString());
                                         _currentEvent.addProperty("latitude", actualValue.get("locality").getAsJsonObject().get("point").getAsJsonObject().get("coordinates").getAsJsonArray().get(1).getAsDouble());
                                         _currentEvent.addProperty("longitude", actualValue.get("locality").getAsJsonObject().get("point").getAsJsonObject().get("coordinates").getAsJsonArray().get(0).getAsDouble());
                                         _currentEvent.addProperty("index", i);

                                         SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
                                         // Create a calendar object that will convert the date and time value in milliseconds to date.
                                         Calendar calendar = Calendar.getInstance();
                                         calendar.setTimeInMillis(actualValue.get("dates").getAsJsonArray().get(0).getAsJsonObject().get("datehour_init").getAsJsonObject().get("$date").getAsLong());
                                         _currentEvent.addProperty("eventDate", formatter.format(calendar.getTime()));
                                         _currentEvent.addProperty("eventDateOriginal", String.valueOf(actualValue.get("dates").getAsJsonArray().get(0).getAsJsonObject().get("datehour_init").getAsJsonObject().get("$date").getAsLong()));
                                         _currentEvent.addProperty("description", actualValue.get("description").getAsString());

                                         if (actualValue.get("prices") != null && actualValue.get("prices").getAsJsonArray().size() > 0)
                                             _currentEvent.addProperty("price", actualValue.get("prices").getAsJsonArray().get(0).getAsJsonObject()
                                                     .get("value").getAsDouble());
                                         else _currentEvent.addProperty("price", 0.0);

                                         if (actualValue.get("facebook_event_id").getAsString().isEmpty())
                                             _currentEvent.addProperty("eventLikes", 0);
                                         else {
                                             if (AccessToken.getCurrentAccessToken() != null) {
                                                 Log.i("EventsFragment", AccessToken.getCurrentAccessToken().getToken() + "|" + actualValue.get("facebook_event_id").getAsString());

                                                 GraphRequest _request = GraphRequest.newGraphPathRequest(
                                                         AccessToken.getCurrentAccessToken(),
                                                         actualValue.get("facebook_event_id").getAsString(),
                                                         new GraphRequest.Callback() {
                                                             @Override
                                                             public void onCompleted(GraphResponse response) {
                                                                 Log.i("EventsFragment", "is null ? " + ((null == response.getJSONObject()) ? "sim" : response.getJSONObject()));

                                                                 _currentEvent.addProperty("eventLikes", response.getJSONObject().optLong("attending_count"));
                                                             }
                                                         }
                                                 );

                                                 Bundle parameters = new Bundle();
                                                 parameters.putString("fields", "attending_count");
                                                 _request.setParameters(parameters);
                                                 _request.executeAsync();
                                             }
                                         }

                                         data.add(_currentEvent);
//                                         allData.add(_currentEvent);
                                     }

                                     data = sortJsonArrayDate(data); // sorted by default by date
                                     adapter = new CustomListAdapter(getActivity(), data);
                                     list.setAdapter(adapter);
                                 } else {
                                     tvStatus.setVisibility(View.VISIBLE);
                                 }
                             }

                             @Override
                             public void onFailure(Call<JsonObject> call, Throwable t) {

                                 tvStatus.setVisibility(View.VISIBLE);
                                 Toast.makeText(getContext(), "Verifique sua conexão.", Toast.LENGTH_SHORT).show();

                                 t.printStackTrace();
                             }
                         }
                );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onReceiveEventBus(UpdateLike updateLike) {
        if (lastCategory != null) {
            loadCategoryEvents(lastCategory);
        } else {
            getAllEvents();
        }
    }

    public void loadCategoryEvents(Category c) {
        lastCategory = c;
        if (c.getId().equals("5833c1fc5157a00685c21eea")) { //FAVORITOS
            Call<JsonObject> getFavorite = api.getFavoriteEvents(sp.getString("cookie", ""));
            getFavorite.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        loadEvents(response);
                    } else {
                        tvStatus.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    tvStatus.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Verifique sua conexão", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Call<JsonObject> eventsCategory = api.getEventsByCategory(c.getId());

            eventsCategory.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        loadEvents(response);
                    } else {
                        tvStatus.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    tvStatus.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Verifique sua conexão", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }
    }

    public void loadEvents(Response<JsonObject> response) {
        data = new JsonArray();

        JsonArray respota = response.body().getAsJsonArray("data");

        Date currentDate = new Date();
        JsonArray castDates = new JsonArray();
        for (int i = 0; i < respota.size(); i++) {
            Calendar calendar1 = Calendar.getInstance();
            long date1 = respota.get(i).getAsJsonObject().get("dates").getAsJsonArray().get(0).getAsJsonObject().get("datehour_init").getAsJsonObject().get("$date").getAsLong();
            calendar1.setTimeInMillis(date1);

            Calendar calendar2 = Calendar.getInstance();
            long date2 = respota.get(i).getAsJsonObject().get("dates").getAsJsonArray().get(0).getAsJsonObject().get("datehour_end").getAsJsonObject().get("$date").getAsLong();
            calendar2.setTimeInMillis(date2);

            if (calendar2.getTime().after(currentDate)) {
                castDates.add(respota.get(i));
            }
        }
        respota = castDates;

        for (int i = 0; i < respota.size(); i++) {
            JsonObject actualValue = respota.get(i).getAsJsonObject();
            _currentEvent = new JsonObject();
            _currentEvent.addProperty("id", actualValue.get("id").getAsJsonObject().get("$oid").getAsString());
            _currentEvent.addProperty("title", actualValue.get("name").getAsString());
            _currentEvent.addProperty("addressTitle", actualValue.get("locality").getAsJsonObject().get("title").getAsString());
            _currentEvent.addProperty("cover", "https://s3-us-west-2.amazonaws.com/bs.cover/" + actualValue.get("cover").getAsString());
            _currentEvent.addProperty("latitude", actualValue.get("locality").getAsJsonObject().get("point").getAsJsonObject().get("coordinates").getAsJsonArray().get(1).getAsDouble());
            _currentEvent.addProperty("longitude", actualValue.get("locality").getAsJsonObject().get("point").getAsJsonObject().get("coordinates").getAsJsonArray().get(0).getAsDouble());
            _currentEvent.addProperty("index", i);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(actualValue.get("dates").getAsJsonArray().get(0).getAsJsonObject().get("datehour_init").getAsJsonObject().get("$date").getAsLong());
            _currentEvent.addProperty("eventDate", formatter.format(calendar.getTime()));
            _currentEvent.addProperty("eventDateOriginal", String.valueOf(actualValue.get("dates").getAsJsonArray().get(0).getAsJsonObject().get("datehour_init").getAsJsonObject().get("$date").getAsLong()));
            _currentEvent.addProperty("description", actualValue.get("description").getAsString());

            if (actualValue.get("prices") != null && actualValue.get("prices").getAsJsonArray().size() > 0)
                _currentEvent.addProperty("price", actualValue.get("prices").getAsJsonArray().get(0).getAsJsonObject()
                        .get("value").getAsDouble());
            else _currentEvent.addProperty("price", 0.0);

            if (actualValue.get("facebook_event_id").getAsString().isEmpty())
                _currentEvent.addProperty("eventLikes", 0);
            else {
                if (AccessToken.getCurrentAccessToken() != null) {
                    Log.i("EventsFragment", AccessToken.getCurrentAccessToken().getToken() + "|" + actualValue.get("facebook_event_id").getAsString());

                    GraphRequest _request = GraphRequest.newGraphPathRequest(
                            AccessToken.getCurrentAccessToken(),
                            actualValue.get("facebook_event_id").getAsString(),
                            new GraphRequest.Callback() {
                                @Override
                                public void onCompleted(GraphResponse response) {
                                    Log.i("EventsFragment", "is null ? " + ((null == response.getJSONObject()) ? "sim" : response.getJSONObject()));

                                    _currentEvent.addProperty("eventLikes", response.getJSONObject().optLong("attending_count"));
                                }
                            }
                    );

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "attending_count");
                    _request.setParameters(parameters);
                    _request.executeAsync();
                }
            }

            data.add(_currentEvent);

        }

        if (data.size() == 0) tvStatus.setVisibility(View.VISIBLE);
        else tvStatus.setVisibility(View.GONE);

        cast = respota;

        adapter.setData(data);
        adapter.notifyDataSetChanged();

        imgFilter1.performClick();
    }

    // SORT BY DATE -----------------------------------------------------------
    public static JsonArray sortJsonArrayDate(JsonArray array) {
        List<JsonObject> jsons = new ArrayList<JsonObject>();
        for (int i = 0; i < array.size(); i++) {
            jsons.add(array.get(i).getAsJsonObject());
        }
        Collections.sort(jsons, new Comparator<JsonObject>() {
            @Override
            public int compare(JsonObject a, JsonObject b) {
                String dayA = null;

                try {
                    dayA = a.get("eventDateOriginal").getAsString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String dayB = null;
                try {
                    dayB = b.get("eventDateOriginal").getAsString();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                // Here you could parse string id to integer and then compare.
                if (dayB == null && dayA == null) return 0;
                return dayA.compareTo(dayB);
            }
        });

        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(jsons).getAsJsonArray();

        return jsonArray;


    }

    // SORT BY DISTANCE -----------------------------------------------------------
    public static JsonArray sortJsonArrayDistance(JsonArray array) {

        List<JsonObject> jsons = new ArrayList<JsonObject>();
        for (int i = 0; i < array.size(); i++) {
            jsons.add(array.get(i).getAsJsonObject());
        }
        Collections.sort(jsons, new Comparator<JsonObject>() {
            @Override
            public int compare(JsonObject a, JsonObject b) {

                Location locationUser = new Location("User");
                locationUser.setLatitude(latitude);
                locationUser.setLongitude(longitude);

                Location locationA = new Location("A");
                Location locationB = new Location("B");

                locationA.setLatitude(a.get("latitude").getAsDouble());
                locationA.setLongitude(a.get("longitude").getAsDouble());

                locationB.setLatitude(b.get("latitude").getAsDouble());
                locationB.setLongitude(b.get("longitude").getAsDouble());

                float distanceA, distanceB;

                distanceA = locationA.distanceTo(locationUser);
                distanceB = locationB.distanceTo(locationUser);


                if (distanceA < distanceB) return -1;
                if (distanceA > distanceB) return 1;
                return 0;
            }
        });

        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(jsons).getAsJsonArray();


        return jsonArray;
    }

    // SORT BY PRICE -----------------------------------------------------------
    public static JsonArray sortJsonArrayPrice(JsonArray array) {

        List<JsonObject> jsons = new ArrayList<JsonObject>();
        for (int i = 0; i < array.size(); i++) {
            jsons.add(array.get(i).getAsJsonObject());
        }
        Collections.sort(jsons, new Comparator<JsonObject>() {
            @Override
            public int compare(JsonObject a, JsonObject b) {
                Double priceA = null;

                try {
                    priceA = a.get("price").getAsDouble();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Double priceB = null;
                try {
                    priceB = b.get("price").getAsDouble();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (priceA < priceB) return -1;
                if (priceA > priceB) return 1;
                return 0;
            }
        });

        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(jsons).getAsJsonArray();


        return jsonArray;
    }

    public void onResume() {
        super.onResume();
        user = User.getUser(getActivity());
    }
}
