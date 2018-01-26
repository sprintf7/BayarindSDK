package net.sprintasia.bayarind.listener;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ops1 on 12/01/2018.
 */

public interface OnApiListener {
    void onSuccess(Boolean status, String message, JSONObject data);
    void onSuccess(Boolean status, String message, JSONArray data);
    void onSuccess(Boolean status, String message, String data);
    void onFailed();
}
