package at.eht.stream.webservice;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Markus Deutsch
 */
public class SubmitLargeBatchResponse extends Response {

    private List<String> batchIds;

    public SubmitLargeBatchResponse(String responseString) {
        super(responseString);
        if(isSuccess()){
            try {
                batchIds = new ArrayList<>();
                JSONArray batchIdsArray = resultObject.getJSONObject("response").getJSONArray("batchids");
                for(int i = 0; i < batchIdsArray.length(); i++){
                    batchIds.add(batchIdsArray.getString(i));
                }
            } catch (JSONException e) {
                this.result = "ERROR_JSON";
                this.message = e.getMessage();
            }
        }
    }

    public List<String> getBatchIds() {
        return batchIds;
    }

}
