package br.com.brasolia.models;

import java.util.Date;
import java.util.Map;

/**
 * Created by breno on 11/09/2017.
 */

public class NewBSDate {

    private Date end_date;
    private Date start_date;

    public NewBSDate(Map<String, Object> objectMap){
        end_date = (Date) objectMap.get("end_date");
        start_date = (Date) objectMap.get("start_date");
    }
}
