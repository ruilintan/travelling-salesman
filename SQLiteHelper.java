package melissatan.androidproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by melissatan on 24/11/17.
 */

public class SQLiteHelper extends SQLiteAssetHelper {
//We are creating a java file called SQLiteHelper and extending SQLiteOpenHelper class and It is used to create a bridge between android and SQLite.
//        To perform basic SQL operations we need to extend SQLiteOpenHelper class.

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "androidproject.db";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String TABLE_NAME = "costs";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_1 = "from";
    public static final String COLUMN_2 = "to";
    public static final String COLUMN_3 = "cost_publictransport";
    public static final String COLUMN_4 = "cost_taxi";
    public static final String COLUMN_5 = "cost_foot";
    public static final String COLUMN_6 = "time_publictransport";
    public static final String COLUMN_7 = "time_taxi";
    public static final String COLUMN_8 = "time_foot";

    float getTravelCost(int from, int to, ItineraryPlanning.TransportType transportMode) {
        SQLiteDatabase db = getReadableDatabase();

        String transportModeName = "";
        switch (transportMode) {
            case PUBLIC_TRANSPORT:
                transportModeName = "cost_publictransport";
                break;
            case TAXI:
                transportModeName = "cost_taxi";
                break;
            case FOOT:
                transportModeName = "cost_foot";
                break;
        }

        String query = "SELECT " + transportModeName + " FROM costs WHERE `from`=" + from + " AND `to`=" + to;
        Cursor cursor = db.rawQuery(query, null);
        float returnVal = 0;
        if (cursor != null && cursor.moveToFirst()) {
            returnVal = cursor.getFloat(0);
            cursor.close();
        }
        return returnVal;
    }

    float getTravelTime(int from, int to, ItineraryPlanning.TransportType transportMode) {
        SQLiteDatabase db = getReadableDatabase();

        String transportModeName = "";
        switch (transportMode) {
            case PUBLIC_TRANSPORT:
                transportModeName = "time_publictransport";
                break;
            case TAXI:
                transportModeName = "time_taxi";
                break;
            case FOOT:
                transportModeName = "time_foot";
                break;
        }

        String query = "SELECT " + transportModeName + " FROM costs WHERE `from`=" + from + " AND `to`=" + to;
        Cursor cursor = db.rawQuery(query, null);
        float returnVal = 0;
        if (cursor != null && cursor.moveToFirst()) {
            returnVal = cursor.getFloat(0);
            cursor.close();
        }

        return returnVal;
    }

}
