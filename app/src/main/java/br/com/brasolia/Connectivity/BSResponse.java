package br.com.brasolia.Connectivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Created by cayke on 07/04/17.
 */

public class BSResponse {
    public enum ResponseStatus {
        BSResponseSuccess, BSResponseError
    }


    private ResponseStatus status;
    private String type;
    private Object data;


    public BSResponse(JsonObject responseObject) {
        Map<String, Object> result = new Gson().fromJson(responseObject, Map.class);

        String responseStatus = (String) result.get("status");
        if (responseStatus.equals("success")) {
            status = ResponseStatus.BSResponseSuccess;
        } else {
            status = ResponseStatus.BSResponseError;
        }

        type = (String) result.get("type");
        data = result.get("data");

        checkForUserDenied();
    }

    private void checkForUserDenied() {
        if (status == ResponseStatus.BSResponseError && type.equals("access_denied")) {
            killUser();
        } else if (status == ResponseStatus.BSResponseError && type.equals("not_authorized")) {
            killUser();
        }
    }

    private void killUser() {
        //todo matar user
//        if (IBMainData.getInstance().getUser() != null)
//            IBMainData.getInstance().logOutUser();
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}
