package computing.mobile.instameet;

import android.content.Context;

import com.loopj.android.http.*;
import org.json.*;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;


/**
 * Created by harsh on 3/30/2016.
 */
public class APIClient {

    private static String API_URL = "http://10.0.2.2:8001/api/instameet/";

    public APIClient(){};

    static void getHistory(String username, String password, Context app_context) throws JSONException{
        final String uname = username;
        final Context context = app_context;
        final ArrayList results = new ArrayList();
        RequestParams params = new RequestParams();
        params.put("username",username);
        params.put("password",password);
        params.setContentEncoding("UTF-8");
        InstaMeetRestClient.post("get_history/",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header [] headers, JSONObject response){
                System.out.println("Success!!");
                String status = "";
                try {
                    status = response.getString("status");
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                if(!("SUCCESS".equals(status)))
                    return;

                JSONArray history = new JSONArray();
                try {
                    history = response.getJSONArray("history");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                for (int i = 0; i < history.length(); i++) {
                    try {
                        JSONObject entry = history.getJSONObject(i);
                        String companion = ((String) entry.get("user1")).equals(uname) ? (String) entry.get("user2") : (String) entry.get("user1");
                        DataOperator dop = new DataOperator(context,UserHistoryData.UserHistoryDataEntry.TABLE_NAME);
                        dop.insertHistory(dop, (String)entry.get("pk_history"), companion, uname, (String)entry.get("timestamp"));
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public static void main() throws Exception{
        UserGlobalData ugd = UserGlobalData.getInstance();
        ugd.username = "harsh";
        ugd.password = "abcd";
        ugd.email = "harsh@asu.edu";
        ugd.phone = "9898989898";

        //getHistory(ugd.username, ugd.password);
    }
}
