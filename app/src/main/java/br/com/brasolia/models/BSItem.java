package br.com.brasolia.models;

import java.util.List;
import java.util.Map;

import br.com.brasolia.AppActivity;

/**
 * Created by cayke on 20/10/17.
 */

public abstract class BSItem  {
    private String id;
    private String address;
    private String description;
    private List<String> images;
    private String thumb;
    private Double latitude;
    private Double longitude;
    private Double distance;
    private String name;
    private String phone;
    private int price_tier;
    private String url;
    private String website;

    public BSItem(String id, Map<String, Object> dictionary) {
        this.id = id;

        address = (String) BSDictionary.getValueWithKeyAndType(dictionary, "address", String.class);
        description = (String) BSDictionary.getValueWithKeyAndType(dictionary, "description", String.class);
        thumb = (String) BSDictionary.getValueWithKeyAndType(dictionary, "thumb", String.class);
        images = (List) BSDictionary.getValueWithKeyAndType(dictionary, "images", List.class);
        latitude = (Double) BSDictionary.getValueWithKeyAndType(dictionary, "latitude", Double.class);
        longitude = (Double) BSDictionary.getValueWithKeyAndType(dictionary, "longitude", Double.class);
        distance = distance(getLatitude(), getLongitude(), AppActivity.getLocationUtil().getLatitude(), AppActivity.getLocationUtil().getLongitude());
        name = (String) BSDictionary.getValueWithKeyAndType(dictionary, "name", String.class);
        phone = (String) BSDictionary.getValueWithKeyAndType(dictionary, "phone", String.class);
        price_tier = ((Double) BSDictionary.getValueWithKeyAndType(dictionary, "price_tier", Double.class)).intValue();
        url = (String) BSDictionary.getValueWithKeyAndType(dictionary, "url", String.class);
        website = (String) BSDictionary.getValueWithKeyAndType(dictionary, "website", String.class);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getImages() {
        return images;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getPrice_tier() {
        return price_tier;
    }

    public String getUrl() {
        return url;
    }

    public String getWebsite() {
        return website;
    }

    public String getThumb() {
        return thumb;
    }

    public Double getDistance() {
        return distance;
    }
}
