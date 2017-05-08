package br.com.brasolia.homeTabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import br.com.brasolia.AppActivity;
import br.com.brasolia.BSEventActivity;
import br.com.brasolia.Connectivity.BSConnection;
import br.com.brasolia.Connectivity.BSRequests;
import br.com.brasolia.Connectivity.BSResponse;
import br.com.brasolia.R;
import br.com.brasolia.adapters.BSEventsAdapter;
import br.com.brasolia.models.BSCategory;
import br.com.brasolia.models.BSEvent;
import br.com.brasolia.util.FragmentDataAndConnectionHandler;
import br.com.brasolia.util.ItemClickSupport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cayke on 11/04/17.
 */

public class BSEventsFragment extends Fragment {
    AppActivity mContext;

    RecyclerView recyclerView;
    ImageView imageView1, imageView2, imageView3;
    RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3;

    Call<JsonObject> call;
    List<BSEvent> events;
    BSCategory filterCategory;
    int selectedFilter = 1;

    private FragmentDataAndConnectionHandler dataAndConnectionHandler;
    private boolean isLoading = false;

    @Override
    public void onDestroy() {
        if (call != null)
            call.cancel();

        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null){
            filterCategory = bundle.getParcelable("category");
        }

        getEventsFromCategory(filterCategory);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events_list, container, false);

        Context context = rootView.getContext();
        if (context instanceof AppActivity)
            mContext = (AppActivity) context;

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

        //-------------------- INIT CONNECTION AND EMPTY DATA HANDLER -------------------------------
        dataAndConnectionHandler = new FragmentDataAndConnectionHandler(inflater, container, rootView);
        dataAndConnectionHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEventsFromCategory(filterCategory);
                dataAndConnectionHandler.showLoadingLayout();
            }
        });
        dataAndConnectionHandler.setLoadingMessage("Carregando eventos...");
        //----------------------------------------------------------------------------------

        setSelectedMenu(selectedFilter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isLoading) {
            dataAndConnectionHandler.showLoadingLayout();
        }
    }


    private void setSelectedMenu(int selectedMenu) {
        selectedFilter = selectedMenu;

        //HORARIO
        if (selectedMenu == 1) {
            imageView1.setImageResource(R.drawable.menu1);
            relativeLayout1.setBackgroundResource(R.color.black);
        } else {
            imageView1.setImageResource(R.drawable.selectedmenu1);
            relativeLayout1.setBackgroundResource(R.color.white);
        }

        //LOCALIZAÇÃO
        if (selectedMenu == 2) {
            Picasso.with(getContext()).load(R.drawable.ic_distance_white).into(imageView2);
            relativeLayout2.setBackgroundResource(R.color.black);
        } else {
            Picasso.with(getContext()).load(R.drawable.ic_distance_black).into(imageView2);
            relativeLayout2.setBackgroundResource(R.color.white);
        }

        //PREÇO
        if (selectedMenu == 3) {
            imageView3.setImageResource(R.drawable.menu4);
            relativeLayout3.setBackgroundResource(R.color.black);
        } else {
            imageView3.setImageResource(R.drawable.selectedmenu4);
            relativeLayout3.setBackgroundResource(R.color.white);
        }

        mountRecycler(selectedFilter);
    }

    private void getEventsFromCategory(BSCategory category) {
        isLoading = true;

        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.getEventsByCategory(category.getId());

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                isLoading = false;

                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) bsResponse.getData();

                        events = new ArrayList<>();
                        for (Map<String, Object> dictionary : data) {
                            events.add(new BSEvent(dictionary));
                        }

                        mountRecycler(selectedFilter);
                        dataAndConnectionHandler.showMainView();
                    } else {
                        dataAndConnectionHandler.showExceptionLayout();
                    }
                } else {
                    dataAndConnectionHandler.showExceptionLayout();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                isLoading = false;

                dataAndConnectionHandler.showInternetOffLayout();
            }
        });
    }

    private void mountRecycler(int choice) {
        if (events != null) {

            switch (choice) {

                case 1:
                    Collections.sort(events, new Comparator<BSEvent>() {
                        @Override
                        public int compare(BSEvent o1, BSEvent o2) {
                            return Long.valueOf(o1.getStartHour().getTime()).compareTo(o2.getStartHour().getTime());
                        }
                    });
                    break;

                case 2:
                    Collections.sort(events, new Comparator<BSEvent>() {
                        public int compare(BSEvent obj1, BSEvent obj2) {
                            return Double.valueOf(obj1.getDistance()).compareTo(Double.valueOf(obj2.getDistance()));
                        }
                    });
                    break;

                case 3 :
                    Collections.sort(events, new Comparator<BSEvent>() {
                        public int compare(BSEvent obj1, BSEvent obj2) {
                            return Double.valueOf(obj1.getPrices().get(0).getPrice()).compareTo(Double.valueOf(obj2.getPrices().get(0).getPrice()
                            ));
                        }
                    });
                    break;

            }

            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new BSEventsAdapter(events, choice));

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