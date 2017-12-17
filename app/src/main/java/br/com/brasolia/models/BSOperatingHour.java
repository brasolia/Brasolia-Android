package br.com.brasolia.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by cayke on 20/10/17.
 */

public class BSOperatingHour implements Parcelable{
    private String day_week;
    private String open_hour;
    private String close_hour;
    private boolean open;


    public BSOperatingHour(Map<String, Object> dictionary) {
        day_week =  (String) BSDictionary.getValueWithKeyAndType(dictionary, "day_week", String.class);
        open =  (boolean) BSDictionary.getValueWithKeyAndType(dictionary, "open", Boolean.class);

        if (open) {
            open_hour = (String) BSDictionary.getValueWithKeyAndType(dictionary, "start_date", String.class);
            close_hour = (String) BSDictionary.getValueWithKeyAndType(dictionary, "end_date", String.class);
        }
    }

    public String getDay_week() {
        return day_week;
    }

    public String getOpen_hour() {
        return open_hour;
    }

    public String getClose_hour() {
        return close_hour;
    }

    public boolean isOpen() {
        return open;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.day_week);
        dest.writeString(this.open_hour);
        dest.writeString(this.close_hour);
        dest.writeByte(this.open ? (byte) 1 : (byte) 0);
    }

    protected BSOperatingHour(Parcel in) {
        this.day_week = in.readString();
        this.open_hour = in.readString();
        this.close_hour = in.readString();
        this.open = in.readByte() != 0;
    }

    public static final Creator<BSOperatingHour> CREATOR = new Creator<BSOperatingHour>() {
        @Override
        public BSOperatingHour createFromParcel(Parcel source) {
            return new BSOperatingHour(source);
        }

        @Override
        public BSOperatingHour[] newArray(int size) {
            return new BSOperatingHour[size];
        }
    };
}
