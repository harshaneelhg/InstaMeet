package computing.mobile.instameet;

public class UserGlobalData {
    private static UserGlobalData mInstance= null;

    public String username;
    public String password;
    public String email;
    public String phone;

    protected UserGlobalData(){}

    public static synchronized UserGlobalData getInstance(){
        if(null == mInstance){
            mInstance = new UserGlobalData();
        }
        return mInstance;
    }
}