package at.eht.stream.webservice;

import org.json.JSONException;

/**
 * @author Markus Deutsch
 */
public class SubmitBatchResponse extends Response {

    private String batchId;

    public SubmitBatchResponse(String responseString) {
        super(responseString);
        if(isSuccess()){
            try {
                this.batchId = resultObject.getJSONObject("response").getString("batchid");
            } catch (JSONException e) {
                this.result = "ERROR_JSON";
                this.message = e.getMessage();
            }
        }
    }

    public String getBatchId() {
        return batchId;
    }

}
