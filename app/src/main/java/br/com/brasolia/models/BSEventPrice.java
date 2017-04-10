package br.com.brasolia.models;

import java.util.Date;
import java.util.Map;

/**
 * Created by cayke on 10/04/17.
 */

public class BSEventPrice {
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
}
