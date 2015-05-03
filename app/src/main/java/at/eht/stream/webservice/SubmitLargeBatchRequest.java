package at.eht.stream.webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import at.eht.stream.Config;
import at.eht.stream.model.SampleBatch;

/**
 * @author Markus Deutsch
 */
public class SubmitLargeBatchRequest extends Request {

    public SubmitLargeBatchRequest(OnRequestCompleted callback, List<SampleBatch> batches, String datasetName) {
        super(callback);
        setSampleBatches(batches);
        setSampleRate(Config.SAMPLERATE);
        setDatasetName(datasetName);
    }

    @Override
    protected String getServiceName() {
        return "multistore";
    }

    @Override
    protected Response getResponse(String requestResult) {
        return new SubmitLargeBatchResponse(requestResult);
    }

    private void setDatasetName(String datasetName){
        try {
            requestJson.put("datasetName", datasetName == null ? JSONObject.NULL : datasetName);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void setSampleRate(int sampleRate){
        try {
            requestJson.put("sampleRate", sampleRate);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void setSampleBatches(List<SampleBatch> batches){
        try {
            JSONArray sampleBatches = new JSONArray();
            for (SampleBatch batch : batches) {
                sampleBatches.put(batch.toJSON());
            }
            requestJson.put("sampleBatches", sampleBatches);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
