package br.com.brasolia.models;

import java.util.Map;

/**
 * Created by breno on 11/09/2017.
 */

public class NewBSCategory {

    private String name;

    public NewBSCategory(Map<String, Object> objectMap){
        name = (String) objectMap.get("name");
    }

}
