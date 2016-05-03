package computing.mobile.instameet;

/**
 * Created by harsh on 4/6/2016.
 */
import android.provider.BaseColumns;

/**
 * Created by harsh on 3/12/2016.
 */
public final class UserHistoryData {

    public UserHistoryData(){}

    public static abstract class UserHistoryDataEntry implements BaseColumns{
        public static final String DATABASE_NAME = "INSTAMEET_DATABASE.db";
        public static final String TABLE_NAME = "HISTORY_DATA";
        public static final String PK_HISTORY = "PK_HISTORY";
        public static final String TIMESTAMP = "TIMESTAMP";
        public static final String USERNAME = "USERNAME";
        public static final String COMPANION_NAME = "COMPANION_NAME";
    }
}
