package computing.mobile.instameet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NearbyUsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_users);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ListView lv = (ListView) this.findViewById(R.id.nearUsersListView);
        ArrayList items = new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.near_users_row_layout,R.id.rowTextUsername,items);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int i, long l) {
                TextView v = (TextView) view.findViewById(R.id.rowTextUsername);
                //Toast.makeText(getApplicationContext(), "selected Item Name is " + v.getText(), Toast.LENGTH_LONG).show();
                UserGlobalData ugd = UserGlobalData.getInstance();
                new APIClient().sendRequest(ugd.username, ugd.password,((String)v.getText()).split("Username:")[1].trim(), getApplicationContext());
            }
        });
        Bundle extras = getIntent().getExtras();
        for(int i=0; i<extras.size(); i++){
            String[] row = extras.getStringArray("user"+i);
            String name = row[1].equals("")?row[0]:row[1];
            String dist = row[2];
            if(dist.equals("")){
                items.add(0, name);
                adapter.notifyDataSetChanged();
            }
            else {
                items.add(0, name + "    (" + dist + " miles away)\nUsername: "+row[0]);
                adapter.notifyDataSetChanged();
            }
            lv.smoothScrollToPosition(0);
        }
    }
}
