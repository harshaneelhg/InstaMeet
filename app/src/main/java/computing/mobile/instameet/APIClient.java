package computing.mobile.instameet;

import android.content.Context;
import android.content.Intent;
import android.text.StaticLayout;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.*;
import org.json.*;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;


/**
 * Created by harsh on 3/30/2016.
 */
public class APIClient {

    public APIClient(){};

    static void getHistory(String username, String password, Context app_context) throws JSONException{
        final String uname = username;
        final Context context = app_context;
        RequestParams params = new RequestParams();
        params.put("username",username);
        params.put("password",password);
        params.setContentEncoding("UTF-8");
        InstaMeetRestClient.post("get_history/",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header [] headers, JSONObject response){
                System.out.println("Response received for getHistory()");
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
    static void sendLocation(final String username, final String password, String location){
        RequestParams params = new RequestParams();
        params.put("username",username);
        params.put("password",password);
        params.put("location",location);
        params.setContentEncoding("UTF-8");
        InstaMeetRestClient.post("update_location/",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("API_RESPONSE","Response received for update_location()");
                String status = "";
                try {
                    status = response.getString("status");
                    if(!("SUCCESS".equals(status))) {
                        Log.e("API_RESPONSE","Error Updating location. Server responded with message: "+ response.getString("message"));
                    }
                    else{
                        Log.d("API_RESPONSE","Updated location. Server responded with message: "+ response.getString("message"));
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
    static void  registerUser(String username, String password, String displayName){
        RequestParams params = new RequestParams();
        params.put("username",username);
        params.put("password",password);
        params.put("display_name",displayName);
        params.setContentEncoding("UTF-8");
        InstaMeetRestClient.post("register_user/",params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Log.d("API_RESPONSE", "Response received for register_user()");
                String status = "";
                try {
                    status = response.getString("status");
                    if(!("SUCCESS".equals(status))) {
                        Log.e("API_RESPONSE","Error Updating location. Server responded with message: "+ response.getString("message"));
                    }
                    else{
                        Log.d("API_RESPONSE","Updated location. Server responded with message: "+ response.getString("message"));
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    static void updateInterest(String username, String password, final String interestID, final String interestValue){
        RequestParams params = new RequestParams();
        params.put("username",username);
        params.put("password",password);
        params.put("interest_id",interestID);
        params.put("interest_value",interestValue);
        params.setContentEncoding("UTF-8");
        InstaMeetRestClient.post("update_interest/", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Log.d("API_RESPONSE", "Response received for register_user()");
                String status = "";
                try {
                    status = response.getString("status");
                    if(!("SUCCESS".equals(status))) {
                        Log.e("API_RESPONSE","Error Updating interests. Server responded with message: "+ response.getString("message"));
                    }
                    else{
                        Log.d("API_RESPONSE","Updated interests. Server responded with message: "+ response.getString("message"));
                        UserGlobalData ugd = UserGlobalData.getInstance();
                        ugd.interests[Integer.parseInt(interestID)] = Integer.parseInt(interestValue);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
    static void updateUser(String username, String old_password, final String new_password, String email, final String display_name, final String phone, final boolean discover, final boolean locSharing){
        RequestParams params = new RequestParams();
        params.put("username",username);
        params.put("password",old_password);
        params.put("new_password",new_password);
        params.put("email",email);
        params.put("phone",phone);
        params.put("display_name",display_name);
        params.put("discover", discover?"True":"False");
        params.put("location_sharing", locSharing?"True":"False");
        params.setContentEncoding("UTF-8");
        InstaMeetRestClient.post("update_user/", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Log.d("API_RESPONSE", "Response received for update_user()");
                String status = "";
                try {
                    status = response.getString("status");
                    if(!("SUCCESS".equals(status))) {
                        Log.e("API_RESPONSE","Error Updating location. Server responded with message: "+ response.getString("message"));
                    }
                    else{
                        Log.d("API_RESPONSE","Updated location. Server responded with message: "+ response.getString("message"));
                        UserGlobalData ugd =UserGlobalData.getInstance();
                        ugd.displayName = display_name;
                        ugd.password = new_password;
                        ugd.phone = phone;
                        ugd.discover = discover;
                        ugd.locSharing = locSharing;
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

    }
    static void findUsers(String username, String password, final Context activityContext){
        RequestParams params = new RequestParams();
        params.put("username",username);
        params.put("password",password);
        params.setContentEncoding("UTF-8");
        InstaMeetRestClient.post("get_nearby_users/",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Log.d("API_RESPONSE", "Response received for get_nearby_users()");
                String status = "";
                try {
                    status = response.getString("status");
                    if(!("SUCCESS".equals(status))) {
                        Log.e("API_RESPONSE","Error finding. Server responded with message: "+ response.getString("message"));
                    }
                    else{
                        Log.d("API_RESPONSE","Server responded with message: "+ response.getString("message"));
                        Intent near_users_intent = new Intent(activityContext, NearbyUsersActivity.class);
                        JSONArray users = response.getJSONArray("near_users");
                        for(int i=0;i<users.length(); i++) {
                            JSONObject user = (JSONObject) users.get(i);
                            String[] userEntry = new String[3];
                            userEntry[0] = user.getString("username");
                            userEntry[1] = user.getString("display_name");
                            userEntry[2] = user.getString("location_sharing").equals("True")?user.getString("distance"):"";
                            near_users_intent.putExtra("user"+i, userEntry);
                        }
                        activityContext.startActivity(near_users_intent);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
    static void sendRequest(String user1, String password, String user2, final Context applicationContext){
        RequestParams params = new RequestParams();
        params.put("user1",user1);
        params.put("password", password);
        params.put("user2",user2);
        params.setContentEncoding("UTF-8");
        InstaMeetRestClient.post("send_request/", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Log.d("API_RESPONSE", "Response received for send_request()");
                String status = "";
                try {
                    status = response.getString("status");
                    if("SUCCESS".equals(status)) {
                        Log.d("API_RESPONSE","Request sent. Server responded with message: "+ response.getString("message"));
                        Toast.makeText(applicationContext, "Request sent successfully!", Toast.LENGTH_LONG).show();
                    }
                    else if("DUPLICATE_REQUEST".equals(status)){
                        Log.e("API_RESPONSE","Duplicate request. Server responded with message: "+ response.getString("message"));
                        Toast.makeText(applicationContext, "Duplicate request", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Log.e("API_RESPONSE","Error sending request. Server responded with message: "+ response.getString("message"));
                        Toast.makeText(applicationContext, "Error sending request", Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
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
