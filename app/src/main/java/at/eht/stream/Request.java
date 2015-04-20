package at.eht.stream;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Markus Deutsch
 */
public abstract class Request {

    private OnRequestCompleted callback;
    protected JSONObject requestJson;

    public Request(OnRequestCompleted callback){
        this.callback = callback;
    }

    protected abstract String getServiceName();

    protected abstract Response getResponse(String requestResult);

    public OnRequestCompleted getCallback(){
        return callback;
    }

    public final String getRequest(){
        JSONObject request = new JSONObject();
        try {
            request.put("request", requestJson);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return request.toString();
    }
}
