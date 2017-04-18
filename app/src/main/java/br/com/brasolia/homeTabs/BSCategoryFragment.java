package br.com.brasolia.homeTabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import br.com.brasolia.Connectivity.BSConnection;
import br.com.brasolia.Connectivity.BSRequests;
import br.com.brasolia.Connectivity.BSResponse;
import br.com.brasolia.MainActivity;
import br.com.brasolia.R;
import br.com.brasolia.SearchEventsActivity;
import br.com.brasolia.adapters.BSCategoriesAdapter;
import br.com.brasolia.models.BSCategory;
import br.com.brasolia.util.ItemClickSupport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cayke on 11/04/17.
 */

public class BSCategoryFragment extends Fragment {
    private static final int DEFAULT_SPAN_COUNT = 2;

    MainActivity mainActivity;
    RecyclerView recyclerView;
    private Button btSearch;
    private Button btProfile;

    private ProgressDialog progress;
    private boolean isLoading = false;

    Call<JsonObject> call;
    List<BSCategory> categories;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getCategories();
    }

    @Override
    public void onDestroy() {
        call.cancel();

        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        btSearch.setVisibility(View.VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        Context context = rootView.getContext();

        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }

        //SCREEN ELEMENTS -----------------------------------------------------------------
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerCategories);
        btSearch = (Button) rootView.findViewById(R.id.fragment_categories_search);
        btProfile = (Button) rootView.findViewById(R.id.fragment_categories_config);
        // --------------------------------------------------------------------------------

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), SearchEventsActivity.class);
                startActivity(it);
            }
        });

        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mainActivity.scrollToIndex(2);
            }
        });


        mountRecycler();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isLoading)
            progress = ProgressDialog.show(mainActivity, "", "Carregando dados...");
    }

    private void getCategories() {
        isLoading = true;

        BSRequests requests = BSConnection.createService(BSRequests.class);
        call = requests.getCategories();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progress.dismiss();
                isLoading = false;
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) bsResponse.getData();

                        categories = new ArrayList<BSCategory>();
                        for (Map<String, Object> dictionary : data) {
                            BSCategory category = new BSCategory(dictionary);
                            categories.add(category);
                        }

                        Collections.sort(categories, new Comparator<BSCategory>() {
                            @Override
                            public int compare(BSCategory category, BSCategory t1) {
                                return category.getOrder() - t1.getOrder();
                            }
                        });

                        mountRecycler();
                        Log.d("getCategories", "success");
                    }
                    else {
                        Log.d("getCategories", "server error");
                        Toast.makeText(mainActivity, "Erro ao obter categorias. \n" + bsResponse.getData().toString(), Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(mainActivity, "Erro ao obter categorias. Verifique sua conexao com a internet", Toast.LENGTH_LONG).show();
                    Log.d("getCategories", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                isLoading = false;
                progress.dismiss();
                Log.d("getCategories", "conection failure");
                Toast.makeText(mainActivity, "Erro ao obter categorias. Verifique sua conexao com a internet", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mountRecycler() {
        if (categories != null) {
            recyclerView.setHasFixedSize(true);
            GridLayoutManager layoutManager = new GridLayoutManager(mainActivity, DEFAULT_SPAN_COUNT);
            BSCategoriesAdapter adapter = new BSCategoriesAdapter(categories);
            adapter.setLayoutManager(layoutManager, DEFAULT_SPAN_COUNT);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        mainActivity.setSelectedCategory(categories.get(position));
                }
            });
        }
    }
}