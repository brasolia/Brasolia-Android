package br.com.brasolia.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by cayke on 10/04/17.
 */

public class BSEvent {
    private Date createdAt;
    private Date updatedAt;
    private Date endHour;
    private Date startHour;
    private Date closingEvent;
    private String description;
    private Double latitute;
    private Double longitude;
    private String address;
    private String locality;
    private String ticketLink;
    private List<String> tags;
    private String coverImageKey;
    private String phone;
    private String facebookEventID;
    private Double likes;
    private String listLink;
    private String id;
    private List<BSCategory> categories;
    private String name;
    private List<BSEventPrice> prices;

    public BSEvent(Map<String, Object> dictionary) {
        Map<String, Object> idDict = (Map<String, Object>) BSDictionary.getValueWithKeyAndType(dictionary, "id", Map.class);
        id = (String) BSDictionary.getValueWithKeyAndType(idDict, "$oid", String.class);

        List<Map<String, Object>> datesArray = (List<Map<String, Object>>) BSDictionary.getValueWithKeyAndType(dictionary, "dates", List.class);
        initDates(datesArray);

        Map<String, Object> localityDict = (Map<String, Object>) BSDictionary.getValueWithKeyAndType(dictionary, "locality", Map.class);
        initLocality(localityDict);

        ticketLink = (String) BSDictionary.getValueWithKeyAndType(dictionary, "ticket_link", String.class);

        createdAt = BSDate.getDateWithKey("created_at", dictionary);
        updatedAt = BSDate.getDateWithKey("updated_at", dictionary);
        closingEvent = BSDate.getDateWithKey("closing_event", dictionary);

        tags = (List<String>) BSDictionary.getValueWithKeyAndType(dictionary, "tags", List.class);

        coverImageKey = (String) BSDictionary.getValueWithKeyAndType(dictionary, "cover", String.class);
        phone = (String) BSDictionary.getValueWithKeyAndType(dictionary, "phone", String.class);
        facebookEventID = (String) BSDictionary.getValueWithKeyAndType(dictionary, "facebook_event_id", String.class);
        description = (String) BSDictionary.getValueWithKeyAndType(dictionary, "description", String.class);
        listLink = (String) BSDictionary.getValueWithKeyAndType(dictionary, "list_link", String.class);
        name = (String) BSDictionary.getValueWithKeyAndType(dictionary, "name", String.class);

        likes = (Double) BSDictionary.getValueWithKeyAndType(dictionary, "likes", Double.class);

        List<Map<String, Object>> pricesArray = (List<Map<String, Object>>) BSDictionary.getValueWithKeyAndType(dictionary, "prices", List.class);
        prices = new ArrayList<>();
        if (pricesArray != null) {
            for (Map<String, Object> priceDict: pricesArray) {
                prices.add(new BSEventPrice(priceDict));
            }
        }

        List<Map<String, Object>> categoriesArray = (List<Map<String, Object>>) BSDictionary.getValueWithKeyAndType(dictionary, "categories", List.class);
        categories = new ArrayList<>();
        if (categoriesArray != null) {
            for (Map<String, Object> category: categoriesArray) {
                categories.add(new BSCategory(category));
            }
        }
    }

    private void initDates(List<Map<String, Object>> dates) {
        if (dates != null && dates.size() > 0) {
            Map<String, Object> hoursDict = dates.get(0);

            endHour = BSDate.getDateWithKey("datehour_end", hoursDict);
            startHour = BSDate.getDateWithKey("datehour_init", hoursDict);
        }
    }

    private void initLocality(Map<String, Object> localityDict) {
        address = (String) BSDictionary.getValueWithKeyAndType(localityDict, "address", String.class);
        locality = (String) BSDictionary.getValueWithKeyAndType(localityDict, "title", String.class);

        Map<String, Object> point = (Map<String, Object>) BSDictionary.getValueWithKeyAndType(localityDict, "point", Map.class);
        List<Double> coordinates = (List<Double>) BSDictionary.getValueWithKeyAndType(point, "coordinates", List.class);
        longitude = coordinates.get(0);
        latitute = coordinates.get(1);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getEndHour() {
        return endHour;
    }

    public Date getStartHour() {
        return startHour;
    }

    public Date getClosingEvent() {
        return closingEvent;
    }

    public String getDescription() {
        return description;
    }

    public Double getLatitute() {
        return latitute;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getLocality() {
        return locality;
    }

    public String getTicketLink() {
        return ticketLink;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getCoverImageKey() {
        return coverImageKey;
    }

    public String getPhone() {
        return phone;
    }

    public String getFacebookEventID() {
        return facebookEventID;
    }

    public Double getLikes() {
        return likes;
    }

    public String getListLink() {
        return listLink;
    }

    public String getId() {
        return id;
    }

    public List<BSCategory> getCategories() {
        return categories;
    }

    public String getName() {
        return name;
    }

    public List<BSEventPrice> getPrices() {
        return prices;
    }
}
