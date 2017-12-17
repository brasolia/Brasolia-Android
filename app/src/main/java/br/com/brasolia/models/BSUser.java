package br.com.brasolia.models;

import java.util.Map;

/**
 * Created by cayke on 10/04/17.
 */

public class BSUser {
    private String id;
    private String displayName;
    private String imageKey;

    public BSUser (Map<String, Object> dictionary) {
        id = (String) BSDictionary.getValueWithKeyAndType(dictionary, "id", String.class);
        displayName = (String) BSDictionary.getValueWithKeyAndType(dictionary, "name", String.class);
        imageKey = (String) BSDictionary.getValueWithKeyAndType(dictionary, "image", String.class);
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImageKey() {
        return imageKey;
    }
}
