package computing.mobile.instameet;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DataOperator dop = new DataOperator(getApplicationContext(),UserHistoryData.UserHistoryDataEntry.TABLE_NAME);
        UserGlobalData ugd = UserGlobalData.getInstance();
        cursor = dop.getHistory(dop, ugd.username);
        cursor.moveToLast();
        ListView lv = (ListView) this.findViewById(R.id.historyListView);
        ArrayList items = new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.history_row_layout,R.id.rowTextView,items);
        lv.setAdapter(adapter);
        try {
            String name = cursor.getString(0);
            name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
            items.add(0,name + "  " + cursor.getString(1));
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
        while(cursor.moveToPrevious()){
            String name = cursor.getString(0);
            name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
            items.add(0,name + "  " + cursor.getString(1));
            adapter.notifyDataSetChanged();
        }
        lv.smoothScrollToPosition(0);
    }
}