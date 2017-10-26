package br.com.brasolia.models;

import java.util.Date;
import java.util.Map;

/**
 * Created by cayke on 10/04/17.
 */

public class BSDate {
    public static Date getDateWithKey(String key, Map<String, Object> dictionary) {
        Map<String, Object> endHourDict = (Map<String, Object>) dictionary.get(key);
        return new Date(((Double) endHourDict.get("$date")).longValue());
    }

    public static Date getDate(long timestamp) {
        Date date = new Date();
        date.setTime(timestamp);
        return date;
    }
}
