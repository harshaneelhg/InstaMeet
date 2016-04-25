package computing.mobile.instameet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

/**
 * Created by hgokhale on 4/23/16.
 */
public class UserHistoryOperator extends SQLiteOpenHelper {
    public static int database_version = 1;
    private static String table_name = "";
    public String CREATE_QUERY = "";

    public UserHistoryOperator(Context context, String table_name) {
        super(context, UserRequestData.UserRequestDataEntry.DATABASE_NAME, null, database_version);
        this.table_name = table_name;
        this.CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + table_name + " ("
                + UserRequestData.UserRequestDataEntry.PK_REQUESTS + " CHARACTER PRIMARY KEY, "
                + UserRequestData.UserRequestDataEntry.USER1 + " CHARACTER, "
                + UserRequestData.UserRequestDataEntry.USER2 + " CHARACTER, "
                + UserRequestData.UserRequestDataEntry.STATUS + " CHARACTER, "
                + UserRequestData.UserRequestDataEntry.USER_EMAIL + " CHARACTER, "
                + UserRequestData.UserRequestDataEntry.USER_PHONE + " CHARACTER);";
        SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()+ "/com.mobile.instameet/" + UserRequestData.UserRequestDataEntry.DATABASE_NAME, null);
    }

    public Cursor getRequest(UserHistoryOperator dop, String username, String status){
        SQLiteDatabase sqdb = dop.getReadableDatabase();
        String [] columns = {UserRequestData.UserRequestDataEntry.USER1, UserRequestData.UserRequestDataEntry.USER_EMAIL, UserRequestData.UserRequestDataEntry.USER_PHONE};
        String WHERE = UserRequestData.UserRequestDataEntry.STATUS + "=\"" + status+"\" AND " + UserRequestData.UserRequestDataEntry.USER2 + "=\"" + username + "\"";
        Cursor cursor = sqdb.query(this.table_name, columns, WHERE , null, null, null, null);
        return cursor;
    }

    public void insertRequest(UserHistoryOperator dop,String pkRequest, String user1, String user2, String status, String userEmail, String userPhone){
        SQLiteDatabase sqdb = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(UserRequestData.UserRequestDataEntry.PK_REQUESTS, pkRequest);
        cv.put(UserRequestData.UserRequestDataEntry.USER1, user1);
        cv.put(UserRequestData.UserRequestDataEntry.USER2, user2);
        cv.put(UserRequestData.UserRequestDataEntry.STATUS, status);
        cv.put(UserRequestData.UserRequestDataEntry.USER_EMAIL, userEmail);
        cv.put(UserRequestData.UserRequestDataEntry.USER_PHONE, userPhone);
        long k = sqdb.insert(this.table_name,null,cv);
        Log.d("DB_INSERT", "Row inserted:(" + user1 + ", " + user2 + ", " + status + ", " + userEmail + ", " + userPhone + ") Status:" + k);
    }

    public void updateStatus(UserHistoryOperator dop, String user1, String user2, String newStatus){
        SQLiteDatabase sqdb = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(UserRequestData.UserRequestDataEntry.STATUS, newStatus);
        sqdb.update(UserRequestData.UserRequestDataEntry.TABLE_NAME,
                cv,
                UserRequestData.UserRequestDataEntry.USER1 + " = ? AND " + UserRequestData.UserRequestDataEntry.USER2 + " = ?",
                new String[]{user1, user2});
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.d("DB_CREATE", "Request database created using "+CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_QUERY);
        Log.d("DB_CREATE", "Request database upgraded using "+CREATE_QUERY);
    }
}
