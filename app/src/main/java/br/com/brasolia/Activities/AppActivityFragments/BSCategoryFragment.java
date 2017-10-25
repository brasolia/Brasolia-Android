package br.com.brasolia.Activities.AppActivityFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.brasolia.Activities.AppActivity;
import br.com.brasolia.R;
import br.com.brasolia.Activities.SearchEventsActivity;
import br.com.brasolia.adapters.BSCategoriesAdapter;
import br.com.brasolia.application.BrasoliaApplication;
import br.com.brasolia.models.BSCategory;
import br.com.brasolia.models.BSRequestService;
import br.com.brasolia.util.BSConnectionFragment;
import br.com.brasolia.util.BSFirebaseListenerRef;
import br.com.brasolia.util.FragmentDataAndConnectionHandler;
import br.com.brasolia.util.ItemClickSupport;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by cayke on 11/04/17.
 */

public class BSCategoryFragment extends BSConnectionFragment {
    private static final int DEFAULT_SPAN_COUNT = 2;

    AppActivity context;
    RecyclerView recyclerView;
    private LinearLayout btSearch;
    private LinearLayout btProfile;
    private CircleImageView image_profile;

    private FragmentDataAndConnectionHandler dataAndConnectionHandler;
    private boolean isLoading = false;

    BSFirebaseListenerRef mRef;
    List<BSCategory> categories;

    @Override
    public void onDestroy() {
        if (mRef != null)
            mRef.detach();

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
            btProfile.getLayoutParams().width = ConvertUtils.dp2px(50);
            btProfile.getLayoutParams().height = ConvertUtils.dp2px(50);
            image_profile.getLayoutParams().width = ConvertUtils.dp2px(50);
            image_profile.getLayoutParams().height = ConvertUtils.dp2px(50);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isLoading) {
            dataAndConnectionHandler.showLoadingLayout();
        }
    }

    private void getCategories() {
        isLoading = true;

        mRef = BSRequestService.getCategories(this);
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

    @Override
    public void categoriesUpdated(boolean success, List<BSCategory> categories) {
        isLoading = false;

        if (success) {
            this.categories = categories;

            Collections.sort(this.categories, new Comparator<BSCategory>() {
                @Override
                public int compare(BSCategory category, BSCategory t1) {
                    return category.getPosition() - t1.getPosition();
                }
            });

            mountRecycler();
            dataAndConnectionHandler.showMainView();
        }
        else {
            dataAndConnectionHandler.showExceptionLayout();
        }
    }
}