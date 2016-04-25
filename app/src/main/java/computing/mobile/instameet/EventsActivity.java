package computing.mobile.instameet;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yelp.clientlib.connection.*;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Coordinate;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.BoundingBoxOptions;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

import retrofit.Call;
import retrofit.Response;

public class EventsActivity extends AppCompatActivity {
    private ArrayList<Business> businesses;
    private Response<SearchResponse> response;
    private boolean found = false;
    private Object lock = new Object();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        Consumer Key 	aPvXqXfyK1QIqe2M1LPdwg
Consumer Secret 	TYhUXOaxnhwMxu2KYPPvOkYk7OA
Token 	GEYqzl6lRkzLPQyiKRoSegHYq7G5FJKh
Token Secret 	11nx56ticbpKYeCE1vEJYywX4vM
         */
        final RadioGroup rgroup = (RadioGroup) findViewById(R.id.eventsRadioGroup);
        Button btn = (Button)findViewById(R.id.searchbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                RadioButton rbtn = (RadioButton) findViewById(rgroup.getCheckedRadioButtonId());
                TextView txtView = (TextView) findViewById(R.id.eventsTextView);
                Paramters parameterLsit = new Paramters();
                Map<String, String> params = new HashMap<>();
                String txt = rbtn.getText().toString();
                UserGlobalData ugd = UserGlobalData.getInstance();
                if (txt.equals("Location")) {
                    if(ugd.location == null){
                        Context context = getApplicationContext();
                        CharSequence text = "Enable location!!";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    else{
                        String[] location = ugd.location.split(" ");
                        CoordinateOptions coordinate = CoordinateOptions.builder()
                                .latitude(Double.parseDouble(location[0]))
                                .longitude(Double.parseDouble(location[1])).build();
                        params.put("term", "Italian food");
                       parameterLsit.location = coordinate;
                    }
                } else {
                    int[] user_interests = ugd.interests;
                    int N = ugd.getN_INTERESTS();
                    if (user_interests[0] == 1) {
                        params.put("term", "Italian food");
                    }
                    if (user_interests[1] == 1) {
                        params.put("term", "club");
                    }
                    if (user_interests[2] == 1) {
                        params.put("term", "Indian cusine");
                    }
                    if (user_interests[3] == 1) {
                        params.put("term", "long drive");
                    }
                    if (user_interests[4] == 1) {
                        params.put("term", "religious");
                    }
                    if (user_interests[5] == 1) {
                        params.put("term", "group");
                    }
                    if (user_interests[6] == 1) {
                        params.put("term", "house party");
                    }
                    if (user_interests[7] == 1) {
                        params.put("term", "pets");
                    }
                    if (user_interests[8] == 1) {
                        params.put("term", "concerts");
                    }
                    if (user_interests[9] == 1) {
                        params.put("term", "sports");
                    }
                    parameterLsit.city = "Tempe";
                }
                if (params.size() == 0) {
                    params.put("term", "food");
                    parameterLsit.city = "Tempe";
                }
                params.put("sort", "2");
                Search sch = new Search();
                sch.execute(parameterLsit);
                try {
                    String s = sch.get();
                    txtView.setText(s);
                } catch (Exception exp) {

                }
            }
        });

    }
    private  class Paramters {
        Map<String,String> params;
        String city;
        CoordinateOptions location;
    }
    private class Search extends AsyncTask< Paramters , Integer, String> {

        protected String doInBackground(Paramters...paramter){
            String result = new String("");
            YelpAPIFactory apifactory = new YelpAPIFactory("aPvXqXfyK1QIqe2M1LPdwg","TYhUXOaxnhwMxu2KYPPvOkYk7OA","GEYqzl6lRkzLPQyiKRoSegHYq7G5FJKh","11nx56ticbpKYeCE1vEJYywX4vM");
            YelpAPI api = apifactory.createAPI();
            try {
                Call<SearchResponse> call;
                if(paramter[0].location != null) {
                    call = api.search(paramter[0].location, paramter[0].params);
                }
                else{
                    call = api.search(paramter[0].city, paramter[0].params);
                }
                response = call.execute();
                ArrayList<Business> list = response.body().businesses();
                for(int i = 0; i < list.size(); i++){
                       Business business = list.get(i);

                       String name = business.name();
                       String phone = business.displayPhone();
                       String rating = business.rating().toString();
                        if(name != null) {
                            result += name + "\n";
                        }
                        if(phone != null){
                           result += phone + "\n";
                        }
                        if(rating != null){
                            result += "rating: " + rating + "\n\n";
                        }
                }
            }
            catch (Exception exp){
                result = exp.getMessage();
            }
            return  result;
        }
        protected void onPreExecute(){

        }
        protected void onProgressUpdate(Integer y){

        }
        protected void onPostExecute(String z){

        }
    }

}
