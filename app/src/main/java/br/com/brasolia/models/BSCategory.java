package br.com.brasolia.models;

import java.util.Map;

/**
 * Created by cayke on 07/04/17.
 */

public class BSCategory {
    private String id;
    private String name;
    private String image;
    private int order;

    BSCategory(Map<String, Object> dictionary) {
        Map<String, Object> temp = (Map<String, Object>) BSDictionary.getValueWithKeyAndType(dictionary, "id", Map.class);
        id = (String) BSDictionary.getValueWithKeyAndType(temp, "$oid", String.class);

        //id = (String) BSDictionary.getValueWithKeyAndType(dictionary, "id", String.class); //todo ta vindo errado do server

        name = (String) BSDictionary.getValueWithKeyAndType(dictionary, "name", String.class);
        image = (String) BSDictionary.getValueWithKeyAndType(dictionary, "image", String.class);
        order = ((Double) BSDictionary.getValueWithKeyAndType(dictionary, "order", Double.class)).intValue();
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
}
