package br.com.brasolia.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by cayke on 07/04/17.
 */

public class BSCategory implements Parcelable {
    public enum Size {
        big, small
    }

    private String id;
    private String franchise;
    private String name;
    private int position;
    private Size size;
    private String url_image;

    public BSCategory(String id, Map<String, Object> dictionary) {
        this.id = id;

        franchise = (String) BSDictionary.getValueWithKeyAndType(dictionary, "franchise", String.class);
        name = (String) BSDictionary.getValueWithKeyAndType(dictionary, "name", String.class);
        position = ((Double) BSDictionary.getValueWithKeyAndType(dictionary, "position", Double.class)).intValue();
        String size = (String) BSDictionary.getValueWithKeyAndType(dictionary, "size", String.class);
        if (size.equals("big"))
            this.size = Size.big;
        else
            this.size = Size.small;
        url_image = (String) BSDictionary.getValueWithKeyAndType(dictionary, "url_image", String.class);
    }

    public String getId() {
        return id;
    }

    public String getFranchise() {
        return franchise;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public Size getSize() {
        return size;
    }

    public String getUrl_image() {
        return url_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.franchise);
        dest.writeString(this.name);
        dest.writeInt(this.position);
        dest.writeInt(this.size == null ? -1 : this.size.ordinal());
        dest.writeString(this.url_image);
    }

    protected BSCategory(Parcel in) {
        this.id = in.readString();
        this.franchise = in.readString();
        this.name = in.readString();
        this.position = in.readInt();
        int tmpSize = in.readInt();
        this.size = tmpSize == -1 ? null : Size.values()[tmpSize];
        this.url_image = in.readString();
    }

    public static final Creator<BSCategory> CREATOR = new Creator<BSCategory>() {
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