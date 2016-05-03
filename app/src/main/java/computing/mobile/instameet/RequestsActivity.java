package computing.mobile.instameet;

import android.database.Cursor;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestsActivity extends AppCompatActivity {

    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        RequestUserTracker.getInstance().flushMap();
        UserHistoryOperator dop = new UserHistoryOperator(getApplicationContext(),UserRequestData.UserRequestDataEntry.TABLE_NAME);
        final UserGlobalData ugd = UserGlobalData.getInstance();
        cursor = dop.getRequest(dop, ugd.username, "PENDING");
        cursor.moveToLast();
        ListView lv = (ListView) this.findViewById(R.id.listViewRequests);
        ArrayList items = new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.request_row_layout,R.id.rowRequestTextView,items){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    convertView = (LinearLayout) inflater.inflate(R.layout.request_row_layout, null);
                }
                String[] textItems = RequestUserTracker.getInstance().get(position+"").split("::");
                String name = textItems[0];
                String email = textItems.length>1?textItems[1]:"";
                String itemText = "Username: "+name+ (email.equals("")?"":"\n"+"Email: "+ email);
                ((TextView)convertView.findViewById(R.id.rowRequestTextView)).setText(itemText);
                Button acceptButton = (Button)convertView.findViewById(R.id.buttonAccept);
                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ugd.rewards += 25;
                        new APIClient().updateRequest(RequestUserTracker.getInstance().get(position+"").split("::")[0],UserGlobalData.getInstance().username,UserGlobalData.getInstance().password,"ACCEPTED",getApplicationContext());
                    }
                });

                Button rejectButton = (Button)convertView.findViewById(R.id.buttonReject);
                rejectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new APIClient().updateRequest(RequestUserTracker.getInstance().get(position+"").split("::")[0],UserGlobalData.getInstance().username,UserGlobalData.getInstance().password,"REJECTED",getApplicationContext());
                    }
                });

                return convertView;
            }
        };
        lv.setAdapter(adapter);
        RequestUserTracker map = RequestUserTracker.getInstance();
        try{
            String name = cursor.getString(0);
            String email = cursor.getString(1);
            String itemText = "Username: "+name+ (email.equals("")?"":"\n"+"Email: "+ email);
            map.put(items.size()+"",name+"::"+email);
            items.add(0,itemText);
            adapter.notifyDataSetChanged();
            Log.d("DEBUG_LISTVIEW","Added row "+ name + " "+ email);

        } catch (Exception e){
            e.printStackTrace();
        }
        while(cursor.moveToPrevious()){
            String name = cursor.getString(0);
            String email = cursor.getString(1);
            String itemText = "Username: "+name+ (email.equals("")?"":"\n"+"Email: "+ email);
            map.put(items.size()+"",name+"::"+email);
            items.add(0,itemText);
            adapter.notifyDataSetChanged();
            Log.d("DEBUG_LISTVIEW","Added row "+ name + " "+ email);
        }
        lv.smoothScrollToPosition(0);
    }

}
