package at.eht.stream.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import at.eht.stream.R;
import at.eht.stream.database.SampleBatchDAO;
import at.eht.stream.service.AccelerationDataReceiverService;

/**
 * @author Markus Deutsch
 */
public class MainActivity extends ActionBarActivity {

    private final static String LOG_TAG = "MainActivity";

    private TextView tvStatus;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        ((TextView) findViewById(R.id.tvMaxNumber)).setText(getString(R.string.retention, Integer.MAX_VALUE));

        if(savedInstanceState != null && savedInstanceState.containsKey("COUNT")){
            count = savedInstanceState.getInt("COUNT", 0);
            updateCount();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent pebbleServiceIntent = new Intent(this, AccelerationDataReceiverService.class);
        startService(pebbleServiceIntent);

        SampleBatchDAO.init(this);
    }

    public void updateCount(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                tvStatus.setText(getString(R.string.count, String.valueOf(count)));
            }
        });
    }

    public void exportData(){
        try {
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "acceldata");
            if(!dir.exists()) dir.mkdirs();
            File outputFile = new File(dir.getAbsolutePath(), String.valueOf("accel-"+new Date().getTime()) + ".csv");
            Log.i(LOG_TAG, "Saving output to " + outputFile.getAbsolutePath());
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            //for (Sample sample : collection.getSnapshot()) {
            //    outputStream.write((sample.toString()+"\n").getBytes());
            //}
            outputStream.close();
            Toast.makeText(this, "Export finished.", Toast.LENGTH_SHORT).show();
        } catch (IOException e){
            Toast.makeText(this, "Export failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_export) {
            exportData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("COUNT", count);
        super.onSaveInstanceState(outState);
    }
}
