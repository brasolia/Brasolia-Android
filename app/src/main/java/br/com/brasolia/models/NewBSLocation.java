package br.com.brasolia.models;

import java.util.Map;

/**
 * Created by breno on 11/09/2017.
 */

public class NewBSLocation {

    private String longitude;
    private String address;
    private String latitude;

    public NewBSLocation(Map<String, Object> objectMap){
        longitude = (String) objectMap.get("tags");
        address = (String) objectMap.get("tags");
        latitude = (String) objectMap.get("tags");
    }

    //region GETS

    public String getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    //endregion
}
