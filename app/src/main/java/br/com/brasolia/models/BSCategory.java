package br.com.brasolia.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cayke on 07/04/17.
 */

public class BSCategory implements Parcelable {
    public enum Size {
        big, small
    }

    private String key;
    private String franchise;
    private String name;
    private int position;
    private Size size;
    private String url_image;

    public BSCategory() {}

    public String getKey() {
        return key;
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
        dest.writeString(this.key);
        dest.writeString(this.franchise);
        dest.writeString(this.name);
        dest.writeInt(this.position);
        dest.writeInt(this.size == null ? -1 : this.size.ordinal());
        dest.writeString(this.url_image);
    }

    protected BSCategory(Parcel in) {
        this.key = in.readString();
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