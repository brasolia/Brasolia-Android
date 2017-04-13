package br.com.brasolia.homeTabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.brasolia.Connectivity.BSConnection;
import br.com.brasolia.Connectivity.BSRequests;
import br.com.brasolia.Connectivity.BSResponse;
import br.com.brasolia.BSEventActivity;
import br.com.brasolia.R;
import br.com.brasolia.adapters.BSEventsAdapter;
import br.com.brasolia.models.BSEvent;
import br.com.brasolia.util.ItemClickSupport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cayke on 11/04/17.
 */

public class BSEventsFragment extends Fragment{
    Context mContext;

    RecyclerView recyclerView;
    ImageView imageView1, imageView2, imageView3;
    RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3;

    List<BSEvent> events;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events_list, container, false);
        mContext = rootView.getContext();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_events_list_recycler);
        imageView1 = (ImageView) rootView.findViewById(R.id.fragment_events_list_bottom_imageview_1);
        imageView2 = (ImageView) rootView.findViewById(R.id.fragment_events_list_bottom_imageview_2);
        imageView3 = (ImageView) rootView.findViewById(R.id.fragment_events_list_bottom_imageview_3);
        relativeLayout1 = (RelativeLayout) rootView.findViewById(R.id.fragment_events_list_bottom_button_1);
        relativeLayout2 = (RelativeLayout) rootView.findViewById(R.id.fragment_events_list_bottom_button_2);
        relativeLayout3 = (RelativeLayout) rootView.findViewById(R.id.fragment_events_list_bottom_button_3);

        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedMenu(1);
            }
        });

        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedMenu(2);
            }
        });

        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedMenu(3);
            }
        });

        setSelectedMenu(1);

        getEvents();

        return rootView;
    }


    private void setSelectedMenu(int selectedMenu) {
        //HORARIO
        if (selectedMenu == 1) {
            imageView1.setImageResource(R.drawable.menu1);
            relativeLayout1.setBackgroundResource(R.color.black);
        }
        else {
            imageView1.setImageResource(R.drawable.selectedmenu1);
            relativeLayout1.setBackgroundResource(R.color.white);
        }

        //LOCALIZAÇÃO
        if (selectedMenu == 2) {
            imageView2.setImageResource(R.drawable.menu2);
            relativeLayout2.setBackgroundResource(R.color.black);
        }
        else {
            imageView2.setImageResource(R.drawable.selectedmenu2);
            relativeLayout2.setBackgroundResource(R.color.white);
        }

        //PREÇO
        if (selectedMenu == 3) {
            imageView3.setImageResource(R.drawable.menu4);
            relativeLayout3.setBackgroundResource(R.color.black);
        }
        else {
            imageView3.setImageResource(R.drawable.selectedmenu4);
            relativeLayout3.setBackgroundResource(R.color.white);
        }

        //todo fazer a logica necessaria para as celulas
    }

    private void getEvents() {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.getEvents();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) bsResponse.getData();

                        events = new ArrayList<BSEvent>();
                        for (Map<String, Object> dictionary : data) {
                            events.add(new BSEvent(dictionary));
                        }

                        mountRecycler();

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

    private void mountRecycler() {
        if (events != null) {
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new BSEventsAdapter(events));

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    Intent i = new Intent(getActivity(), BSEventActivity.class);
                    i.putExtra("event", events.get(position));
                    startActivity(i);
                }
            });
        }
    }
}
