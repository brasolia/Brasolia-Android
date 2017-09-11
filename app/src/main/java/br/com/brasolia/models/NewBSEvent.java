package br.com.brasolia.models;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by breno on 11/09/2017.
 */

public class NewBSEvent {

    private String description;
    private int id;
    private NewBSVenue venue;
    private ArrayList<NewBSCategory> categories;
    private String title;
    private String phone;
    private int price_tier;
    private ArrayList<NewBSDate> dates;
    private ArrayList<NewBSImage> images;
    private String cellphone;
    //todo descomentar private ArrayList<Tags> tags;
    //todo descomentar private ArrayList<Guests> guest_list;

    public NewBSEvent(Map<String, Object> objectMap){
        description = (String) objectMap.get("description");
        id = (int) objectMap.get("id");
        venue = (NewBSVenue) objectMap.get("venue");
        categories = (ArrayList<NewBSCategory>) objectMap.get("categories");
        title = (String) objectMap.get("title");
        phone = (String) objectMap.get("phone");
        price_tier = (int) objectMap.get("price_tier");
        dates = (ArrayList<NewBSDate>) objectMap.get("dates");
        images = (ArrayList<NewBSImage>) objectMap.get("images");
        cellphone = (String) objectMap.get("cellphone");
        //todo descomentar tags = (ArrayList<Tags>) objectMap.get("tags");
        //todo descomentar guest_list = (ArrayList<Guests>) objectMap.get("guest_list");
    }
}