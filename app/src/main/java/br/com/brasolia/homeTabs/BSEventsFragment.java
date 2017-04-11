package br.com.brasolia.homeTabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import br.com.brasolia.R;

/**
 * Created by cayke on 11/04/17.
 */

public class BSEventsFragment extends Fragment{
    RecyclerView recyclerView;
    ImageView imageView1, imageView2, imageView3;
    RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events_list, container, false);

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

        return rootView;
    }


    private void setSelectedMenu(int selectedMenu) {
        if (selectedMenu == 1) {
            imageView1.setImageResource(R.drawable.menu1);
            relativeLayout1.setBackgroundResource(R.color.black);
        }
        else {
            imageView1.setImageResource(R.drawable.selectedmenu1);
            relativeLayout1.setBackgroundResource(R.color.white);
        }
        if (selectedMenu == 2) {
            imageView2.setImageResource(R.drawable.menu2);
            relativeLayout2.setBackgroundResource(R.color.black);
        }
        else {
            imageView2.setImageResource(R.drawable.selectedmenu2);
            relativeLayout2.setBackgroundResource(R.color.white);
        }
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
}
