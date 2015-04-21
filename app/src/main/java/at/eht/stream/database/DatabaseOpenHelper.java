package at.eht.stream.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static at.eht.stream.database.SampleBatchDAO.*;

/**
 * @author Markus Deutsch
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static DatabaseOpenHelper mInstance;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "accelerationdata.db";

    public static synchronized DatabaseOpenHelper getInstance(Context context){
        if(mInstance == null){
            mInstance = new DatabaseOpenHelper(context);
        }

        return mInstance;
    }

    private DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String batchQuery = getCreateTableString(
                SampleBatchDAO.TABLE_NAME,
                // TODO: Maybe not the best idea to use a static import (once there are more tables)
                new String[]{FIELD_HASH, FIELD_TIMESTAMP, FIELD_SAMPLES},
                new String[]{"TEXT PRIMARY KEY", "TEXT", "TEXT"}
        );
        db.execSQL(batchQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String getCreateTableString(String tableName, String[] columnName, String[] columnType){
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(tableName);
        sb.append(" (");
        for (int i = 0; i < columnName.length; i++) {
            sb.append(columnName[i]);
            sb.append(" ");
            sb.append(columnType[i]);
            if(i != columnName.length - 1) sb.append(", ");
        }
        sb.append(");");
        Log.d("DatabaseOpenHelper", "Creating table " + tableName + ": " + sb.toString());
        return sb.toString();
    }
}
