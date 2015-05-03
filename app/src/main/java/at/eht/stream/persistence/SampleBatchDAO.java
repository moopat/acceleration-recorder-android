package at.eht.stream.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
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

    public static SampleBatch findByHash(String hash){
        Cursor cursor = null;
        SQLiteDatabase db = DatabaseOpenHelper.getInstance(context).getReadableDatabase();
        SampleBatch batch = null;

        cursor = db.query(
                TABLE_NAME,
                new String[]{FIELD_SAMPLES},
                FIELD_HASH + "=?",
                new String[]{hash},
                null,
                null,
                null);

        if(cursor.getCount() == 1){
            int samplesIndex = cursor.getColumnIndex(FIELD_SAMPLES);
            cursor.moveToFirst();
            batch = new SampleBatch(cursor.getString(samplesIndex));
            cursor.close();
        }

        return batch;
    }

    public static SampleBatch findNext(){
        Cursor cursor = null;
        SQLiteDatabase db = DatabaseOpenHelper.getInstance(context).getReadableDatabase();
        SampleBatch batch = null;

        cursor = db.query(
                TABLE_NAME,
                new String[]{FIELD_SAMPLES},
                null,
                null,
                null,
                null,
                FIELD_TIMESTAMP + " ASC",
                "1");

        if(cursor.getCount() == 1){
            int samplesIndex = cursor.getColumnIndex(FIELD_SAMPLES);
            cursor.moveToFirst();
            batch = new SampleBatch(cursor.getString(samplesIndex));
            cursor.close();
        }

        return batch;
    }

    public static List<SampleBatch> findNextFew(int count){
        Cursor cursor = null;
        SQLiteDatabase db = DatabaseOpenHelper.getInstance(context).getReadableDatabase();
        List<SampleBatch> batches = new ArrayList<>();

        cursor = db.query(
                TABLE_NAME,
                new String[]{FIELD_SAMPLES},
                null,
                null,
                null,
                null,
                FIELD_TIMESTAMP + " ASC",
                String.valueOf(count));

        if(cursor.getCount() > 0) {
            int samplesIndex = cursor.getColumnIndex(FIELD_SAMPLES);
            cursor.moveToFirst();
            do {
                String sampleBatchAsJson = cursor.getString(samplesIndex);
                batches.add(new SampleBatch(sampleBatchAsJson));

                cursor.moveToNext();
            } while(!cursor.isAfterLast());
        }

        return batches;
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
            if(cursor != null) cursor.close();
        }

        return all;
    }

    public static void deleteAll() {
        SQLiteDatabase db = DatabaseOpenHelper.getInstance(context).getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public static boolean deleteByHash(String hash){
        SQLiteDatabase db = DatabaseOpenHelper.getInstance(context).getWritableDatabase();
        return db.delete(TABLE_NAME, FIELD_HASH + "=?", new String[]{hash}) == 1;
    }

    public static long getNumberOfBatches(){
        SQLiteDatabase db = DatabaseOpenHelper.getInstance(context).getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }
}
