package br.com.brasolia.models;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by breno on 11/09/2017.
 */

public class NewBSVenue {

    private int id;
    private NewBSLocation location;
    private ArrayList<NewBSImage> images;
    private String description;
    private int franchise_id;
    //todo descomentar private ArrayList<Comments> comments;
    private ArrayList<NewBSCategory> categories;
    //todo descomentar private ArrayList<Tags> tags;
    private String phone;
    private String name;
    private int price_tier;
    private String cellphone;

    public NewBSVenue(Map<String, Object> objectMap){
        id = (int) objectMap.get("id");
        location = (NewBSLocation) objectMap.get("location");
        images = (ArrayList<NewBSImage>) objectMap.get("images");

        description = (String) objectMap.get("description");
        franchise_id = (int) objectMap.get("franchise_id");
        //todo descomentar tags = (ArrayList<Comments>) objectMap.get("comments");
        categories = (ArrayList<NewBSCategory>) objectMap.get("categories");
        //todo descomentar tags = (ArrayList<Tags>) objectMap.get("tags");
        phone = (String) objectMap.get("phone");
        name = (String) objectMap.get("name");
        price_tier = (int) objectMap.get("price_tier");
        cellphone = (String) objectMap.get("cellphone");
    }
}
