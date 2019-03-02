package am.victor.graphhoppermapmatching.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import am.victor.graphhoppermapmatching.models.TrackPoint;

public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all Track Points from the database.
     *
     * @return a List of Track Points
     */
    public List<TrackPoint> getTrackPoints() {
        List<TrackPoint> trackPointsArrayList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM tbl_distance", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            double tripLatitude = cursor.getDouble(cursor.getColumnIndex("trip_lat"));
            double tripLongitude = cursor.getDouble(cursor.getColumnIndex("trip_lng"));
            trackPointsArrayList.add(new TrackPoint(tripLatitude, tripLongitude));
            cursor.moveToNext();
        }
        cursor.close();
        return trackPointsArrayList;
    }

}