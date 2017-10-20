package br.com.brasolia.models;

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
}
