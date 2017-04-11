package br.com.brasolia.models;

import java.util.Date;
import java.util.Map;

/**
 * Created by cayke on 10/04/17.
 */

public class BSComment {
    String id;
    String eventID;
    BSUser owner;
    Date createAt;
    String message;

    BSComment(Map<String, Object> dictionary) {
        Map<String, Object> idDict = (Map<String, Object>) BSDictionary.getValueWithKeyAndType(dictionary, "id", Map.class);
        id = (String) BSDictionary.getValueWithKeyAndType(idDict, "$oid", String.class);

        Map<String, Object> eventDict = (Map<String, Object>) BSDictionary.getValueWithKeyAndType(dictionary, "event", Map.class);
        eventID = (String) BSDictionary.getValueWithKeyAndType(eventDict, "$oid", String.class);

        owner = new BSUser((Map<String, Object>) BSDictionary.getValueWithKeyAndType(dictionary, "user", Map.class));
        createAt = BSDate.getDateWithKey("created_at", dictionary);

        message = (String) BSDictionary.getValueWithKeyAndType(dictionary, "message", String.class);
    }
}
