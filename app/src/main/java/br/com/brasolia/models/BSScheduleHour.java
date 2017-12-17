package br.com.brasolia.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

/**
 * Created by cayke on 17/12/2017.
 */

public class BSScheduleHour implements Parcelable {
    private Calendar initialDate;
    private Calendar finishDate;

    public BSScheduleHour (Map<String, Object> dictionary) {
        long initial_date =  ((Double)BSDictionary.getValueWithKeyAndType(dictionary, "initial_date", Double.class)).longValue();
        long finish_date =  ((Double)BSDictionary.getValueWithKeyAndType(dictionary, "finish_date", Double.class)).longValue();

        this.initialDate = new GregorianCalendar();
        this.initialDate.setTime(new Date(initial_date));
        this.finishDate = new GregorianCalendar();
        this.finishDate.setTime(new Date(finish_date));
    }

    public String getDayString() {
        int day = initialDate.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                return  "Domingo";
            case Calendar.MONDAY:
                return "Segunda-feira";
            case Calendar.TUESDAY:
                return "Terça-feira";
            case Calendar.WEDNESDAY:
                return "Quarta-feira";
            case Calendar.THURSDAY:
                return "Quinta-feira";
            case Calendar.FRIDAY:
                return "Sexta-feira";
            case Calendar.SATURDAY:
                return "Sábado";
            default:
                return "";
        }
    }

    public String getHourString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String init = sdf.format(initialDate.getTime());
        String end = sdf.format(finishDate.getTime());

        return init + " - "  + end;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.initialDate);
        dest.writeSerializable(this.finishDate);
    }

    protected BSScheduleHour(Parcel in) {
        this.initialDate = (Calendar) in.readSerializable();
        this.finishDate = (Calendar) in.readSerializable();
    }

    public static final Creator<BSScheduleHour> CREATOR = new Creator<BSScheduleHour>() {
        @Override
        public BSScheduleHour createFromParcel(Parcel source) {
            return new BSScheduleHour(source);
        }

        @Override
        public BSScheduleHour[] newArray(int size) {
            return new BSScheduleHour[size];
        }
    };
}
