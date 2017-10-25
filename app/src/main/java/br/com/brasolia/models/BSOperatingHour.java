package br.com.brasolia.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by cayke on 20/10/17.
 */

public class BSOperatingHour implements Parcelable{

    public BSOperatingHour(Map<String, Object> dictionary) {

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    protected BSOperatingHour(Parcel in) {
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
