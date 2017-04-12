package br.com.brasolia.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by cayke on 10/04/17.
 */

public class BSEvent implements Parcelable {
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
    private Double rating;
    private List<String> images;

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

        //todo pegar o rating
        rating = 0d;

        //todo pegar imagens
        images = new ArrayList<>();
        images.add(coverImageKey);
        images.add(coverImageKey);
        images.add(coverImageKey);
        images.add(coverImageKey);
        images.add(coverImageKey);
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

    public Double getRating() {
        return rating;
    }

    public List<String> getImages() {
        return images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeLong(this.endHour != null ? this.endHour.getTime() : -1);
        dest.writeLong(this.startHour != null ? this.startHour.getTime() : -1);
        dest.writeLong(this.closingEvent != null ? this.closingEvent.getTime() : -1);
        dest.writeString(this.description);
        dest.writeValue(this.latitute);
        dest.writeValue(this.longitude);
        dest.writeString(this.address);
        dest.writeString(this.locality);
        dest.writeString(this.ticketLink);
        dest.writeStringList(this.tags);
        dest.writeString(this.coverImageKey);
        dest.writeString(this.phone);
        dest.writeString(this.facebookEventID);
        dest.writeValue(this.likes);
        dest.writeString(this.listLink);
        dest.writeString(this.id);
        dest.writeTypedList(this.categories);
        dest.writeString(this.name);
        dest.writeTypedList(this.prices);
        dest.writeValue(this.rating);
        dest.writeStringList(this.images);
    }

    protected BSEvent(Parcel in) {
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        long tmpEndHour = in.readLong();
        this.endHour = tmpEndHour == -1 ? null : new Date(tmpEndHour);
        long tmpStartHour = in.readLong();
        this.startHour = tmpStartHour == -1 ? null : new Date(tmpStartHour);
        long tmpClosingEvent = in.readLong();
        this.closingEvent = tmpClosingEvent == -1 ? null : new Date(tmpClosingEvent);
        this.description = in.readString();
        this.latitute = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.address = in.readString();
        this.locality = in.readString();
        this.ticketLink = in.readString();
        this.tags = in.createStringArrayList();
        this.coverImageKey = in.readString();
        this.phone = in.readString();
        this.facebookEventID = in.readString();
        this.likes = (Double) in.readValue(Double.class.getClassLoader());
        this.listLink = in.readString();
        this.id = in.readString();
        this.categories = in.createTypedArrayList(BSCategory.CREATOR);
        this.name = in.readString();
        this.prices = in.createTypedArrayList(BSEventPrice.CREATOR);
        this.rating = (Double) in.readValue(Double.class.getClassLoader());
        this.images = in.createStringArrayList();
    }

    public static final Creator<BSEvent> CREATOR = new Creator<BSEvent>() {
        @Override
        public BSEvent createFromParcel(Parcel source) {
            return new BSEvent(source);
        }

        @Override
        public BSEvent[] newArray(int size) {
            return new BSEvent[size];
        }
    };
}
