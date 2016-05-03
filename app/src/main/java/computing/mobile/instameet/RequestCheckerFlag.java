package computing.mobile.instameet;

/**
 * Created by hgokhale on 4/23/16.
 */
public class RequestCheckerFlag {

    private static RequestCheckerFlag mInstance = null;
    private static boolean isAppFinished = false;
    protected RequestCheckerFlag(){}

    public static boolean isAppFinished() {
        return isAppFinished;
    }

    public static void setIsAppFinished(boolean isAppFinished) {
        RequestCheckerFlag.isAppFinished = isAppFinished;
    }

    public static synchronized RequestCheckerFlag getInstance(){
        if(null == mInstance){
            mInstance = new RequestCheckerFlag();
        }
        return mInstance;
    }

}
