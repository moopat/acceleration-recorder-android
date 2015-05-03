package at.eht.stream.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import at.eht.stream.R;
import at.eht.stream.Util;
import at.eht.stream.persistence.DatasetMetadataManager;
import at.eht.stream.persistence.SampleBatchDAO;
import at.eht.stream.service.AccelerationDataReceiverService;
import at.eht.stream.service.UploadService;

/**
 * @author Markus Deutsch
 */
public class MainActivity extends ActionBarActivity {

    private final static String LOG_TAG = "MainActivity";

    private TextView tvSampleCount, tvProgressDescription;
    private EditText etDatasetName;
    private Button btnUuid, btnSave;
    private ProgressBar pbProgress;
    private int count;

    private BroadcastReceiver dataUploadedReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvSampleCount = (TextView) findViewById(R.id.tvSampleCount);
        SampleBatchDAO.init(this);

        if(savedInstanceState != null && savedInstanceState.containsKey("COUNT")){
            count = savedInstanceState.getInt("COUNT", 0);
        } else {
            count = (int) SampleBatchDAO.getNumberOfBatches();
        }

        pbProgress = (ProgressBar) findViewById(R.id.pbProgress);
        tvProgressDescription = (TextView) findViewById(R.id.tvProgressDescription);
        etDatasetName = (EditText) findViewById(R.id.etDataSetName);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnUuid = (Button) findViewById(R.id.btnAddUid);

        updateCount();

        findViewById(R.id.btnUpload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, UploadService.class));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatasetMetadataManager.getInstance(MainActivity.this).
                        setDatasetTitle(etDatasetName.getText().toString());
            }
        });

        btnUuid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = etDatasetName.getText().toString();
                String randomString = Util.getRandomString();

                if(currentText.lastIndexOf("-") > -1){
                    currentText = currentText.substring(0, currentText.lastIndexOf("-") + 1);
                } else {
                    currentText = currentText + "-";
                }

                currentText = currentText + randomString;
                etDatasetName.setText(currentText);
                DatasetMetadataManager.getInstance(MainActivity.this).
                        setDatasetTitle(currentText);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent pebbleServiceIntent = new Intent(this, AccelerationDataReceiverService.class);
        startService(pebbleServiceIntent);

        setupDataUploadedReceiver();
        etDatasetName.setText(DatasetMetadataManager.getInstance(this).getDatasetTitle());
    }

    public void updateCount(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                tvSampleCount.setText(String.valueOf(count));
            }
        });
    }

    public void updateProgress(long total, long current){
        int progress = total == 0 ? 0 : getPercentage(total, current);
        tvSampleCount.setText(String.valueOf(total - current));
        tvProgressDescription.setText(getString(R.string.upload_progress, progress, current, total));
        pbProgress.setMax((int) total);
        pbProgress.setProgress((int) current);
    }

    public int getPercentage(long total, long current){
        return (int) (((float) current / (float) total) * 100);
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
        unregisterReceiver(dataUploadedReceiver);
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

    private void setupDataUploadedReceiver(){
        dataUploadedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateProgress(
                        intent.getLongExtra(UploadService.KEY_TOTAL, 0),
                        intent.getLongExtra(UploadService.KEY_CURRENT, 0)
                );
            }
        };
        registerReceiver(dataUploadedReceiver, new IntentFilter(UploadService.ACTION_DATAUPLOADED));
    }
}
