package at.eht.stream.webservice;

import org.json.JSONException;
import org.json.JSONObject;

import at.eht.stream.Config;
import at.eht.stream.model.SampleBatch;

/**
 * @author Markus Deutsch
 */
public class SubmitBatchRequest extends Request {

    // TODO: Set sample rate and batch name.

    public SubmitBatchRequest(OnRequestCompleted callback, SampleBatch batch, String datasetName) {
        super(callback);
        setSampleBatch(batch);
        setSampleRate(Config.SAMPLERATE);
        setDatasetName(datasetName);
    }

    @Override
    protected String getServiceName() {
        return "store";
    }

    @Override
    protected Response getResponse(String requestResult) {
        return new SubmitBatchResponse(requestResult);
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

    private void setSampleBatch(SampleBatch batch){
        try {
            requestJson.put("sampleBatch", batch.toJSON());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
