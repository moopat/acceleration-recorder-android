package at.eht.stream.webservice;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Markus Deutsch
 */
public class Response {

    String responseString;
    String result;
    String message;

    JSONObject resultObject;

    public Response(String responseString){
        this.responseString = responseString;
        try {
            this.resultObject = new JSONObject(responseString);
            this.result = resultObject.getJSONObject("response").getString("status");
            this.message = resultObject.getJSONObject("response").optString("message", null);
        } catch (JSONException e) {
            e.printStackTrace();
            this.result = "ERROR_CLIENT";
            this.message = "The response could not be deserialized on the client.";
        }
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess(){
        return result != null && result.equalsIgnoreCase("OK");
    }
}
