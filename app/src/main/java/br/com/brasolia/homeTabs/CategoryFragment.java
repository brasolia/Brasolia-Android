package br.com.brasolia.homeTabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.brasolia.R;
import br.com.brasolia.SearchEventsActivity;
import br.com.brasolia.adapters.CategoriesAdapter;
import br.com.brasolia.application.BrasoliaApplication;
import br.com.brasolia.models.Category;
import br.com.brasolia.models.ItemAdapterCategory;
import br.com.brasolia.models.User;
import br.com.brasolia.webserver.BrasoliaAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Matheus on 15/07/2016.
 */
public class CategoryFragment extends Fragment {

    private RecyclerView recyclerCategories;
    private List<Category> categories;
    private ArrayList<Category> searchCategories;
    private ArrayList<ItemAdapterCategory> itensAdapter;
    private CategoriesAdapter adapter;
    private TextView tvStatus;
    private User user;
    private boolean searching = false;
    private Button btSearch;
    private EditText searchEditText;

    public CategoryFragment() {
    }

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        //SCREEN ELEMENTS -----------------------------------------------------------------
        recyclerCategories = (RecyclerView) rootView.findViewById(R.id.recyclerCategories);
        tvStatus = (TextView) rootView.findViewById(R.id.tvStatusCategory);
        btSearch = (Button) rootView.findViewById(R.id.searchBtn);
        searchEditText = (EditText) rootView.findViewById(R.id.searchEditText);
        // --------------------------------------------------------------------------------

        user = User.getUser(getActivity());

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btSearch.setVisibility(View.GONE);
                Intent it = new Intent(getActivity(), SearchEventsActivity.class);
                startActivity(it);
            }
        });

        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btSearch.setVisibility(View.GONE);
                Intent it = new Intent(getActivity(), SearchEventsActivity.class);
                startActivity(it);
            }
        });

        BrasoliaAPI api = ((BrasoliaApplication) getActivity().getApplication()).getApi();

        Call<JsonObject> getCategories = api.getCategories();
        getCategories.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    categories = new ArrayList<>();

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

                    for (int i = 0; i < response.body().get("data").getAsJsonArray().size(); i++) {
                        Category aux = new Category();
                        aux.setName(response.body().get("data").getAsJsonArray().get(i).getAsJsonObject().get("name").getAsString());

                        aux.setId(response.body().get("data").getAsJsonArray().get(i).getAsJsonObject().get("id").getAsJsonObject().get("$oid").getAsString());

                        if (sp.getString("cookie", "").equals("")) {
                            if (aux.getId().equals("5833c1fc5157a00685c21eea")) {
                                continue;
                            }
                        }

                        if (!response.body().get("data").getAsJsonArray().get(i).getAsJsonObject().get("image").isJsonNull()) {
                            String image = response.body().get("data").getAsJsonArray().get(i).getAsJsonObject().get("image").getAsString();
                            aux.setImage("https://s3-us-west-2.amazonaws.com/bs.thumb/" + image);
                        } else
                            aux.setImage("https://s3-us-west-2.amazonaws.com/bs.thumb/201606301621205992709250859f6716491aafb42c42057ab86a.png");

                        categories.add(aux);
                    }

                    if (categories != null && !categories.isEmpty()) {
                        itensAdapter = createItemAdapterFromCategories(categories);
                        adapter = new CategoriesAdapter(getActivity(), itensAdapter);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        recyclerCategories.setLayoutManager(mLayoutManager);
                        recyclerCategories.setItemAnimator(new DefaultItemAnimator());
                        recyclerCategories.setAdapter(adapter);

                        tvStatus.setVisibility(View.GONE); // hide status
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });

        recyclerCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }

    public Category getCategory(int position) {

        Category c;

        if (searching) {
            c = searchCategories.get(position);
        } else {
            c = categories.get(position);
        }

        return c;
    }

    public void onResume() {
        super.onResume();
        btSearch.setVisibility(View.VISIBLE);
    }

    public ArrayList<ItemAdapterCategory> createItemAdapterFromCategories(List<Category> categories) {

        Collections.sort(categories, new CategoryOrderComparator());

        ArrayList<ItemAdapterCategory> itensAdapter = new ArrayList<ItemAdapterCategory>();

        for (int i = 0; i < categories.size(); i += 2) {
            ItemAdapterCategory item = new ItemAdapterCategory();

            item.setCategoryLeft(categories.get(i));

            if ((i + 1) < categories.size())
                item.setCategoryRight(categories.get(i + 1)); // IF THERE IS A SECOND CATEGORY
            else
                item.setCategoryRight(null);

            itensAdapter.add(item);
        }

        return itensAdapter;
    }
}

class CategoryOrderComparator implements Comparator<Category> {
    @Override
    public int compare(Category p1, Category p2) {
        return p1.getOrder() - p2.getOrder();
    }
}
