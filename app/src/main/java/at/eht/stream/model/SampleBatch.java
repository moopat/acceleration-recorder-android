package at.eht.stream.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import at.eht.stream.Util;

/**
 * @author Markus Deutsch
 */
public class SampleBatch {

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());

    static {
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private Sample[] samples;
    private String timestamp;
    private String hash;

    public SampleBatch(Sample[] samples) {
        this.samples = samples;
        this.timestamp = dateFormatter.format(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis());
        generateHash();
    }

    public SampleBatch(String jsonString) {
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
            this.hash = json.getString("hash");
            this.timestamp = json.getString("timestamp");

            JSONArray jsonSamples = json.getJSONArray("samples");
            this.samples = new Sample[jsonSamples.length()];
            for(int i = 0; i < jsonSamples.length(); i++){
                samples[i] = new Sample(jsonSamples.getJSONObject(i));
            }
            //generateHash(); Don't regenerate hash because the implementation might change.
        } catch (JSONException e) {
            throw new InstantiationError("Error inflating SampleBatch from JSON: " + jsonString);
        }

    }

    public Sample[] getSamples() {
        return samples;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getHash() {
        return hash;
    }

    private void generateHash(){
        StringBuilder sb = new StringBuilder();
        for (Sample sample : samples) {
            sb.append(sb.toString());
        }
        sb.append(timestamp);

        try{
            hash = Util.getSHA256(sb.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("SampleBatch", e.getMessage());
            hash = sb.toString().substring(0, Math.min(64, sb.toString().length()));
        }
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();

        try {
            json.put("timestamp", timestamp);
            json.put("hash", hash);

            JSONArray jsonArray = new JSONArray();
            for (Sample sample : samples) {
                jsonArray.put(sample.toJSON());
            }

            json.put("samples", jsonArray);
        } catch (JSONException e){
            throw new RuntimeException(e);
        }

        return json;
    }
}
