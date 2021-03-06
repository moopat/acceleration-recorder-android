package at.eht.stream.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import at.eht.stream.persistence.DatasetMetadataManager;
import at.eht.stream.persistence.SampleBatchDAO;
import at.eht.stream.model.SampleBatch;
import at.eht.stream.webservice.OnRequestCompleted;
import at.eht.stream.webservice.RequestPosterTask;
import at.eht.stream.webservice.Response;
import at.eht.stream.webservice.SubmitBatchRequest;
import at.eht.stream.webservice.SubmitBatchResponse;
import at.eht.stream.webservice.SubmitLargeBatchRequest;
import at.eht.stream.webservice.SubmitLargeBatchResponse;

/**
 * @author Markus Deutsch
 */
public class UploadService extends Service {

    public final String LOG_TAG = "UploadService";
    private int errorCount = 0;
    private final int maxErrorCount = 3;
    private long numberOfTotalUploads = 0;
    private long numberOfTransmissions = 0;

    public static final String KEY_TOTAL = "totalUploads";
    public static final String KEY_CURRENT = "currentUploads";
    public static final String ACTION_DATAUPLOADED = "at.eht.stream.ACTION_DATA_UPLOADED";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SampleBatchDAO.init(this);
        startUploading();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startUploading(){
        numberOfTotalUploads = SampleBatchDAO.getNumberOfBatches();
        numberOfTransmissions = 0;
        sendDataUpdatedBroadcast();
        uploadNextFew();
    }

    private void uploadNextFew(){
        List<SampleBatch> sampleBatches = SampleBatchDAO.findNextFew(5);
        if(sampleBatches == null || sampleBatches.size() < 1 || numberOfTransmissions >= numberOfTotalUploads){
            stopSelf();
            return;
        }

        new RequestPosterTask().execute(new SubmitLargeBatchRequest(new OnRequestCompleted() {
            @Override
            public void onSuccess(Response response) {
                SubmitLargeBatchResponse batchResponse = (SubmitLargeBatchResponse) response;
                Log.d(LOG_TAG, "Successfully transmitted batch: " + Arrays.toString(batchResponse.getBatchIds().toArray()));
                List<String> batchIds = batchResponse.getBatchIds();
                for (String batchId : batchIds) {
                    if(SampleBatchDAO.deleteByHash(batchId)){
                        errorCount = 0;
                        numberOfTransmissions++;
                        Log.d(LOG_TAG, "Deleting " + batchId + " OK.");
                    } else {
                        errorCount++;
                        Log.e(LOG_TAG, "Deleting " + batchId + " failed.");
                    }
                }
                sendDataUpdatedBroadcast();
                uploadNextFew();
            }

            @Override
            public void onError(Response response) {
                errorCount++;
                if(errorCount < maxErrorCount){
                    uploadNextFew();
                } else {
                    stopSelf();
                }
            }
        }, sampleBatches, DatasetMetadataManager.getInstance(this).getDatasetTitle()));

    }

    private void uploadNext(){
        SampleBatch sampleBatch = SampleBatchDAO.findNext();
        if(sampleBatch == null || numberOfTransmissions >= numberOfTotalUploads){
            stopSelf();
            return;
        }

        new RequestPosterTask().execute(new SubmitBatchRequest(new OnRequestCompleted() {
            @Override
            public void onSuccess(Response response) {
                SubmitBatchResponse batchResponse = (SubmitBatchResponse) response;
                Log.d(LOG_TAG, "Successfully transmitted batch: " + batchResponse.getBatchId());
                if(SampleBatchDAO.deleteByHash(batchResponse.getBatchId())){
                    errorCount = 0;
                    numberOfTransmissions++;
                    sendDataUpdatedBroadcast();
                } else {
                    errorCount++;
                }
                uploadNext();
            }

            @Override
            public void onError(Response response) {
                errorCount++;
                if(errorCount < maxErrorCount){
                    uploadNext();
                } else {
                    stopSelf();
                }
            }
        }, sampleBatch, DatasetMetadataManager.getInstance(this).getDatasetTitle()));

    }

    private void sendDataUpdatedBroadcast(){
        Intent intent = new Intent(ACTION_DATAUPLOADED);
        intent.putExtra(KEY_CURRENT, numberOfTransmissions);
        intent.putExtra(KEY_TOTAL, numberOfTotalUploads);
        sendBroadcast(intent);
    }

}
