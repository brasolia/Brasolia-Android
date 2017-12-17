package br.com.brasolia.models;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cayke on 10/04/17.
 * Refactored by cayke on 17/12/17.
 */

public class BSEvent extends BSItem {
    private List<BSScheduleHour> schedule_hours;

    public BSEvent(String id, Map<String, Object> dictionary) {
        super(id, dictionary);

        List<Map<String, Object>> hours = (List) BSDictionary.getValueWithKeyAndType(dictionary, "schedule_hours", List.class);
        schedule_hours = new ArrayList<>();
        for (Map<String, Object> hour : hours) {
            schedule_hours.add(new BSScheduleHour(hour));
        }

    }

    public List<BSScheduleHour> getSchedule_hours() {
        return schedule_hours;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(this.schedule_hours);
    }

    protected BSEvent(Parcel in) {
        super(in);
        this.schedule_hours = new ArrayList<BSScheduleHour>();
        in.readList(this.schedule_hours, BSScheduleHour.class.getClassLoader());
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
