package br.com.brasolia.models;

import java.util.Date;
import java.util.Map;

/**
 * Created by cayke on 10/04/17.
 */

public class BSComment {
    String id;
    String itemID;
    String ownerID;
    BSUser owner;
    Date createdAt;
    String message;

    public BSComment(String id, Map<String, Object> dictionary) {
        this.id = id;

        itemID = (String) BSDictionary.getValueWithKeyAndType(dictionary, "item", String.class);

        ownerID = (String) BSDictionary.getValueWithKeyAndType(dictionary, "user", String.class);

        long timestamp = ((Double) BSDictionary.getValueWithKeyAndType(dictionary, "timestamp", Double.class)).longValue();
        createdAt = BSDate.getDate(timestamp);

        message = (String) BSDictionary.getValueWithKeyAndType(dictionary, "message", String.class);
    }

    public String getId() {
        return id;
    }

    public String getItemID() {
        return itemID;
    }

    public String getOwnerID() {
        return ownerID;
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
