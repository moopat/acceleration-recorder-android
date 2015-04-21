package at.eht.stream.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import at.eht.stream.model.SampleBatch;

/**
 * @author Markus Deutsch
 */
public class SampleBatchDAO {

    private static Context context;

    public static final String TABLE_NAME = "samplebatch";
    public static final String FIELD_HASH = "hash";
    public static final String FIELD_TIMESTAMP = "timestamp";
    public static final String FIELD_SAMPLES = "samples";

    public static void init(Context context) {
        SampleBatchDAO.context = context;
    }

    public static boolean insert(SampleBatch batch) {
        long successful = -1;
        SQLiteDatabase db = DatabaseOpenHelper.getInstance(context).getWritableDatabase();
        try {
            db.beginTransaction();

            ContentValues cv = new ContentValues();
            cv.put(FIELD_HASH, batch.getHash());
            cv.put(FIELD_TIMESTAMP, batch.getTimestamp());
            cv.put(FIELD_SAMPLES, batch.toJSON().toString());

            successful = db.insert(TABLE_NAME, null, cv);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            //db.close();
        }

        return successful > -1;
    }

    public static List<SampleBatch> readAll() {
        Cursor cursor = null;
        SQLiteDatabase db = DatabaseOpenHelper.getInstance(context).getReadableDatabase();
        List<SampleBatch> all = new ArrayList<SampleBatch>();

        try {

            cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" ORDER BY "+FIELD_TIMESTAMP+" DESC", null);

            if(cursor.getCount() > 0)
            {
                //int hashIndex = cursor.getColumnIndex(FIELD_HASH);
                //int timestampIndex = cursor.getColumnIndex(FIELD_TIMESTAMP);
                int samplesIndex = cursor.getColumnIndex(FIELD_SAMPLES);

                cursor.moveToFirst();
                do {
                    String sampleBatchAsJson = cursor.getString(samplesIndex);
                    all.add(new SampleBatch(sampleBatchAsJson));

                    cursor.moveToNext();
                } while(!cursor.isAfterLast());
            }

        } finally {

            try {
                cursor.close();
                //db.close();
            } catch (Exception e){

            }
        }

        return all;
    }

    public static void deleteAll() {
        SQLiteDatabase db = DatabaseOpenHelper.getInstance(context).getWritableDatabase();
        try
        {
            db.beginTransaction();
            String delete = String.format("DELETE FROM %s;", TABLE_NAME);
            db.execSQL(delete);

            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
            //db.close();
        }

    }
}
