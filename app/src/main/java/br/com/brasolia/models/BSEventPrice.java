package br.com.brasolia.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Map;

/**
 * Created by cayke on 10/04/17.
 */

public class BSEventPrice implements Parcelable {
    enum Gender {
        male, female, universal
    }
    private Gender gender;
    private Double price;
    private Date startHour;
    private Date endHour;

    BSEventPrice(Map<String, Object> dictionary) {
        String sex = (String) BSDictionary.getValueWithKeyAndType(dictionary, "gender", String.class);
        switch (sex) {
            case "F":
                gender = Gender.female;
                break;
            case "M":
                gender = Gender.male;
                break;
            default:
                gender = Gender.universal;
                break;
        }

        price = (Double) BSDictionary.getValueWithKeyAndType(dictionary, "value", Double.class);

        startHour = BSDate.getDateWithKey("start", dictionary);
        endHour = BSDate.getDateWithKey("end", dictionary);
    }

    public Gender getGender() {
        return gender;
    }

    public Double getPrice() {
        return price;
    }

    public Date getStartHour() {
        return startHour;
    }

    public Date getEndHour() {
        return endHour;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
        dest.writeValue(this.price);
        dest.writeLong(this.startHour != null ? this.startHour.getTime() : -1);
        dest.writeLong(this.endHour != null ? this.endHour.getTime() : -1);
    }

    protected BSEventPrice(Parcel in) {
        int tmpGender = in.readInt();
        this.gender = tmpGender == -1 ? null : Gender.values()[tmpGender];
        this.price = (Double) in.readValue(Double.class.getClassLoader());
        long tmpStartHour = in.readLong();
        this.startHour = tmpStartHour == -1 ? null : new Date(tmpStartHour);
        long tmpEndHour = in.readLong();
        this.endHour = tmpEndHour == -1 ? null : new Date(tmpEndHour);
    }

    public static final Parcelable.Creator<BSEventPrice> CREATOR = new Parcelable.Creator<BSEventPrice>() {
        @Override
        public BSEventPrice createFromParcel(Parcel source) {
            return new BSEventPrice(source);
        }

        @Override
        public BSEventPrice[] newArray(int size) {
            return new BSEventPrice[size];
        }
    };
}
