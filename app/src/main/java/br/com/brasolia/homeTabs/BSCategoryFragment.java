package br.com.brasolia.homeTabs;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.brasolia.AppActivity;
import br.com.brasolia.Connectivity.BSConnection;
import br.com.brasolia.Connectivity.BSEndpoint;
import br.com.brasolia.R;
import br.com.brasolia.SearchEventsActivity;
import br.com.brasolia.adapters.BSCategoriesAdapter;
import br.com.brasolia.application.BrasoliaApplication;
import br.com.brasolia.models.BSCategory;
import br.com.brasolia.util.FragmentDataAndConnectionHandler;
import br.com.brasolia.util.ItemClickSupport;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cayke on 11/04/17.
 */

public class BSCategoryFragment extends Fragment {
    private static final int DEFAULT_SPAN_COUNT = 2;

    AppActivity context;
    RecyclerView recyclerView;
    private LinearLayout btSearch;
    private LinearLayout btProfile;
    private CircleImageView image_profile;

    private FragmentDataAndConnectionHandler dataAndConnectionHandler;
    private boolean isLoading = false;

    Call<JsonObject> call;
    List<BSCategory> categories;

    @Override
    public void onDestroy() {
        if (call != null)
            call.cancel();

        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getCategories();
    }

    public void onResume() {
        super.onResume();
        btSearch.setVisibility(View.VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        final Context context = rootView.getContext();

        if (context instanceof AppActivity) {
            this.context = (AppActivity) context;
        }

        //SCREEN ELEMENTS -----------------------------------------------------------------
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerCategories);
        btSearch = (LinearLayout) rootView.findViewById(R.id.fragment_categories_search);
        image_profile = (CircleImageView) rootView.findViewById(R.id.image_profile);
        btProfile = (LinearLayout) rootView.findViewById(R.id.fragment_categories_config);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            btProfile.getLayoutParams().width = convertDpToPixel(50, context);
            btProfile.getLayoutParams().height = convertDpToPixel(50, context);
            image_profile.getLayoutParams().width = convertDpToPixel(45, context);
            image_profile.getLayoutParams().height = convertDpToPixel(45, context);
            Picasso picasso = Picasso.with(BrasoliaApplication.getAppContext());
            picasso.setIndicatorsEnabled(false);
            picasso.load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).resize(500, 500).into(image_profile);
        }
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
                BSCategoryFragment.this.context.pushFragment(new BSProfileFragment(), "BSProfileFragment");
            }
        });

        //-------------------- INIT CONNECTION AND EMPTY DATA HANDLER -------------------------------
        dataAndConnectionHandler = new FragmentDataAndConnectionHandler(inflater, container, rootView);
        dataAndConnectionHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategories();
                dataAndConnectionHandler.showLoadingLayout();
            }
        });
        dataAndConnectionHandler.setLoadingMessage("Carregando dados...");
        //----------------------------------------------------------------------------------

        mountRecycler();

        return rootView;
    }

    public static int convertDpToPixel(int dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = dp * ((int)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isLoading) {
            dataAndConnectionHandler.showLoadingLayout();
        }
    }

    private void getCategories() {
        isLoading = true;

        BSEndpoint endpoint = BSConnection.createService(BSEndpoint.class);
        Call<JsonObject> call = endpoint.getCategories();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                isLoading = false;

                if (response.isSuccessful()) {
                    Object responseObject = new Gson().fromJson(response.body(), Object.class);

                    try {
                        Map<String, Object> result = (Map<String, Object>) responseObject;
                        categories = new ArrayList<BSCategory>();

                        Iterator it = result.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            categories.add(new BSCategory((String) pair.getKey(), (Map<String, Object>) pair.getValue()));
                            it.remove(); // avoids a ConcurrentModificationException
                        }

                        Collections.sort(categories, new Comparator<BSCategory>() {
                            @Override
                            public int compare(BSCategory category, BSCategory t1) {
                                return category.getPosition() - t1.getPosition();
                            }
                        });

                        mountRecycler();
                        dataAndConnectionHandler.showMainView();
                    }
                    catch (Exception e) {
                        dataAndConnectionHandler.showExceptionLayout();
                    }
                }
                else {
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

    private void mountRecycler() {
        if (categories != null) {
            recyclerView.setHasFixedSize(true);
            GridLayoutManager layoutManager = new GridLayoutManager(context, DEFAULT_SPAN_COUNT);
            BSCategoriesAdapter adapter = new BSCategoriesAdapter(categories);
            adapter.setLayoutManager(layoutManager, DEFAULT_SPAN_COUNT);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    BSItemsFragment itemsFragment = new BSItemsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("category", categories.get(position));
                    itemsFragment.setArguments(bundle);
                    context.pushFragment(itemsFragment, "BSItemsFragment");
                }
            });
        }
    }
}