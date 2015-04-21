package at.eht.stream.webservice;

import at.eht.stream.model.SampleBatch;

/**
 * @author Markus Deutsch
 */
public class SubmitBatchRequest extends Request {

    public SubmitBatchRequest(OnRequestCompleted callback, SampleBatch batch) {
        super(callback);
        setSampleBatch(batch);
    }

    @Override
    protected String getServiceName() {
        return "store";
    }

    @Override
    protected Response getResponse(String requestResult) {
        return new SubmitBatchResponse(requestResult);
    }

    private void setSampleBatch(SampleBatch batch){
        requestJson = batch.toJSON();
    }
}