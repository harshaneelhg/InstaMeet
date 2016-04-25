package computing.mobile.instameet;

import android.provider.BaseColumns;

/**
 * Created by hgokhale on 4/23/16.
 */
public class UserRequestData {
    public UserRequestData(){}

    public static abstract class UserRequestDataEntry implements BaseColumns{
        public static final String DATABASE_NAME = "INSTAMEET_USER_REQUESTS.db";
        public static final String TABLE_NAME = "USER_REQUESTS";
        public static final String PK_REQUESTS = "PK_REQUESTS";
        public static final String USER1 = "USER1";
        public static final String USER2 = "USER2";
        public static final String STATUS = "STATUS";
        public static final String USER_EMAIL = "USER_EMAIL";
        public static final String USER_PHONE = "USER_PHONE";
    }

}
