package computing.mobile.instameet;

public class UserGlobalData {
    private static UserGlobalData mInstance= null;
    private int N_INTERESTS = 10;

    public String username;
    public String password;
    public String displayName;
    public String email;
    public String phone;
    public String location;
    public boolean discover;
    public boolean locSharing;
    public int[] interests;
    public boolean isSignOut;
    public int rewards = 0;

    protected UserGlobalData(){
        interests = new int[N_INTERESTS];
    }

    public static synchronized UserGlobalData getInstance(){
        if(null == mInstance){
            mInstance = new UserGlobalData();
        }
        return mInstance;
    }
    public int getN_INTERESTS(){
        return N_INTERESTS;
    }
}