package at.eht.stream.persistence;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Markus Deutsch
 */
public class DatasetMetadataManager {

    private static SharedPreferences mPreferences;
    private static DatasetMetadataManager mInstance;

    private static final String PREF_NAME = "METADATA";
    private static final String PREF_KEY_TITLE = "datasettitle";

    private DatasetMetadataManager(Context context){
        mPreferences = context.getSharedPreferences("METADATA", Context.MODE_PRIVATE);
    }

    public static DatasetMetadataManager getInstance(Context context){
        if(mInstance == null)
            mInstance = new DatasetMetadataManager(context);
        return mInstance;
    }

    public String getDatasetTitle(){
        return mPreferences.getString(PREF_KEY_TITLE, null);
    }

    public void setDatasetTitle(String title){
        mPreferences.edit().putString(PREF_KEY_TITLE, title).apply();
    }
}
