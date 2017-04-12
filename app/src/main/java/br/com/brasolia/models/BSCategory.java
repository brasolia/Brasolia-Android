package br.com.brasolia.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by cayke on 07/04/17.
 */

public class BSCategory implements Parcelable {
    private String id;
    private String name;
    private String image;
    private int order;

    public BSCategory(Map<String, Object> dictionary) {
        Map<String, Object> temp = (Map<String, Object>) BSDictionary.getValueWithKeyAndType(dictionary, "id", Map.class);
        if (temp != null)
            id = (String) BSDictionary.getValueWithKeyAndType(temp, "$oid", String.class);
        else
            id = (String) BSDictionary.getValueWithKeyAndType(dictionary, "$oid", String.class);

        name = (String) BSDictionary.getValueWithKeyAndType(dictionary, "name", String.class);
        image = (String) BSDictionary.getValueWithKeyAndType(dictionary, "image", String.class);
        try {
            order = ((Double) BSDictionary.getValueWithKeyAndType(dictionary, "order", Double.class)).intValue();
        }
        catch (Exception e) {
            order = 0;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getOrder() {
        return order;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeInt(this.order);
    }

    protected BSCategory(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.image = in.readString();
        this.order = in.readInt();
    }

    public static final Parcelable.Creator<BSCategory> CREATOR = new Parcelable.Creator<BSCategory>() {
        @Override
        public BSCategory createFromParcel(Parcel source) {
            return new BSCategory(source);
        }

        @Override
        public BSCategory[] newArray(int size) {
            return new BSCategory[size];
        }
    };
}