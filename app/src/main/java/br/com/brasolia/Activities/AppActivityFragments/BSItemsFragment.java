package br.com.brasolia.Activities.AppActivityFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import br.com.brasolia.Activities.AppActivity;
import br.com.brasolia.Activities.BSEventActivity;
import br.com.brasolia.R;
import br.com.brasolia.adapters.BSItemsAdapter;
import br.com.brasolia.models.BSCategory;
import br.com.brasolia.models.BSItem;
import br.com.brasolia.models.BSRequestService;
import br.com.brasolia.util.BSConnectionFragment;
import br.com.brasolia.util.BSFirebaseListenerRef;
import br.com.brasolia.util.FragmentDataAndConnectionHandler;
import br.com.brasolia.util.ItemClickSupport;

/**
 * Created by cayke on 11/04/17.
 */

public class BSItemsFragment extends BSConnectionFragment {

    AppActivity mContext;

    RecyclerView recyclerView;
    ImageView imageView1, imageView2, imageView3;
    RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3;
    private LinearLayout bottomBar;
    private ImageView backToCategories;

    List<BSItem> items;
    BSCategory filterCategory;
    int selectedFilter = 1;

    private FragmentDataAndConnectionHandler dataAndConnectionHandler;
    private boolean isLoading = false;

    BSFirebaseListenerRef mRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            filterCategory = bundle.getParcelable("category");
        }

        getItemsFromCategory(filterCategory);
    }

    @Override
    public void onDestroy() {
        if (mRef != null)
            mRef.detach();

        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events_list, container, false);

        Context context = rootView.getContext();
        if (context instanceof AppActivity)
            mContext = (AppActivity) context;

        backToCategories = (ImageView) rootView.findViewById(R.id.backToCategories);
        bottomBar = (LinearLayout) rootView.findViewById(R.id.bottom_bar);
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

        backToCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.popFragment();
            }
        });

        //-------------------- INIT CONNECTION AND EMPTY DATA HANDLER -------------------------------
        dataAndConnectionHandler = new FragmentDataAndConnectionHandler(inflater, container, rootView);
        dataAndConnectionHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItemsFromCategory(filterCategory);
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
            imageView1.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_hour_white));
            relativeLayout1.setBackgroundResource(R.color.black);
        } else {
            imageView1.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_hour_black));
            relativeLayout1.setBackgroundResource(R.color.white);
        }

        //LOCALIZAÇÃO
        if (selectedMenu == 2) {
            imageView2.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_distance_white));
            relativeLayout2.setBackgroundResource(R.color.black);
        } else {
            imageView2.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_distance_black));
            relativeLayout2.setBackgroundResource(R.color.white);
        }

        //PREÇO
        if (selectedMenu == 3) {
            imageView3.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_price_white));
            relativeLayout3.setBackgroundResource(R.color.black);
        } else {
            imageView3.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_price_black));
            relativeLayout3.setBackgroundResource(R.color.white);
        }

        mountRecycler(selectedFilter);
    }

    private void getItemsFromCategory(BSCategory category) {
        isLoading = true;

        mRef = BSRequestService.getItemsFromCategory(category, this);
    }

    private void mountRecycler(int choice) {
        if (items != null) {

//            switch (choice) {
//
//                case 1:
//                    Collections.sort(events, new Comparator<BSEvent>() {
//                        @Override
//                        public int compare(BSEvent o1, BSEvent o2) {
//                            return Long.valueOf(o1.getStartHour().getTime()).compareTo(o2.getStartHour().getTime());
//                        }
//                    });
//                    break;
//
//                case 2:
//                    Collections.sort(events, new Comparator<BSEvent>() {
//                        public int compare(BSEvent obj1, BSEvent obj2) {
//                            return Double.valueOf(obj1.getDistance()).compareTo(Double.valueOf(obj2.getDistance()));
//                        }
//                    });
//                    break;
//
//                case 3:
//                    Collections.sort(events, new Comparator<BSEvent>() {
//                        public int compare(BSEvent obj1, BSEvent obj2) {
//                            return Double.valueOf(obj1.getPrices().get(0).getPrice()).compareTo(Double.valueOf(obj2.getPrices().get(0).getPrice()
//                            ));
//                        }
//                    });
//                    break;
//
//            }


            recyclerView.setHasFixedSize(true);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new BSItemsAdapter(items, choice));
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (dy > 0) {

                        if (bottomBar.getVisibility() == View.VISIBLE) {

                            Animation fadeOut = new AlphaAnimation(1, 0);
                            fadeOut.setInterpolator(new AccelerateInterpolator());
                            fadeOut.setDuration(1000);
                            bottomBar.setAnimation(fadeOut);
                            bottomBar.setVisibility(View.GONE);
                        }
                    } else {

                        if(bottomBar.getVisibility() == View.GONE){

                            Animation fadeIn = new AlphaAnimation(0, 1);
                            fadeIn.setInterpolator(new DecelerateInterpolator());
                            fadeIn.setDuration(1000);
                            bottomBar.setAnimation(fadeIn);
                            bottomBar.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    Intent i = new Intent(getActivity(), BSEventActivity.class);
                    i.putExtra("item", items.get(position));
                    startActivity(i);
                }
            });
        }
    }


    @Override
    public void itemsUpdated(boolean success, List<BSItem> items) {
        isLoading = false;

        if (success) {
            this.items = items;
            mountRecycler(selectedFilter);
            dataAndConnectionHandler.showMainView();
        }
        else {
            dataAndConnectionHandler.showExceptionLayout();
        }
    }
}