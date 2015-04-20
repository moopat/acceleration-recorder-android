package at.eht.stream;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author Markus Deutsch
 */
public class RequestPosterTask extends AsyncTask<Request, Void, Response> {

    OnRequestCompleted callback;

    @Override
    protected Response doInBackground(Request... params) {
        Request request = params[0];
        callback = request.getCallback();
        String result = "";

        try {
            URL connectionUrl = new URL(String.format(Config.WEBSERVICE_LINK, request.getServiceName()));
            HttpURLConnection urlConnection = (HttpURLConnection) connectionUrl.openConnection();

            Log.d("RequestPosterTask", "Posting to " + connectionUrl.getPath());
            Log.d("RequestPosterTask", request.getRequest());


            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream outStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
            writer.write("request=" + URLEncoder.encode(request.getRequest(), "UTF-8"));
            writer.flush();
            writer.close();
            outStream.close();

            // Get response
            InputStream is = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder responseBuilder = new StringBuilder();
            while((line = reader.readLine()) != null){
                responseBuilder.append(line);
                responseBuilder.append('\r');
            }
            reader.close();
            result = responseBuilder.toString();
            Log.d("RequestPosterTask", result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return request.getResponse(result);
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);

        Log.d("RequestPosterTask", response.getResponseString());

        if(response.getResult().equalsIgnoreCase("OK")){
            callback.onSuccess(response);
        } else {
            callback.onError(response);
        }
    }
}
