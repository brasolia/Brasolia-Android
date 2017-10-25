package br.com.brasolia.models;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cayke on 20/10/17.
 */

public class BSVenue extends BSItem {
    private List<BSOperatingHour> operating_hours;

    public BSVenue(String id, Map<String, Object> dictionary) {
        super(id, dictionary);

        List<Map<String, Object>> hours = (List) BSDictionary.getValueWithKeyAndType(dictionary, "operating_hours", List.class);
        operating_hours = new ArrayList<>();
        for (Map<String, Object> hour : hours) {
            operating_hours.add(new BSOperatingHour(hour));
        }

    }

    public List<BSOperatingHour> getOperating_hours() {
        return operating_hours;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(this.operating_hours);
    }

    protected BSVenue(Parcel in) {
        super(in);
        this.operating_hours = new ArrayList<BSOperatingHour>();
        in.readList(this.operating_hours, BSOperatingHour.class.getClassLoader());
    }

    public static final Creator<BSVenue> CREATOR = new Creator<BSVenue>() {
        @Override
        public BSVenue createFromParcel(Parcel source) {
            return new BSVenue(source);
        }

        @Override
        public BSVenue[] newArray(int size) {
            return new BSVenue[size];
        }
    };
}
