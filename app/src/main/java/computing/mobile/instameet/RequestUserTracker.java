package computing.mobile.instameet;

import java.util.HashMap;

/**
 * Created by hgokhale on 4/24/16.
 */
public class RequestUserTracker {

    private static RequestUserTracker mInstance;
    private static HashMap<String,String> userTracker;

    protected RequestUserTracker(){
        userTracker = new HashMap<String, String>();
    }

    public static synchronized RequestUserTracker getInstance(){
        if(null == mInstance){
            mInstance = new RequestUserTracker();
        }
        return mInstance;
    }

    public static void flushMap(){
        userTracker = new HashMap<String, String>();
    }
    public static void put(String key, String value){
        userTracker.put(key,value);
    }
    public static String get(String key){
        if(userTracker.containsKey(key))
            return userTracker.get(key);
        return "INVALID_KEY";
    }

}
