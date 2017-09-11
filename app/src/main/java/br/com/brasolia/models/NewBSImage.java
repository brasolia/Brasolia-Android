package br.com.brasolia.models;

import java.util.Map;

/**
 * Created by breno on 11/09/2017.
 */

public class NewBSImage {

    private String url;

    public NewBSImage(Map<String, Object> objectMap) {
        url = (String) objectMap.get("url");
    }
}
