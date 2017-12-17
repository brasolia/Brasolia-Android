package br.com.brasolia.models;

import java.util.Date;
import java.util.Map;

/**
 * Created by cayke on 10/04/17.
 */

public class BSComment {
    String id;
    String itemID;
    BSUser owner;
    Date createdAt;
    String message;

    public BSComment(String id, Map<String, Object> dictionary) {
        this.id = id;

        itemID = (String) BSDictionary.getValueWithKeyAndType(dictionary, "item", String.class);

        Number timestamp = (Number) BSDictionary.getValueWithKeyAndType(dictionary, "timestamp", Number.class);
        createdAt = BSDate.getDate(timestamp.longValue());

        message = (String) BSDictionary.getValueWithKeyAndType(dictionary, "message", String.class);

        owner = new BSUser((Map<String, Object>) dictionary.get("user"));
    }

    public String getId() {
        return id;
    }

    public String getItemID() {
        return itemID;
    }

    public BSUser getOwner() {
        return owner;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getMessage() {
        return message;
    }
}
