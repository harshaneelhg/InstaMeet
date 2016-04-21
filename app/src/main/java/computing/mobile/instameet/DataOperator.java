package computing.mobile.instameet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

/**
 * Created by harsh on 4/7/2016.
 */
public class DataOperator extends SQLiteOpenHelper {
    public static int database_version = 1;
    private static String table_name = "";
    public String CREATE_QUERY = "";

    public DataOperator(Context context, String table_name) {
        super(context, UserHistoryData.UserHistoryDataEntry.DATABASE_NAME, null, database_version);
        this.table_name = table_name;
        this.CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + table_name + " ("
                + UserHistoryData.UserHistoryDataEntry.PK_HISTORY + " CHARACTER PRIMARY KEY, "
                + UserHistoryData.UserHistoryDataEntry.USERNAME + " CHARACTER, "
                + UserHistoryData.UserHistoryDataEntry.COMPANION_NAME + " CHARACTER, "
                + UserHistoryData.UserHistoryDataEntry.TIMESTAMP + " CHARACTER);";
        SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()+ "/" + UserHistoryData.UserHistoryDataEntry.DATABASE_NAME, null);
    }

    public void insertHistory(DataOperator dop, String pk_history, String companion_name, String username, String timestamp){
        SQLiteDatabase sqdb = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(UserHistoryData.UserHistoryDataEntry.PK_HISTORY, pk_history);
        cv.put(UserHistoryData.UserHistoryDataEntry.USERNAME, username);
        cv.put(UserHistoryData.UserHistoryDataEntry.COMPANION_NAME, companion_name);
        cv.put(UserHistoryData.UserHistoryDataEntry.TIMESTAMP, timestamp);
        long k = sqdb.insert(this.table_name,null,cv);
        Log.d("DB_INSERT", "Row inserted:(" + pk_history + ", " + companion_name + ", " + username + ", " + timestamp + ") Status:" + k);
    }

    public Cursor getHistory(DataOperator dop, String username){
        SQLiteDatabase sqdb = dop.getReadableDatabase();
        String [] columns = {UserHistoryData.UserHistoryDataEntry.COMPANION_NAME, UserHistoryData.UserHistoryDataEntry.TIMESTAMP};
        Cursor cursor = sqdb.query(this.table_name, columns, UserHistoryData.UserHistoryDataEntry.USERNAME + "=\"" + username+"\"" , null, null, null, null);
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.d("DB_CREATE", "Database Created using "+CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_QUERY);
        Log.d("DB_CREATE", "Database Created using "+CREATE_QUERY);
    }
}
